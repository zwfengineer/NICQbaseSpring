package com.example.nicqbasespring.dao;

import com.example.nicqbasespring.entries.Message;
import com.example.nicqbasespring.entries.User;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.Map;

public interface UserDaoImpl {
     String getUserName(String uid);
     Object checkUserNum(User user);
     Object checkUserNum(String uid);
     Object modifyUserLevel(User user);
     Object modifyUserStatus(User user);
     Object modifyUserInfo(User user);
     Object getUid(String username);
     Object addUser(User user);
     Object getAllUserNum();
     Object getUserNum(String username);
     Object getUser(String uid);
     Object checkFriend(String uid,String fid);
     void addFriend(String uid,String fid);
     void removeFriend(String uid,String fid);
     List<Map<String,Object>> getFriends(String uid)throws JsonProcessingException;
     List<Map<String,Object>> searchUser(String uid);
     List<Map<String,Object>> searchUser(String uid, String gender);

}
