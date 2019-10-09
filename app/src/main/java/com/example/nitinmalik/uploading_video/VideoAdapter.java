package com.example.nitinmalik.uploading_video;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter {

    private List<Video> videoList;
    private Context context;
    private String event_name;

    public VideoAdapter(Context context, List<Video> videoList, String event_name) {
        this.context = context;
        this.videoList = videoList;
        this.event_name = event_name;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_video, parent, false);
        // set the view's size, margins, paddings and layout parameters
        VideoAdapter.MyViewHolder vh = new VideoAdapter.MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder1,final int position) {
        VideoAdapter.MyViewHolder holder = (VideoAdapter.MyViewHolder) holder1;
        final Video video = videoList.get(position);
        holder.ViewVideo_ID.setText("Video ID: " + String.valueOf(video.getVideo_ID()));
        holder.ViewVideo_name.setText("Event Name: " + video.getVideo_name());
        holder.ViewVideo_desc.setText(video.getVideo_desc());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, videoList.get(position).getVideo_name(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, StreamVideo.class);
                intent.putExtra("video_name", video.getVideo_name());
                intent.putExtra("event_name", event_name);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView ViewVideo_ID, ViewVideo_name, ViewVideo_desc;// init the item view's
        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            ViewVideo_ID = (TextView) itemView.findViewById(R.id.video_ID);
            ViewVideo_name = (TextView) itemView.findViewById(R.id.video_name);
            ViewVideo_desc = (TextView) itemView.findViewById(R.id.video_desc);
        }
    }
}
