package fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.denver.recorder_ui.R;
import com.example.denver.recorder_ui.RecordActivity;
import database.RecordingDatabase;
import database.RecordingEntity;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    EditText title_field;
    EditText first_field;
    EditText last_field;
    EditText date_field;
    EditText desc_field;

    private AppCompatButton skip_button;
    private AppCompatButton enter_button;
    private AppCompatButton cancel_button;

    //Database
    //Database
    RecordingDatabase RD;

    //Strings to hold user inputs
    protected String input_title = "Untitled";
    protected String input_first_name = "Unnamed";
    protected String input_last_name = "Unnamed";
    protected String input_date = "Undated";
    protected String input_desc = "Blank";
    public static String new_file_name = null;



    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View detail_frag_view = inflater.inflate(R.layout.fragment_detail_entry, container, false);
        title_field = detail_frag_view.findViewById(R.id.edit_title);
        first_field = detail_frag_view.findViewById(R.id.edit_first_name);
        last_field = detail_frag_view.findViewById(R.id.edit_last_name);
        date_field = detail_frag_view.findViewById(R.id.edit_date);
        desc_field = detail_frag_view.findViewById(R.id.edit_description);
        final Intent i = new Intent(getActivity(), RecordActivity.class);

        enter_button = detail_frag_view.findViewById(R.id.enter_details_button);
        enter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(getData()){
                   i.putExtra("FILE_NAME", new_file_name);
                   startActivityForResult(i, 2);
               }



            }
        });

        skip_button = detail_frag_view.findViewById(R.id.skip_details_button);
        skip_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Figure out way to generate unique filenames
                new_file_name = ListFragment.directory.toString() + '/' + input_title + ".3gp";
                startActivityForResult(i, 2);

            }
        });

        //Goes on onPause while in other activity



        // Inflate the layout for this fragment
        return detail_frag_view;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            create_entry();
            title_field.getText().clear();
            first_field.getText().clear();
            last_field.getText().clear();
            date_field.getText().clear();
            desc_field.getText().clear();
        }
    }

    boolean getData(){
        input_title = title_field.getText().toString();
        //If title is unique, proceed. Else display error message and wait for a unique
        //title
        input_first_name = first_field.getText().toString();
        input_last_name = last_field.getText().toString();
        input_date = date_field.getText().toString();
        input_desc = desc_field.getText().toString();
        new_file_name = ListFragment.directory.toString() + '/' + input_title + ".3gp";
        return true;
    }
    void create_entry(){
        RD = RecordingDatabase.getRecordingDatabase(getContext());
        final RecordingEntity newR = new RecordingEntity();
        newR.setFirstName(input_first_name);
        newR.setLastName(input_last_name);
        newR.setLength("0:00");
        newR.setDate(input_date);
        newR.setDescription(input_desc);
        newR.setImgFile("loss.jpg");
        newR.setTitle(input_title);
        newR.setAudioFile(new_file_name);
        RD.RecordingDao().insert(newR);
    }

}
