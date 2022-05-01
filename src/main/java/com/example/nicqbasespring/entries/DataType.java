package com.example.nicqbasespring.entries;

public enum DataType {
    Text("Text"),
    FullText("FullText"),
    Video("Video"),
    OnlineVideo("OnlineVideo"),
    Voice("Voice"),
    OnlineVioce("OnlineVoice");
    private final String desc;
    DataType(String desc){
        this.desc = desc;
    }

    @Override
    public String toString() {
        return this.desc;
    }
}


//import com.example.nicqbasespring.config.NicqEnumtrator;
//
//public enum DataType implements NicqEnumtrator {
//    Text(0,"Text"),
//    FullText(1,"FullText"),
//    Video(2,"Video"),
//    OnlineVideo(3,"OnlineVideo"),
//    Voice(4,"Voice"),
//    OnlineVioce(5,"OnlineVoice");
//    private final int code;
//    private final String desc;
//    DataType(int code, String desc){
//        this.code=code;
//        this.desc = desc;
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
