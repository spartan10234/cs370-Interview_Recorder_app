package fragments;


import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.example.denver.recorder_ui.MainActivity;
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

    private static final String LOG_TAG = "ListFragment";

    //For populating the list of recordings
    protected static ArrayList<recording> listOfRecordings = new ArrayList<recording>();
    protected static recordingAdapter adapter = null;

    //Element declarations
    AppCompatButton play_button;
    ListView listView;
    EditText recording_name_field;

    //For accessing and storing audio files
    public static File directory = null;
    public static File Recordings_Contents[] = null;

    //For accessing the database
   // private RecordingDbHelper dbHelper;

    //For
    public static String new_file_name = null;
    public static String user_input = null;

    //States to keep track of which buttons should be enabled, to prevent
    //stuff like playing and recording at the same time.
    boolean readyToRecord = false;
    boolean startPlaying = true;
    boolean startRecording = true;
    boolean paused = true;
    boolean file_name_entered = false;

    //The bits the do the actual playing and recording
    //private MediaRecorder recorder = null;
    private MediaPlayer player = null;



    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Get the name of our directory and load all the files currently stored in it
        directory = getActivity().getDir("Recordings", Context.MODE_PRIVATE);
        Recordings_Contents = directory.listFiles();

//        //From audio files, create recording objects and load into arraylist, which will
//        //then be shown in the listview.
        listOfRecordings.clear();
        for (int i = 0; i < Recordings_Contents.length; i++) {
            listOfRecordings.add(new recording(Recordings_Contents[i].toString()));
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View list_frag_view = inflater.inflate(R.layout.fragment_list, container, false);;
        play_button = list_frag_view.findViewById(R.id.xml_play_button);

        listView = list_frag_view.findViewById(R.id.recording_container);


       // enter_button.setOnClickListener(enter_listener);
        play_button.setOnClickListener(play_listener);
       // record_button.setOnClickListener(record_listener);

        adapter = new recordingAdapter(getActivity(), listOfRecordings);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(list_item_listener);

        // Inflate the layout for this fragment
        return list_frag_view;
    }

    @Override
    public void onStart() {
        super.onStart();


   }

    View.OnClickListener play_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onPlay(startPlaying);
            if (startPlaying) {
                play_button.setText("Stop Playing");
                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        play_button.setText("Start Playing");
                        stopPlaying();
                        startPlaying = !startPlaying;

                    }
                });
            } else {
                play_button.setText("Start Playing");
            }
            startPlaying = !startPlaying;
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

    public static void refreshContents() {
        Recordings_Contents = directory.listFiles();
        adapter.notifyDataSetChanged();
    }
}
