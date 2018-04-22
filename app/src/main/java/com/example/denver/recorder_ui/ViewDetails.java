package com.example.denver.recorder_ui;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import database.RecordingDatabase;
import database.RecordingEntity;

public class ViewDetails extends AppCompatActivity {

    private static final String LOG_TAG = "ViewDetails";
    private EditText title_field, first_name_field, last_name_field, date_field, desc_field;
    ImageView photo_field;
    private AppCompatButton play_button, cancel_button;
    private RecordingDatabase RD;
    private String audio_file_name;
    private String image_file_name;
    private MediaPlayer player = null;
    protected static List<RecordingEntity> list;
    private boolean isPlaying = true;



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
        list = RD.RecordingDao().searchId(id);
        RecordingEntity item = list.get(0);
        //Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();

        title_field = findViewById(R.id.edit_title);
        first_name_field = findViewById(R.id.edit_first_name);
        last_name_field = findViewById(R.id.edit_last_name);
        date_field = findViewById(R.id.edit_date);
        desc_field = findViewById(R.id.edit_description);
      //  photo_field = findViewById(R.id.photo_view);
        setFields(item);

        play_button = findViewById(R.id.multiuse_button);
        play_button.setVisibility(View.VISIBLE);
        play_button.setText(R.string.string_start_playback);
        play_button.setOnClickListener(play_listener);


        cancel_button = findViewById(R.id.enter_details_button);
        cancel_button.setText(R.string.string_cancel);
        cancel_button.setOnClickListener(new View.OnClickListener() {
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
        audio_file_name = item.getAudioFile();
        //image_file_name = item.getImgFile();
       // photo_field.setImageURI(Uri.parse(image_file_name));
    }

    View.OnClickListener play_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onPlay(isPlaying);
            if (isPlaying) {
                play_button.setText("Stop Playing");
                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        play_button.setText("Start Playing");
                        stopPlaying();
                        isPlaying = !isPlaying;

                    }
                });
            } else {
                play_button.setText("Start Playing");
            }
            isPlaying = !isPlaying;
        }
    };

    private void onPlay(boolean start) {
        if (start)
            startPlaying();
        else
            stopPlaying();
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(audio_file_name);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Couldn't load media file: " + audio_file_name);
        }
    }

    //TODO: add a pause button for playback

    //Release resources when finished to get memory back
    private void stopPlaying() {
        player.release();
        player = null;
    }

}
