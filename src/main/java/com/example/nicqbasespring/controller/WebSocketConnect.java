package com.example.nicqbasespring.controller;
import com.example.nicqbasespring.config.NicqWebSocketConfig;
import com.example.nicqbasespring.entries.Message;
import com.example.nicqbasespring.entries.User;
import com.example.nicqbasespring.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint(value = "/wsapi",configurator = NicqWebSocketConfig.class)
public  class WebSocketConnect {
    private final Logger log = LoggerFactory.getLogger(WebSocketConnect.class);
    private  UserService userService;
    private static final Map<String, WebSocketConnect> onLineUserList = new ConcurrentHashMap<>();
    private HttpSession httpSession;
    private Session session;
    private boolean valid;
    @Autowired(required = false)
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @OnOpen
    public void onOpen(@NotNull Session session, EndpointConfig config) throws IOException {
        if (usercheck(session,config)){
            this.session = session;
            this.valid = true;
            this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
            onLineUserList.put(session.getId(),this);
            User user = (User) this.httpSession.getAttribute("user");
            log.info("user:"+user.getUserName()+"Connect success"+"UserNums:"+onLineUserList.size());
        }
    }

    @OnMessage
    public void onMessage(String message,Session session){
        ObjectMapper objectMapper  = new ObjectMapper();
        JsonNode jsondata = objectMapper.valueToTree(message);
        log.info(message);
    }
    @OnClose
    public void onClose(@NotNull Session session){
        if(this.valid) {
            User user =
                    (User) onLineUserList
                            .get(session.getId())
                            .httpSession
                            .getAttribute("user");
            log.info("user: " + user.getUserName() + " logout");
            onLineUserList.remove(session.getId());
        }else {
            log.info("evil user break off");
        }
    }
    @OnError
    public void onError(Session session, @NotNull Throwable error){
        error.printStackTrace();
        log.info(onLineUserList.get(session.getId()).httpSession.getId());
    }


    public Boolean checkrepeat(String sessionid){
        for (WebSocketConnect wsc : onLineUserList.values()){
            if(wsc.httpSession.getId().equals(sessionid)){
                return true;
            }
        }
        return false;
    }

    public boolean usercheck(Session session,EndpointConfig config) throws IOException {
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        if (httpSession == null){
            log.info("Unknow Error");
            session.close();
            return false;
        }
        if (checkrepeat(httpSession.getId())){
            log.info("user repeat Login");
            session.close();
            return false;
        }
        if (httpSession.getAttribute("Logined") == null || (httpSession.getAttribute("user")==null)){
            log.info("user unlogin");
            session.close();
            return false;
        }
        if(! (Boolean) httpSession.getAttribute("Logined")){
            log.info("user loginout!");
            session.close();
            return false;
        }
        return true;
    }

    public static void logout(HttpSession httpSession) throws IOException {
        for(WebSocketConnect webSocketConnect:onLineUserList.values()){
            if (Objects.equals(webSocketConnect.httpSession.getId(), httpSession.getId())){
                webSocketConnect.session.close();
            }
        }
    }

    //广播
    public void BorderCastUserList(Message message, @NotNull List<String> userList)throws EncodeException, IOException{
        for (String user:userList) {
            if (onLineUserList.containsKey(user)) {
                onLineUserList.get(user)
                        .session
                        .getBasicRemote()
                        .sendObject(message);
            }
        }
    }

    public void SendUser(String sessionid,Message message) throws EncodeException, IOException {
        if (onLineUserList.containsKey(sessionid)){
            onLineUserList.get(sessionid).session.getBasicRemote().sendObject(message);
        }
    }
}
