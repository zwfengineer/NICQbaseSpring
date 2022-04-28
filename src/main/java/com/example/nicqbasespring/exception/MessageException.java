package com.example.nicqbasespring.exception;


public class MessageException extends RuntimeException{
    private final MessageErrorType messageErrorType;
    public MessageException(MessageErrorType messageErrorType){
        this.messageErrorType = messageErrorType;
    }

    @Override
    public String toString() {
        return this.messageErrorType.toString();
    }
}
