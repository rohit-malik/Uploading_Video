package com.example.nitinmalik.uploading_video;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        final EditText editText = findViewById(R.id.IP);
        Button button = findViewById(R.id.set_ip);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = editText.getText().toString().trim();
                Intent intent = new Intent(SettingsActivity.this,MainActivity.class);
                intent.putExtra("ip_address",ip);
                startActivity(intent);
            }
        });
    }

}
