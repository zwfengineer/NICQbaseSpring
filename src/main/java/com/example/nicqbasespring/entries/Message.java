package com.example.nicqbasespring.entries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Message {
    private String from;
    private String to;
    private Object Message;
    private String MessageType;
    private static Map<String, List<Message>> HistoryMessage  = new ConcurrentHashMap<>();

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Object getMessage() {
        return Message;
    }

    public void setMessage(Object message) {
        Message = message;
    }

    public String getMessageType() {
        return MessageType;
    }

    public void setMessageType(String messageType) {
        MessageType = messageType;
    }

    public void setHistoryMessage(String uid){
        if(HistoryMessage.containsKey(uid)){
            HistoryMessage.get(uid).add(this);
        }else{
            List<Message> messages = new ArrayList<>();
            messages.add(this);
            HistoryMessage.put(uid,messages);
        }
    }

    public List<Message> getHistoryMessage(String uid){
        return HistoryMessage.getOrDefault(uid, null);
    }
}
