package com.example.nicqbasespring.controller;

import com.example.nicqbasespring.entries.Message;
import com.example.nicqbasespring.entries.User;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Controller
@ServerEndpoint("/wsapi")
public  class webSocketCon {
    private static Map<String, webSocketCon> OnlineUserList = new ConcurrentHashMap<>();
    private Session session;
    private HttpSession httpSession;

    @OnOpen
    public void OnOpen(Session session, EndpointConfig config){
        this.session=session;
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getSimpleName());
        OnlineUserList.put((String) httpSession.getAttribute("uid"),this);
    }

    @OnMessage
    public void OnMessage(String message,Session session){

    }
    @OnClose
    public void OnClose(Session session){

    }
    @OnError
    public  void OnError(Session session){

    }

    public void BorderCastUserList(Message message, List<String> userList)throws EncodeException, IOException{
        for (String user:userList) {
            if (OnlineUserList.containsKey(user)) {
                OnlineUserList.get(user).session.getBasicRemote().sendObject(message);
            }
        }
    }
    public void SendUser(String uid,Message message) throws EncodeException, IOException {
        if (OnlineUserList.containsKey(uid)){
            OnlineUserList.get(uid).session.getBasicRemote().sendObject(message);
        }else{

        }
    }
}
