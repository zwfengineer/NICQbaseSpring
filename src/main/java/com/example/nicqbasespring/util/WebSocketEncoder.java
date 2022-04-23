package com.example.nicqbasespring.util;

import com.example.nicqbasespring.entries.Message;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class WebSocketEncoder implements Encoder.Text<Message>{

    @Override
    public String encode(Message message) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.valueToTree(message).toString();
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}