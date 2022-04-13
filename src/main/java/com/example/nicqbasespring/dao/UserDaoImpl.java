package com.example.nicqbasespring.dao;

import com.example.nicqbasespring.entries.Message;
import com.example.nicqbasespring.entries.User;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.Map;

public interface UserDaoImpl {
    public Object getUserName(String uid);
    public Object checkUserNum(User user);
    public Object checkUserNum(String uid);
    public Object modifyUserLevel(User user);
    public Object modifyUserStatus(User user);
    public Object modifyUserInfo(User user);
    public Object ConnectionUser(User user,String uid);
    public Object getUid(String username);
    public Object addUser(User user);
    public Object getAllUserNum();
    public Object getUserNum(String username);
    public Object putHistoryMessage(String uid, Message message);
    public Object getUser(String uid);
    public Object checkFriend(String uid,String fid);
    public void addFriend(String uid,String fid);
    public void removeFriend(String uid,String fid);
    public List<Map<String,Object>> getFriends(String uid)throws JsonProcessingException;
}
