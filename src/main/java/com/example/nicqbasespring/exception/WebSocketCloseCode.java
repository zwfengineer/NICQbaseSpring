package com.example.nicqbasespring.exception;

import javax.websocket.CloseReason;

public enum WebSocketCloseCode implements CloseReason.CloseCode{
    USER_REPEAT_CONNECT(4000),
    USER_SESSION_TIMEOUT(4001),
    USER_SESSION_DESTORY(4002),
    Serve_Shutdown_Proactively(4003),
    USER_PROACTIVELY_EXIT(4004);
    private final int code;
    WebSocketCloseCode(int i) {
        this.code = i;
    }
    @Override
    public int getCode() {
        return this.code;
    }
}