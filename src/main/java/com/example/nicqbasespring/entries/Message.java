package com.example.nicqbasespring.entries;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
public class Message {
    private String from;
    private String to;
    private Object data;
    private DataType dataType;
    private MessageType messageType;
    private String messageid;

    public Message(
            String from,
            String to,
            Object data,
            DataType dataType,
            MessageType messageType) {
        this.from = from;
        this.to = to;
        this.data = data;
        this.dataType = dataType;
        this.messageType = messageType;
    }
}