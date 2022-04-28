package com.example.nicqbasespring.entries;

public enum MessageType {
    Heart,
    UserMessage,
    SystemMessage,
    AddFriendRequest
}


//
//import com.example.nicqbasespring.config.NicqEnumtrator;
//
//public enum MessageType implements NicqEnumtrator {
//    Heart(0,"Heart"),
//    UserMessage(1,"UserMessage"),
//    SystemMessage(2,"SystemMessage"),
//    AddFriendRequest(3,"AddFriendRequest");
//    private final int code;
//    private final String desc;
//    MessageType(int code,String desc){
//        this.code=code;
//        this.desc=desc;
//    }
//    @Override
//    public int Code() {
//        return this.code;
//    }
//
//    @Override
//    public String Desc() {
//        return this.desc;
//    }
//}
