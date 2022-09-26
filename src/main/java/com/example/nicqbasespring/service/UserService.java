package com.example.nicqbasespring.service;

import com.example.nicqbasespring.controller.WebSocketConnect;

import com.example.nicqbasespring.entries.User;
import com.example.nicqbasespring.util.UserUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

import java.text.SimpleDateFormat;
import java.util.*;



@Service
@Slf4j
public  class UserService extends AbstraService {
    @Autowired
    PasswordEncoder passwordEncoder;
    public Object LoginServer(@NotNull User user, HttpSession httpSession){
        try{
            User datauser= (User) userDao.getUser(userDao.getUid(user.getUserName()));
            if(passwordEncoder.matches(user.getPasswd(),datauser.getPasswd())){
                userDao.Login(datauser.getUID(), httpSession.getId());
                datauser.setPasswd(user.getPasswd());
                try{
                    Thread.sleep(100);
//                    优化点！！！！！
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                return datauser;
            }else{
                log.info("LoginFaild");
                return "用户名或密码错误";
            }
        } catch (EmptyResultDataAccessException e) {
            return "用户名或密码错误，请重新输入";
        }
    }
    public Object RegisterUser(@NotNull User user) {
        if(((Integer)userDao.getUserNum(user.getUserName()))>=1)
        {return  "Duplicate UserName";}
        String count =String.valueOf((Integer) userDao.getAllUserNum()+1);
        String uid = new SimpleDateFormat("yy-MM-dd").format(new Date()) +"-"+ count;
        Random random = new Random();
        String avatar =  String.valueOf(random.nextInt(12));
        user.setUID(uid);
        user.setAvatar(avatar);
        user.setPasswd(passwordEncoder.encode(user.getPasswd()));
        if ((Integer)userDao.addUser(user)==1){
            userDao.createoutbox(user.getUID());
            userDao.createinbox(user.getUID());
            return user;
        }else{
            return "UNKNOW ERROR";
        }
    }
    public Object addFriends(@NotNull User user, String fid) {
        Object obj = userDao.checkUserNum(fid);
        if(! user.getUID().equals(fid)) {
            switch (obj.getClass().getSimpleName()) {
                case "Integer":
                    if (((Integer) obj) == 1) {
                        Integer ckfObj = userDao.checkFriend(user.getUID(), fid);
                        messageDao.cleanaddFriendRequestlist(user.getUID(),fid);
                        if(isOnline(fid)){
                            WebSocketConnect.addfriendEvent(fid);
                        }
                        if (ckfObj != 0) {
                            return "OK:已经是好友了！";
                        } else {
                            userDao.addFriend(user.getUID(), fid);
                            return "OK:已经添加好友";
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
        if (userDao.checkFriend(user.getUID(),fid) ==1){
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
            return userDao.getUserName(uid);
        }catch (EmptyResultDataAccessException exception){
            return "NULL";
        }
    }

    public Object getUserAttribute(String id,String attr){
        Object object = userDao.getUser(id);
        if(object.getClass().getSimpleName().equals("User")){
            return objectMapper.valueToTree(object).get(attr);
        }
        return null;
    }
}
