package com.example.denver.recorder_ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

import database.RecordingDatabase;
import database.RecordingEntity;

public class ViewDetails extends AppCompatActivity {
    private EditText title_field, first_name_field, last_name_field, date_field, desc_field;
    private AppCompatButton skip_button, enter_button, temp_button;
    RecordingDatabase RD;
    protected static List<RecordingEntity> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_detail_entry);

        int id = getIntent().getIntExtra("INPUT_RECORDING_ID", -1);
        if(id == -1){
            Toast.makeText(this, "FAILED SEARCH", Toast.LENGTH_SHORT).show();
            finish();
        }

        RD = RecordingDatabase.getRecordingDatabase(this);
        List<RecordingEntity> list = RD.RecordingDao().searchId(id);
        RecordingEntity item = list.get(0);
        Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();

        title_field = findViewById(R.id.edit_title);
        first_name_field = findViewById(R.id.edit_first_name);
        last_name_field = findViewById(R.id.edit_last_name);
        date_field = findViewById(R.id.edit_date);
        desc_field = findViewById(R.id.edit_description);
        skip_button = findViewById(R.id.skip_details_button);
        setFields(item);
        temp_button = findViewById(R.id.enter_details_button);
        temp_button.setText(R.string.string_cancel);
        temp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    void setFields(RecordingEntity item){
        title_field.setText(item.getTitle());
        title_field.setKeyListener(null);
        first_name_field.setText(item.getFirstName());
        first_name_field.setKeyListener(null);
        last_name_field.setText(item.getLastName());
        last_name_field.setKeyListener(null);
        date_field.setText(item.getDate());
        date_field.setKeyListener(null);
        desc_field.setText(item.getDescription());
        desc_field.setKeyListener(null);
        skip_button.setEnabled(false);
        skip_button.setVisibility(View.INVISIBLE);
    }
}
