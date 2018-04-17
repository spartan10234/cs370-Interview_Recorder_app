package com.example.denver.recorder_ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import database.RecordingDatabase;
import database.RecordingEntity;

public class ViewDetails extends AppCompatActivity {
    EditText title_field, first_name_field, last_name_field, date_field, desc_field;
   // AppCompatButton skip_button, enter_button, temp_button;
    RecordingDatabase RD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_detail_entry);

        int id = getIntent().getIntExtra("INPUT_RECORDING_ID", -1);
        if(id == -1){
            Toast.makeText(this, "FAILED SEARCH", Toast.LENGTH_SHORT).show();
            finish();
        }
        Toast.makeText(this, "VIEWDETAILS ID IS: " + id, Toast.LENGTH_SHORT).show();
        RD = RecordingDatabase.getRecordingDatabase(this);
        RecordingEntity item = RD.RecordingDao().searchId(id);

//        title_field = findViewById(R.id.edit_title);
//        title_field.setText(item.getTitle());
//        title_field.setEnabled(false);
//        first_name_field = findViewById(R.id.edit_first_name);
//        first_name_field.setEnabled(false);
//        last_name_field = findViewById(R.id.edit_last_name);
//        last_name_field.setEnabled(false);
//        date_field = findViewById(R.id.edit_date);
//        date_field.setEnabled(false);
//        desc_field = findViewById(R.id.edit_description);
//        desc_field.setEnabled(false);
//        skip_button = findViewById(R.id.skip_details_button);
//        skip_button.setEnabled(false);
//        skip_button.setVisibility(View.INVISIBLE);
//        enter_button.findViewById(R.id.enter_details_button);
//        enter_button.setEnabled(false);
//        enter_button.setVisibility(View.INVISIBLE);
//        temp_button.findViewById(R.id.leave_details_button);
//        temp_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

    }
}
