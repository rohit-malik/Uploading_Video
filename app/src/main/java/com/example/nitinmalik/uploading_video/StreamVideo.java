package com.example.nitinmalik.uploading_video;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class StreamVideo extends AppCompatActivity {


    VideoView videoView;
    ProgressDialog progressDialog;
    String video_url = "http://172.26.1.221/AndroidUploadImage/";
    String event_name;
    String video_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream_video);

        Intent i = getIntent();
        event_name = i.getStringExtra("event_name");
        video_name = i.getStringExtra("video_name");
        video_url = video_url + event_name + "/" + video_name;
        videoView = (VideoView) findViewById(R.id.VideoView2);
        progressDialog = new ProgressDialog(StreamVideo.this);
        progressDialog.setTitle("streaming");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        MediaController mediaController = new MediaController(StreamVideo.this);
        mediaController.setAnchorView(videoView);
        Uri uri = Uri.parse(video_url);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        progressDialog.dismiss();
        videoView.start();
    }
}
