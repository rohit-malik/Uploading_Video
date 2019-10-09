package com.example.nitinmalik.uploading_video;

import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoListActivity extends AppCompatActivity {

    //this is the JSON Data URL
    //make sure you are using the correct ip else it will not work
    private static final String URL_VIDEOS = "http://172.26.1.221/AndroidUploadImage/videolist.php?event_ID=%1$s";
    //int num1 = 1;
    //private final String URL_VIDEOS = String.format("http://172.26.1.221/AndroidUploadImage/videolist.php?event_ID=%1$s",
    //num1);
    //a list to store all the products
    List<Video> videoList;
    String event_name;
    //the recyclerview
    RecyclerView recyclerViewVideo;
    LinearLayoutManager linearLayoutManager;
    String uri;
    String videopath;
    String date;
    String url = "http://172.26.1.221/AndroidUploadImage/upload.php";
    int event_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        recyclerViewVideo = (RecyclerView) findViewById(R.id.recyclerViewVideo);

        Intent i = getIntent();
        event_ID= i.getIntExtra("event_ID", 1);
        uri = String.format(URL_VIDEOS, event_ID);

        event_name = i.getStringExtra("event_name");

        Button select_button = findViewById(R.id.button_upload_video);

        // set a LinearLayoutManager with default vertical orientation
        {
            linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        }
        recyclerViewVideo.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewVideo.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerViewVideo.addItemDecoration(dividerItemDecoration);
        recyclerViewVideo.setHasFixedSize(true);
        videoList = new ArrayList<>();
        Log.d("ONCREATE","IN ONCREATE");
        loadVideos();

        select_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_gallery();

            }
        });

        Button mashup_button = findViewById(R.id.button_mashup);

        mashup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoListActivity.this, StreamVideo.class);
                intent.putExtra("event_name", event_name + "/mashup");
                intent.putExtra("video_name", "mashup.mp4");
                startActivity(intent);
            }
        });
    }


    private void loadVideos() {

        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.getCache().clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);
                            Log.d("ARRAY_LENGHT",String.valueOf(array.length()));
                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject video = array.getJSONObject(i);
                                Log.d("DATA",String.valueOf(video.getInt("video_ID")));
                                //adding the product to product list
                                videoList.add(new Video(
                                        video.getInt("video_ID"),
                                        video.getString("video_name"),
                                        video.getString("video_desc")
                                ));
                            }

                            //creating adapter object and setting it to recyclerview
                            VideoAdapter adapter = new VideoAdapter(VideoListActivity.this, videoList, event_name);
                            recyclerViewVideo.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //adding our stringrequest to queue
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }

    private void open_gallery(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        //Intent intent = new Intent();
        //intent.setType("video/*");
        //intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Video"),3);
    }

    @Override
    protected void onActivityResult(int requestcode, int resultcode, Intent data){
        super.onActivityResult(requestcode,resultcode,data);
        if(resultcode == RESULT_OK){
            if(requestcode == 3){
                Uri selected_video = data.getData();

                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                //videopath = file.getAbsolutePath();
                videopath = getPath(selected_video);
                File file = new File(videopath);
                date = sdf.format(file.lastModified());
                Log.d("File last modified :", "" + date);
                Log.d("Video_Path"," " + videopath);

                Log.d("Final Path","" + videopath);
                if(videopath!= null){

                    upload_video(videopath);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Video not selected!", Toast.LENGTH_LONG).show();
                }

            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Video.Media.DATA };
        CursorLoader loader = new CursorLoader(getApplicationContext(), uri, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        } else {
            return null;
        }
    }

    private void upload_video(String path){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading Video...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgress(0);
        progressDialog.show();


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    Log.d("Json",response);
                    JSONObject jObj = new JSONObject(response);
                    String message = jObj.getString("message");

                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        Log.d("path of file",path);
        simpleMultiPartRequest.addFile("image",path);
        simpleMultiPartRequest.addStringParam("event_name", event_name);

        simpleMultiPartRequest.setOnProgressListener(new Response.ProgressListener() {
            @Override
            public void onProgress(long transferredBytes, long totalSize) {
                int percentage = (int) ((transferredBytes / ((float) totalSize)) * 100);
                progressDialog.setProgress(percentage);
            }
        });


        com.android.volley.request.StringRequest stringrequest = new com.android.volley.request.StringRequest(Request.Method.POST,"http://172.26.1.221/AndroidUploadImage/date_modified.php",new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                Log.d("modified date",response);
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){

            }
        }){
            @Override
            protected java.util.Map<String, String> getParams() {
                java.util.Map<String, String> params = new java.util.HashMap<>();
                params.put("date", date);
                params.put("name", videopath);
                params.put("event_id", String.valueOf(event_ID));
                params.put("event_name", event_name);
                return params;
            }
        };

        requestQueue.add(simpleMultiPartRequest);
        requestQueue.add(stringrequest);

    }

}
