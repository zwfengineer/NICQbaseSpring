package com.example.nicqbasespring.service;

import com.example.nicqbasespring.dao.UserDao;
import com.example.nicqbasespring.entries.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;


@Component
public  class UserService {
    private UserDao userDao;
    private Logger log;

    @Autowired(required = false)
    public void setLog(Logger log) {
        this.log = log;
    }

    @Autowired(required = false)
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
    public Object LoginServer(@NotNull User user){
        user.setUID(String.valueOf( userDao.getUid(user.getUserName())));
        Integer num = (Integer) userDao.checkUserNum(user);
        log.info(num.toString());
        if(num==1) {
            return this.userDao.getUser(user.getUID());
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
        user.setPermission("normal");
        user.setAvatar("0");
        if ((Integer)userDao.addUser(user)==1){
            return user;
        }else{
            return "UNKNOW ERROR";
        }
    }
    public Object addFriends(User user,String fid){
        /*
                请确保 user 真实有效
        */
        Object obj = userDao.checkUserNum(fid);
        switch (obj.getClass().getSimpleName()){
            case "Integer":
                if (((Integer) obj) == 1){
                    Object ckfObj =  userDao.checkFriend(user.getUID(),fid);
                    if ("Integer".equals(ckfObj.getClass().getSimpleName())) {
                        if ((Integer) ckfObj != 0) {
                            return "OK:已经是好友了！";
                        } else {
                            userDao.addFriend(user.getUID(), fid);
                            return "OK:已经添加好友";
                        }
                    }else {
                        return (String)ckfObj;
                    }
                }else {
                    return "ERR:没有这个用户！";
                }
            case "String":
                return (String)obj;
        }
        return null;
    }
    public Object removeFriend(@NotNull User user, String fid){
        if ((Integer)userDao.checkFriend(user.getUID(),fid)==1){
            userDao.removeFriend(user.getUID(),fid);
            return "OK:已经解除好友";
        }else {
            return "ERR:没加好友";
        }
    }
    public Object getFriends(@NotNull User user){
        if ((Integer)userDao.checkUserNum(user.getUID())==1){
            if ((Integer)userDao.checkFriend(user.getUID())>0){
                return  userDao.getFriends(user.getUID());
            }else{
                return "OK:0";
            }
        }
        return null;
    }
}
