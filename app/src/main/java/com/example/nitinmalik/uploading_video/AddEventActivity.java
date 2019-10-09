package com.example.nitinmalik.uploading_video;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class AddEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        final EditText event_name = (EditText) findViewById(R.id.EditTextEventName);
        final EditText event_desc = (EditText) findViewById(R.id.EditTextEventDesc);

        Button add_button = (Button) findViewById(R.id.ButtonEvent);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = event_name.getText().toString();
                final String desc = event_desc.getText().toString();
                AddEvent(name, desc);
                Intent intent = new Intent(AddEventActivity.this,EventListActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void AddEvent(final String name, final String description){

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        com.android.volley.request.StringRequest stringrequest = new com.android.volley.request.StringRequest(Request.Method.POST,"http://172.26.1.221/AndroidUploadImage/AddEvent.php",new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                Log.d("Event",response);
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
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){

            }
        }){
            @Override
            protected java.util.Map<String, String> getParams() {
                java.util.Map<String, String> params = new java.util.HashMap<>();
                params.put("event_name", name);
                params.put("event_desc", description);
                return params;
            }
        };

        requestQueue.add(stringrequest);

    }


}
