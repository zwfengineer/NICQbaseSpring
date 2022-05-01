package com.example.nicqbasespring.exception;

public class WebsocketException extends RuntimeException{
    private final WebsocketExceptionType websocketExceptionType;
    public WebsocketException(WebsocketExceptionType websocketExceptionType){
        this.websocketExceptionType = websocketExceptionType;
    }

    @Override
    public String toString() {
        return this.websocketExceptionType.toString();
    }
}
