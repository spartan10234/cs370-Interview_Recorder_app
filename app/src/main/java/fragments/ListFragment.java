package fragments;


import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.denver.recorder_ui.R;
import com.example.denver.recorder_ui.RecordingEntityAdapter;
import com.example.denver.recorder_ui.recording;
import com.example.denver.recorder_ui.recordingAdapter;

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
    protected static recordingAdapter adapter = null;

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

    //For
    public static String new_file_name = null;
    public static String user_input = null;

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
        play_button = list_frag_view.findViewById(R.id.play_button);
        add_button = list_frag_view.findViewById(R.id.add_data);

        RD = RecordingDatabase.getRecordingDatabase(getContext());

        list = RD.RecordingDao().getAllRecordings();
        final RecordingEntityAdapter adapter = new RecordingEntityAdapter(list, R.id.list_item);
        Toast.makeText(getActivity(), "Items in Database: " + adapter.getItemCount(),Toast.LENGTH_SHORT).show();

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
                Toast.makeText(getActivity(), "Items in Database: " + adapter.getItemCount(),Toast.LENGTH_SHORT).show();
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

//    AdapterView.OnItemClickListener list_item_listener = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            recording listItem = (recording) recyclerView.getItemAtPosition(position);
//            for (int i = 0; i < recyclerView.getChildCount(); i++) {
//                if (position == i) {
//                    View temp = recyclerView.getChildAt(i);
//                    TextView txtView = ((TextView) temp.findViewById(R.id.list_item_title));
//                    txtView.setTextColor(getResources().getColor(R.color.colorAccent));
//
//                } else {
//                    View temp = recyclerView.getChildAt(i);
//                    TextView txtView = ((TextView) temp.findViewById(R.id.list_item_title));
//                    txtView.setTextColor(Color.BLACK);
//                }
//
//            }
//            new_file_name = listItem.getFullFileName();
//            play_button.setEnabled(true);
//        }
//    };

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

    private void createDummyData(){
        RD = RecordingDatabase.getRecordingDatabase(getContext());
        final RecordingEntity newR = new RecordingEntity();
        newR.setFirstName("Denver");
        newR.setLastName("Gregory");
        newR.setLength("01:12");
        newR.setDescription("This is a new test recording");
        newR.setImgFile("img234.jpg");
        newR.setTitle("New Title");
        newR.setAudioFile("aud123.MP3");
        RD.RecordingDao().insert(newR);

    }
}
