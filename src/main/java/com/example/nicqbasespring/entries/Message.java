package com.example.nicqbasespring.entries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Message {
    private String from;
    private String to;
    private Object Message;
    private MessageType MessageType;

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

    public MessageType getMessageType() {
        return MessageType;
    }

    public void setMessageType(MessageType messageType) {
        MessageType = messageType;
    }

}
enum MessageType{
    Text,
    FullText,
    Video,
    OnlineVideo,
    Voice,
    OnlineVioce,
}
