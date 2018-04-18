package fragments;


import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.denver.recorder_ui.MainActivity;
import com.example.denver.recorder_ui.R;
import com.example.denver.recorder_ui.ViewDetails;

import database.RecordingEntityAdapter;
import old_files_for_Reference_will_be_deleted.recording;
import old_files_for_Reference_will_be_deleted.recordingAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import database.RecordingDatabase;
import database.RecordingEntity;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {

    private static final String LOG_TAG = "ListFragment";

    //For populating the list of recordings
    protected static ArrayList<recording> listOfRecordings = new ArrayList<recording>();
    protected static RecordingEntityAdapter adapter = null;

    //For accessing the database
    protected static List<RecordingEntity> list;
    protected RecordingDatabase RD;

    //Element declarations
    AppCompatButton play_button;
    Button add_button;
    RecyclerView recyclerView;


    //For accessing and storing audio files
    public static File directory = null;
    public static File Recordings_Contents[] = null;

    String file_name = null;

    //States to keep track of which buttons should be enabled, to prevent
    //stuff like playing and recording at the same time.
    boolean startPlaying = true;


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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View list_frag_view = inflater.inflate(R.layout.fragment_list, container, false);;
        play_button = list_frag_view.findViewById(R.id.play_button);
        add_button = list_frag_view.findViewById(R.id.add_data);

        RD = RecordingDatabase.getRecordingDatabase(getContext());

        //CHANGE THIS FOR SEARCH
        list = RD.RecordingDao().getAllRecordings();

        adapter = new RecordingEntityAdapter(list, R.id.list_item, new RecordingEntityAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(RecordingEntity item) {
                file_name = item.getAudioFile();
                play_button.setEnabled(true);
                //Toast.makeText(getActivity(), "Selected Item: " + adapter.focusedItem, Toast.LENGTH_SHORT).show();
                openItemDetails(item);
            }
        });

        //Toast.makeText(getActivity(), "Items in Database: " + adapter.getItemCount(),Toast.LENGTH_SHORT).show();

        //Hook up our recyclerView to its layout
        recyclerView = list_frag_view.findViewById(R.id.recording_container);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

//        play_button.setOnClickListener(play_listener);
//
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDummyData();
                list = RD.RecordingDao().getAllRecordings();
                adapter.notifyItemInserted(list.size()-1);
            }
        });

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
            player.setDataSource(file_name);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Couldn't load media file: " + file_name);
        }
    }

    //TODO: add a pause button for playback

    //Release resources when finished to get memory back
    private void stopPlaying() {
        player.release();
        player = null;
    }

    private void createDummyData(){
        RD = RecordingDatabase.getRecordingDatabase(getContext());
        final RecordingEntity newR = new RecordingEntity();
        newR.setFirstName("Denver");
        newR.setLastName("Gregory");
        newR.setLength("01:12");
        newR.setDate("01/23/45");
        newR.setDescription("This is a new test recording");
        newR.setImgFile("img234.jpg");
        newR.setTitle("New Title");
        newR.setAudioFile("aud123.MP3");
        RD.RecordingDao().insert(newR);

    }

    void openItemDetails(RecordingEntity item){
        Intent i = new Intent(getActivity(), ViewDetails.class);
        i.putExtra("INPUT_RECORDING_ID", item.getRecordingId());

        //TODO change this to a start activity for result to see if file was edited or not
        startActivity(i);
    }

}
