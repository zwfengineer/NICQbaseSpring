package com.example.nicqbasespring.service;

import com.example.nicqbasespring.entries.Message;
import org.springframework.stereotype.Component;

@Component
public class HistoryMessageService extends AbstraService{
    public void save(Message message){
        messageDao.send(message);
        messageDao.accept(message);
    }
    public Message[] load(String uid,int base,int offset){
        return new Message[0];
    }
}
