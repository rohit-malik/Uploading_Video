package com.example.nitinmalik.uploading_video;

public class VideoEvent {
    private int event_ID;
    private String event_name;
    private String event_info;

    public VideoEvent(int id, String name, String info){
        event_ID = id;
        event_name = name;
        event_info = info;
    }

    public int getEvent_ID(){
        return event_ID;
    }

    public String getEvent_name(){
        return event_name;
    }

    public String getEvent_info(){
        return event_info;
    }
}
