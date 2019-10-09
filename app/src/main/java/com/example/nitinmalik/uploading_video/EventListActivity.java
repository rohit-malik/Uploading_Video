package com.example.nitinmalik.uploading_video;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EventListActivity extends AppCompatActivity {

    //this is the JSON Data URL
    //make sure you are using the correct ip else it will not work
    private static final String URL_EVENTS = "http://172.26.1.221/AndroidUploadImage/mysql_db.php";

    //a list to store all the products
    List<VideoEvent> eventList;

    //the recyclerview
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        Button add_button = findViewById(R.id.button_add_event);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        // set a LinearLayoutManager with default vertical orientation
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setHasFixedSize(true);
        eventList = new ArrayList<>();
        Log.d("ONCREATE","IN ONCREATE");
        loadEvents();

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventListActivity.this, AddEventActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    /*
    @Override
    protected void onRestart() {
        super.onRestart();
        loadEvents();
    }
    */
    private void loadEvents() {

        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.getCache().clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_EVENTS,
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
                                JSONObject event = array.getJSONObject(i);
                                Log.d("DATA",String.valueOf(event.getInt("event_ID")));
                                //adding the product to product list
                                eventList.add(new VideoEvent(
                                        event.getInt("event_ID"),
                                        event.getString("event_name"),
                                        event.getString("event_info")
                                ));
                            }

                            //creating adapter object and setting it to recyclerview
                            CustomAdapter adapter = new CustomAdapter(EventListActivity.this, eventList);
                            recyclerView.setAdapter(adapter);
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

}
