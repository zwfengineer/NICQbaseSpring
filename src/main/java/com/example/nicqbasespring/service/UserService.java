package com.example.nicqbasespring.service;

import com.example.nicqbasespring.controller.WebSocketConnect;
import com.example.nicqbasespring.dao.MessageDao;
import com.example.nicqbasespring.dao.UserDao;
import com.example.nicqbasespring.entries.Message;
import com.example.nicqbasespring.entries.User;
import com.example.nicqbasespring.util.UserUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.websocket.EncodeException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


@Service
@Slf4j
public  class UserService extends AbstraService {
    public Object LoginServer(@NotNull User user, HttpSession httpSession){
        user.setUID(String.valueOf( userDao.getUid(user.getUserName())));
        Integer num = (Integer) userDao.checkUserNum(user);
        log.info(num.toString());
        if(num==1) {
            userDao.Login(user.getUID(), httpSession.getId());
            return userDao.getUser(user.getUID());
        }
        else {
            return "用户名或密码错误，请重新输入";
        }
    }
    public Object RegisterUser(@NotNull User user) {
        if(((Integer)userDao.getUserNum(user.getUserName()))>=1){return  "Duplicate UserName";}
        String count =String.valueOf((Integer) userDao.getAllUserNum()+1);
        String uid = new SimpleDateFormat("yy-MM-dd").format(new Date()) +"-"+ count;
        user.setUID(uid);
        if ((Integer)userDao.addUser(user)==1){
            userDao.createoutbox(user.getUID());
            userDao.createinbox(user.getUID());
            return user;
        }else{
            return "UNKNOW ERROR";
        }
    }
    public Object addFriends(@NotNull User user, String fid) {
        /*
        请确保 user 真实有效
        */
        Object obj = userDao.checkUserNum(fid);
        if(! user.getUID().equals(fid)) {
            switch (obj.getClass().getSimpleName()) {
                case "Integer":
                    if (((Integer) obj) == 1) {
                        Object ckfObj = userDao.checkFriend(user.getUID(), fid);
                        if ("Integer".equals(ckfObj.getClass().getSimpleName())) {
                            messageDao.cleanaddFriendRequestlist(user.getUID(),fid);
                            if(isOnline(fid)){
                                WebSocketConnect.addfriendEvent(fid);
                            }
                            if ((Integer) ckfObj != 0) {
                                return "OK:已经是好友了！";
                            } else {
                                userDao.addFriend(user.getUID(), fid);
                                return "OK:已经添加好友";
                            }
                        } else {
                            return ckfObj;
                        }
                    } else {
                        return "ERR:没有这个用户！";
                    }
                case "String":
                    return obj;
            }
        }
        return "ERR:积极是积极的朋友";
    }
    public Object removeFriend(@NotNull User user, String fid){
        if ((Integer)userDao.checkFriend(user.getUID(),fid)==1){
            userDao.removeFriend(user.getUID(),fid);
            return "OK:已经解除好友";
        }else {
            return "ERR:没加好友";
        }
    }
    public Object getFriends(@NotNull User user)  {
        if ((Integer)userDao.checkUserNum(user.getUID())==1){
            if ((Integer)userDao.checkFriend(user.getUID())>0){
                List<Map<String,Object>> datas =  userDao.getFriends(user.getUID());
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.valueToTree(datas);
            }else{
                return "OK:0";
            }
        }
        return null;
    }
    public Object searchUser(@NotNull String useridname){
        List<Map<String,Object>> data =  userDao.searchUser(useridname);
        if (data.size()<1){
            return "null";
        }
        return data;
    }
    public void Logout(String uid){
        userDao.deleteHash("onlineuser",uid);
    }
    public Boolean isOnline(String uid){
        try{
            return userDao.inHashTable("onlineuser",uid);
        }catch (Exception e){
            return false;
        }

    }
    public Boolean isOnline(HttpSession httpSession){
        try{
            return userDao.inHashTable("onlineuser", UserUtil.getHttpSessionUser(httpSession).getUID());
        }catch (Exception e){
            return false;
        }

    }
    public String getUserName(String uid){
        try {
            return (String) userDao.getUserName(uid);
        }catch (EmptyResultDataAccessException exception){
            return "NULL";
        }
    }
}
