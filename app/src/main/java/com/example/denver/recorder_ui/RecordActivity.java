package com.example.denver.recorder_ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class RecordActivity extends AppCompatActivity {

    ImageView button;
    ImageView otherbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_record);
        button = findViewById(R.id.start_recording_button);
        otherbutton = findViewById(R.id.stop_recording_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RecordActivity.this, MainActivity.test, Toast.LENGTH_SHORT).show();
            }
        });
        otherbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
