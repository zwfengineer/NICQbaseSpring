package com.example.nicqbasespring.service;

import com.example.nicqbasespring.entries.Message;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class HistoryMessageService extends AbstraService{
    public void save(Message message){
        messageDao.send(message);
        messageDao.accept(message);
    }
    public Map<String,List<Message>> load(String uid){
       List<Map<String,Object>> friends =  userDao.getFriends(uid);
       Map<String,List<Message>> data = new HashMap<>();
       for(Map<String,Object> friend:friends){
           data.put(
                   (String) friend.get("fid")
                   ,messageDao.loadHistoryMessage(
                           uid,
                           (String) friend.get("fid"),
                           0,
                           10));
       }
       return data;
    }
}
