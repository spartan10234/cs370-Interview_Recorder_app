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
    protected static RecordingEntityAdapter adapter = null;

    //For accessing the database
    protected static List<RecordingEntity> list;
    protected RecordingDatabase RD;

    //Element declarations
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

        RD = RecordingDatabase.getRecordingDatabase(getContext());

        //CHANGE THIS FOR SEARCH
        list = RD.RecordingDao().getAllRecordings();

        adapter = new RecordingEntityAdapter(list, R.id.list_item, new RecordingEntityAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(RecordingEntity item) {
                file_name = item.getAudioFile();

                //Toast.makeText(getActivity(), "Selected Item: " + adapter.focusedItem, Toast.LENGTH_SHORT).show();
                openItemDetails(item);
            }
        });

        //Toast.makeText(getActivity(), "Items in Database: " + adapter.getItemCount(),Toast.LENGTH_SHORT).show();

        //Hook up our recyclerView to its layout
        recyclerView = list_frag_view.findViewById(R.id.recording_container);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        return list_frag_view;
    }

    @Override
    public void onStart() {
        super.onStart();


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
