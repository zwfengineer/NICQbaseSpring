package com.example.nicqbasespring.util;

import javax.websocket.CloseReason;

public enum WebSocketCloseCode implements CloseReason.CloseCode{
    USER_REPEAT_CONNECT(4000),
    USER_SESSION_TIMEOUT(4001);
    private final int code;
    WebSocketCloseCode(int i) {
        this.code = i;
    }
    @Override
    public int getCode() {
        return this.code;
    }
}