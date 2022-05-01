package com.example.nicqbasespring.entries;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private String from;
    private String to;
    private Object data;
    private Timestamp unixTime;
    private DataType dataType;
    private MessageType messageType;

    public Message(
            String from,
            String to,
            DataType dataType,
            Timestamp unixTime,
            MessageType messageType) {
        this.from = from;
        this.to = to;
        this.dataType = dataType;
        this.messageType = messageType;
        this.unixTime = unixTime;
    }

    public Object getData() {
        if (data == null){
            return "";
        }
        return data;
    }

}