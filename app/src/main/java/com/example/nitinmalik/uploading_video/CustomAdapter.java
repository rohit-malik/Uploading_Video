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

public class CustomAdapter extends RecyclerView.Adapter {

    private List<VideoEvent> eventList;
    private Context context;

    public CustomAdapter(Context context, List<VideoEvent> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder1,final int position) {
        MyViewHolder holder = (MyViewHolder) holder1;
        final VideoEvent event = eventList.get(position);
        holder.ViewEvent_ID.setText("Event ID: " + String.valueOf(event.getEvent_ID()));
        holder.ViewEvent_name.setText("Event Name: " + event.getEvent_name());
        holder.ViewEvent_info.setText(event.getEvent_info());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, eventList.get(position).getEvent_name(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, VideoListActivity.class);
                intent.putExtra("event_ID", event.getEvent_ID());
                intent.putExtra("event_name", event.getEvent_name());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView ViewEvent_ID, ViewEvent_name, ViewEvent_info;// init the item view's
        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            ViewEvent_ID = (TextView) itemView.findViewById(R.id.event_ID);
            ViewEvent_name = (TextView) itemView.findViewById(R.id.event_name);
            ViewEvent_info = (TextView) itemView.findViewById(R.id.event_info);
        }
    }
}
