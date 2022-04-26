package com.example.nicqbasespring.controller;
import com.example.nicqbasespring.config.NicqWebSocketConfig;
import com.example.nicqbasespring.entries.DataType;
import com.example.nicqbasespring.entries.Message;
import com.example.nicqbasespring.entries.User;
import com.example.nicqbasespring.service.UserService;
import com.example.nicqbasespring.util.UserUtil;
import com.example.nicqbasespring.util.WebSocketCloseCode;
import com.example.nicqbasespring.entries.MessageType;
import com.example.nicqbasespring.util.WebSocketEncoder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint(value = "/wsapi",configurator = NicqWebSocketConfig.class,encoders = WebSocketEncoder.class)
@Slf4j
public  class WebSocketConnect {
    private static final Map<String, WebSocketConnect> onLineUserList = new ConcurrentHashMap<>();
    private HttpSession httpSession;
    private Session session;
    private boolean valid;
    private static UserService userService;
    @Autowired(required = false)
    public void setUserService(UserService userService) {
        WebSocketConnect.userService = userService;
    }
    private static RedisTemplate redisTemplate;
    @Autowired(required = false)
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        WebSocketConnect.redisTemplate = redisTemplate;
    }

    @OnOpen
    public void onOpen(@NotNull Session session, EndpointConfig config) throws IOException {
        if (true){

        }else {
            if (usercheck(session, config)) {
                this.session = session;
                this.valid = true;
                this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
                User user = (User) this.httpSession.getAttribute("user");
                onLineUserList.put(session.getId(), this);
                redisTemplate.opsForHash().put("onlineuser", user.getUID(), httpSession.getId());
                log.info("user:" + user.getUserName() + "Connect success" + "UserNums:" + onLineUserList.size());
            }
        }
    }

    @OnMessage
    public void onMessage(String message,Session session) throws JsonProcessingException {
        ObjectMapper objectMapper  = new ObjectMapper();
        JsonNode jsondata = objectMapper.readTree(message);
        
        log.info(jsondata.get("user").asText());
    }

    @OnClose
    public void onClose(@NotNull Session session){
        if(this.valid) {
            try{
                User user = (User) this.httpSession.getAttribute("user");
                redisTemplate.opsForHash().delete("onlineuser",user.getUID());
                log.info("user: " + user.getUserName() + " logout");
            }catch (IllegalStateException illegalStateException){
                log.info("user session lost websocket server close");
            }
        }else {
            log.info("evil user break off");
        }
        onLineUserList.remove(session.getId());
    }

    @OnError
    public void onError(Session session, @NotNull Throwable error){
        error.printStackTrace();
        log.info(onLineUserList.get(session.getId()).httpSession.getId());
    }

    public Boolean checkrepeat(String httpsessionid){
        for (WebSocketConnect wsc : onLineUserList.values()){
            if(wsc.httpSession.getId().equals(httpsessionid)){
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
            session.close(new CloseReason(WebSocketCloseCode.USER_REPEAT_CONNECT,"UserRepeatConnect"));
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
                webSocketConnect.valid = false;
            }
        }
    }

    public static void logout(HttpSession httpSession,CloseReason closeReason)throws IOException {
        for(WebSocketConnect webSocketConnect:onLineUserList.values()){
            if (Objects.equals(webSocketConnect.httpSession.getId(), httpSession.getId())){
                webSocketConnect.session.close(closeReason);
                webSocketConnect.valid = false;
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

    public void SendUserMessage(String uid,Message message) throws EncodeException, IOException {
        for(WebSocketConnect webSocketConnect:onLineUserList.values()){
            if(((User)webSocketConnect.httpSession.getAttribute("User")).getUID().equals(uid)){
                webSocketConnect.session.getBasicRemote().sendObject(message);
            }
        }
    }

    public static int onlineusernum(){
        return onLineUserList.size();
    }

    public static void KeepHeart() throws EncodeException, IOException {
        for(WebSocketConnect webSocketConnect:onLineUserList.values()){
            webSocketConnect.session.getBasicRemote().sendObject(
                    new Message(
                            "System",
                            UserUtil.getHttpSessionUser(webSocketConnect.httpSession).getUID(),
                            new Date(),
                            DataType.Text,
                            MessageType.Heart
                    )
            );
        }
    }

}
