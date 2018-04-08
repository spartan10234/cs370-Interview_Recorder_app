package fragments;


import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.denver.recorder_ui.R;
import com.example.denver.recorder_ui.recording;
import com.example.denver.recorder_ui.recordingAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {

    private static final String LOG_TAG = "Recorder";

    //For populating the list of recordings
    protected ArrayList<recording> listOfRecordings = new ArrayList<recording>();
    protected static recordingAdapter adapter = null;

    //Element declarations
    AppCompatButton play_button;
    AppCompatButton record_button;
    AppCompatButton new_button;
    Button enter_button;
    ListView listView;
    EditText recording_name_field;

    //For accessing and storing audio files
    public static File directory = null;
    public static File Recordings_Contents[] = null;

    //For accessing the database
   // private RecordingDbHelper dbHelper;

    //For
    public String new_file_name = null;
    public String user_input = null;

    //States to keep track of which buttons should be enabled, to prevent
    //stuff like playing and recording at the same time.
    boolean readyToRecord = false;
    boolean startPlaying = true;
    boolean startRecording = true;
    boolean paused = true;
    boolean file_name_entered = false;

    //The bits the do the actual playing and recording
    private MediaRecorder recorder = null;
    private MediaPlayer player = null;



    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View list_frag_view = inflater.inflate(R.layout.fragment_list, container, false);;
        play_button = list_frag_view.findViewById(R.id.xml_play_button);
        record_button = list_frag_view.findViewById(R.id.xml_record_button);
        new_button = list_frag_view.findViewById(R.id.xml_new_button);
        enter_button = list_frag_view.findViewById(R.id.xml_enter_button);
        listView = list_frag_view.findViewById(R.id.recording_container);
        recording_name_field = list_frag_view.findViewById(R.id.recordingNameField);


        //Get the name of our directory and load all the files currently stored in it
        directory = getActivity().getDir("Recordings", Context.MODE_PRIVATE);
        Recordings_Contents = directory.listFiles();
//        //From audio files, create recording objects and load into arraylist, which will
//        //then be shown in the listview.
        for (int i = 0; i < Recordings_Contents.length; i++) {
            listOfRecordings.add(new recording(Recordings_Contents[i].toString()));
        }

        enter_button.setOnClickListener(enter_listener);
        play_button.setOnClickListener(play_listener);
        record_button.setOnClickListener(record_listener);

        adapter = new recordingAdapter(getActivity(), listOfRecordings);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(list_item_listener);

        // Inflate the layout for this fragment
        return list_frag_view;
    }

    View.OnClickListener play_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onPlay(startPlaying);
            if (startPlaying) {
                play_button.setText("Stop Playing");
                record_button.setEnabled(false);
                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        play_button.setText("Start Playing");
                        stopPlaying();
                        startPlaying = !startPlaying;
                        if (readyToRecord)
                            record_button.setEnabled(true);
                    }
                });
            } else {
                play_button.setText("Start Playing");
                if (readyToRecord)
                    record_button.setEnabled(true);
            }
            startPlaying = !startPlaying;
        }
    };
    View.OnClickListener enter_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            user_input = recording_name_field.getText().toString();
            Toast.makeText(getActivity(), user_input, Toast.LENGTH_SHORT).show();
            new_file_name = directory.toString() + '/' + user_input + ".3gp";
            file_name_entered = true;
            readyToRecord = true;
            record_button.setEnabled(true);
        }
    };
    View.OnClickListener record_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onRecord(startRecording);
            if (startRecording) {
                record_button.setText("Stop Recording");
                play_button.setEnabled(false);
                recording_name_field.setEnabled(false);
            } else {
                record_button.setText("Start Recording");
                recording_name_field.setEnabled(true);
                //SAVE FILE, ADD TO LIST
                listOfRecordings.add(new recording(new_file_name, user_input));
                //adapter.notifyDataSetChanged();
                refreshContents();
                recording_name_field.getText().clear();
                recording_name_field.setEnabled(true);
                readyToRecord = false;
                record_button.setEnabled(false);
                new_file_name = null;
            }
            startRecording = !startRecording;
        }
    };
    AdapterView.OnItemClickListener list_item_listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            recording listItem = (recording) listView.getItemAtPosition(position);
            for (int i = 0; i < listView.getChildCount(); i++) {
                if (position == i) {
                    View temp = listView.getChildAt(i);
                    TextView txtView = ((TextView) temp.findViewById(R.id.item_name));
                    txtView.setTextColor(getResources().getColor(R.color.colorAccent));

                } else {
                    View temp = listView.getChildAt(i);
                    TextView txtView = ((TextView) temp.findViewById(R.id.item_name));
                    txtView.setTextColor(Color.BLACK);
                }

            }
            new_file_name = listItem.getFullFileName();
            play_button.setEnabled(true);
        }
    };

    //Record/Play Functions
    private void onPlay(boolean start) {
        if (start)
            startPlaying();
        else
            stopPlaying();
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(new_file_name);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Couldn't load media file: " + new_file_name);
        }
    }

    //TODO: add a pause button for playback

    //Release resources when finished to get memory back
    private void stopPlaying() {
        player.release();
        player = null;
    }
    private void onRecord(boolean start) {
        if (start)
            startRecording();
        else
            stopRecording();
    }

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
        recorder.setOutputFile(new_file_name);

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
        refreshContents();
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    public static void refreshContents() {
        Recordings_Contents = directory.listFiles();
        adapter.notifyDataSetChanged();
    }
}
