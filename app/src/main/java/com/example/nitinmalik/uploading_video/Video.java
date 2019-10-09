package com.example.nitinmalik.uploading_video;

public class Video {
    private int video_ID;
    private String video_name;
    private String video_desc;

    public Video(int id, String name, String desc){
        video_ID = id;
        video_name = name;
        video_desc = desc;
    }

    int getVideo_ID(){
        return video_ID;
    }

    String getVideo_name(){
        return video_name;
    }

    String getVideo_desc(){
        return video_desc;
    }
}
