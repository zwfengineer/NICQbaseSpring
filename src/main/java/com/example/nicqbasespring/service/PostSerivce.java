package com.example.nicqbasespring.service;

import com.example.nicqbasespring.entries.Message;
import com.example.nicqbasespring.exception.MessageException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;

@Component
@Slf4j
public class PostSerivce extends AbstraService{

    public  void Post(@NotNull Message message, Session session) throws EncodeException, IOException {
        switch (message.getMessageType()){
            case UserMessage:
                PostUser(message,session);
            case SystemMessage:
                PostSystem(message, session);
            case AddFriendRequest:
                PostAddFriendRequest(message,session);
        }
    }
    private  void PostSystem(Message message,Session session){

    }
    private  void PostUser(Message message,Session session){

    }
    private  void PostAddFriendRequest(@NotNull Message message, Session session) throws EncodeException, IOException {
         if(Postcheck(message.getFrom(),message.getTo())){
             ObjectNode objectNode = (ObjectNode) objectMapper.readTree("{\"posted\":\"true\"}");
             try {
                 messageDao.addFriendRequest(message);
                 message.setData(objectNode);
                 session.getBasicRemote().sendObject(message);
             }catch (MessageException messageException){
                 objectNode.put("posted","false");
                 objectNode.put("message", String.valueOf(messageException));
                 message.setData(objectNode);
             }
        }
    }

//     Test！！！！
    public Boolean Postcheck(String uid,String fid){
        if((Integer)userDao.checkUserNum((String) userDao.getUid(uid))!=1){
            return false;
        }
        if ((Integer)userDao.checkUserNum((String) userDao.getUid(fid))!=1){
            return false;
        }
        return true;
    }
}
