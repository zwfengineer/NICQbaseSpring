package com.example.nicqbasespring.controller;
import clojure.lang.Obj;
import com.example.nicqbasespring.config.NicqWebSocketConfig;
import com.example.nicqbasespring.entries.DataType;
import com.example.nicqbasespring.entries.Message;
import com.example.nicqbasespring.entries.User;
import com.example.nicqbasespring.exception.MessageErrorType;
import com.example.nicqbasespring.exception.MessageException;
import com.example.nicqbasespring.exception.WebsocketException;
import com.example.nicqbasespring.exception.WebsocketExceptionType;
import com.example.nicqbasespring.service.PostSerivce;
import com.example.nicqbasespring.service.UserService;
import com.example.nicqbasespring.util.UserUtil;
import com.example.nicqbasespring.util.WebSocketCloseCode;
import com.example.nicqbasespring.entries.MessageType;
import com.example.nicqbasespring.util.WebSocketEncoder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint(value = "/wsapi",configurator = NicqWebSocketConfig.class,encoders = WebSocketEncoder.class)
@Slf4j
public  class WebSocketConnect {
    public HttpSession httpSession;
    public Session session;
    private boolean valid;

    private static final Map<String, WebSocketConnect> onLineUserList = new ConcurrentHashMap<>();

    private static PostSerivce postSerivce;
    @Autowired
    public  void setPostSerivce(PostSerivce postSerivce) {
        WebSocketConnect.postSerivce = postSerivce;
    }

    private static UserService userService;
    @Autowired(required = false)
    public void setUserService(UserService userService) {
        WebSocketConnect.userService = userService;
    }
    private static RedisTemplate<String, Object> redisTemplate;
    @Autowired(required = false)
    public void setRedisTemplate(RedisTemplate<String, Object>  redisTemplate) {
        WebSocketConnect.redisTemplate = redisTemplate;
    }
    public static ObjectMapper objectMapper;
    @Autowired(required = false)
    public void setObjectMapper(ObjectMapper objectMapper) {
        WebSocketConnect.objectMapper = objectMapper;
    }

    @OnOpen
    public void onOpen(@NotNull Session session, EndpointConfig config) throws IOException {
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

    @OnMessage
    public void onMessage(String messagetext,Session session) throws IOException, EncodeException {
        Message message = objectMapper.readValue(messagetext,Message.class);
        System.out.println(message.toString());
        postSerivce.Post(message,session);
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
    public void onError(@NotNull Session session, @NotNull Throwable error){
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

    public boolean usercheck(Session session, @NotNull EndpointConfig config) throws IOException {
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        if (httpSession == null){
            log.info("Unknow Error");
//            session.close();
//            Websocket Message Model Test
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

    public static int onlineusernum(){
        return onLineUserList.size();
    }

    public static void KeepHeart() throws EncodeException, IOException {
        for(WebSocketConnect webSocketConnect:onLineUserList.values()){
            webSocketConnect.session.getBasicRemote().sendObject(
                    new Message(
                            "System",
                            UserUtil.getHttpSessionUser(webSocketConnect.httpSession).getUID(),
                            DataType.Text,
                            new Timestamp(System.currentTimeMillis()),
                            MessageType.Heart
                    )
            );
        }
    }

    public static  WebSocketConnect getConnectbySessionid(String id){
        WebSocketConnect webSocketConnect = onLineUserList.get(id);
        if( webSocketConnect != null){
            return webSocketConnect;
        }else {
            throw new WebsocketException(WebsocketExceptionType.Online_User_Lost);
        }
    }

    public static  WebSocketConnect getConnectbyHttpsessionid(String id){
        for (WebSocketConnect webSocketConnect:onLineUserList.values()) {
            if (webSocketConnect.httpSession.getId().equals(id)){
                return webSocketConnect;
            }
        }
        throw new WebsocketException(WebsocketExceptionType.Online_User_Lost);
    }

    public static WebSocketConnect getConnectbyUserid(String id){
        for (WebSocketConnect webSocketConnect:onLineUserList.values()) {
            if (UserUtil.getHttpSessionUser(webSocketConnect.httpSession).getUID().equals(id)){
                return webSocketConnect;
            }
        }
        throw new WebsocketException(WebsocketExceptionType.Online_User_Lost);
    }
}
