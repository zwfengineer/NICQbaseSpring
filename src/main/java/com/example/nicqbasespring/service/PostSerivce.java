package com.example.nicqbasespring.service;

import clojure.lang.Obj;
import com.example.nicqbasespring.controller.WebSocketConnect;
import com.example.nicqbasespring.entries.DataType;
import com.example.nicqbasespring.entries.Message;
import com.example.nicqbasespring.entries.MessageType;
import com.example.nicqbasespring.exception.MessageErrorType;
import com.example.nicqbasespring.exception.MessageException;
import com.example.nicqbasespring.util.UserUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j
public class PostSerivce extends AbstraService{
    private HistoryMessageService historyMessageService;

    @Autowired(required = false)
    public void setHistoryMessageService(HistoryMessageService historyMessageService) {
        this.historyMessageService = historyMessageService;
    }

    @Transactional
    public  void Post(@NotNull Message message, Session session) throws EncodeException, IOException {
        switch (message.getMessageType()){
            case UserMessage:
                PostUser(message,session);
                break;
            case SystemMessage:
                PostSystem(message, session);
                break;
            case AddFriendRequest:
                PostAddFriendRequest(message,session);
                break;
        }
    }

    private  void PostSystem(Message message,Session session){

    }

    private  void PostUser(@NotNull Message message, Session session) throws EncodeException, IOException {
        try{
            PostUserCheck(message.getFrom(),message.getTo());
            if (userDao.inHashTable("onlineuser",message.getTo())){
                WebSocketConnect connect = WebSocketConnect.getConnectbyUserid(message.getTo());
                connect.session.getBasicRemote().sendObject(message);
            }
            historyMessageService.save(message);

        }catch (MessageException messageException){
            WebSocketConnect connect = WebSocketConnect.getConnectbySessionid(session.getId());
            connect.session.getBasicRemote().sendObject(new Message(
                    "System",
                    UserUtil.getHttpSessionUser(connect.httpSession).getUID(),
                    DataType.Text,
                    new Timestamp(System.currentTimeMillis()),
                    MessageType.DeliverFeedback
            ));
        }

    }


    private  void PostAddFriendRequest(@NotNull Message message, Session session) throws EncodeException, IOException {
         if(PostAddFriendRequestCheck(message.getFrom(),message.getTo())){
             ObjectNode objectNode = (ObjectNode) objectMapper.readTree("{\"posted\":\"true\"}");
             try {
                 messageDao.addFriendRequest(message);
                 message.setData(objectNode);
                 WebSocketConnect.addfirendrequestevent(message.getTo());
             }catch (MessageException messageException){
                 objectNode.put("posted","false");
                 objectNode.put("message", String.valueOf(messageException));
                 message.setData(objectNode);
             }
             session.getBasicRemote().sendObject(message);
        }
    }

    public Boolean PostAddFriendRequestCheck(String uid,String fid){
        return userExistCheck(uid,fid);
//
//        check black list!
//
    }

    public void PostUserCheck(String uid,String fid){
        if(! userExistCheck(uid,fid)){
            throw new MessageException(MessageErrorType.User_Invilid);
        }
        if(userDao.checkFriend(uid, fid) != 1){
            throw new MessageException(MessageErrorType.Not_Friend);
        }
    }

    public Boolean userExistCheck(String uid,String fid){
        if(! userDao.checkUserNum(uid).equals(1)){
            return false;
        }
        if (! userDao.checkUserNum(fid).equals(1)){
            return false;
        }
        return true;
    }

    public Set<ObjectNode> GetAddFriendRequest(String uid)  {
        Set<Object> addfriendrequestlist = messageDao.getFriednRequest(uid);
        Set<ObjectNode> addfriendrequestnodelist = new HashSet<>();
        for (Object object : addfriendrequestlist){
            ObjectNode objectNode = objectMapper.valueToTree(object);
            objectNode.put("fname", userDao.getUserName(((Message)object).getFrom()));
            addfriendrequestnodelist.add(objectNode);
        }
        return addfriendrequestnodelist;
    }
}