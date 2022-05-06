package com.example.nicqbasespring.exception;

import javax.websocket.CloseReason;

public enum WebSocketCloseCode implements CloseReason.CloseCode{
    USER_REPEAT_CONNECT(4000),
    USER_SESSION_TIMEOUT(4001),
    Serve_Shutdown_Proactively(4002);
    private final int code;
    WebSocketCloseCode(int i) {
        this.code = i;
    }
    @Override
    public int getCode() {
        return this.code;
    }
}