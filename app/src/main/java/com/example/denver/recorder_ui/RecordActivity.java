package com.example.denver.recorder_ui;

import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

import fragments.DetailFragment;
import fragments.ListFragment;

public class RecordActivity extends AppCompatActivity {

    private static final String LOG_TAG = "RecordActivity";

    ImageView record_button;

    MediaRecorder recorder = null;

    //states
    boolean recording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_record);
        record_button = findViewById(R.id.start_recording_button);
        record_button.setOnClickListener(record_listener);

    }
    private void onRecord(boolean start) {
        if (start)
            startRecording();
        else
            stopRecording();
    }
    private View.OnClickListener record_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!recording) {
                record_button.setImageResource(R.drawable.ic_stop_white_48dp);
                recording = !recording;
                startRecording();
            }
            else{
                stopRecording();
                finish();
            }
        }
    };

    private View.OnClickListener stop_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            stopRecording();
            finish();
        }
    };

    private void startRecording() {
        recorder = new MediaRecorder();

        //It might be best to put this whole thing inside a try/catch
        //block? I'm not sure.

        //Sets up the recorder for audio as opposed to video,
        //and a few other things related to file format and encoding.
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

        //I think here is where I need to add the internal storage
        //directory as an argument
        recorder.setOutputFile(DetailFragment.fileName);

        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        //Does some final magic to get the recorder ready.
        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        recorder.start();
    }

    private void stopRecording() {
        //refreshContents();
        recorder.stop();
        recorder.release();
        Toast.makeText(RecordActivity.this, "Saved to: " + DetailFragment.fileName, Toast.LENGTH_SHORT).show();
        recorder = null;
    }
}
