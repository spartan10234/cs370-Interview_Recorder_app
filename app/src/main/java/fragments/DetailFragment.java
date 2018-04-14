package fragments;


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
import com.example.denver.recorder_ui.recording;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    EditText title_field;
    EditText first_field;
    EditText last_field;
    EditText date_field;
    EditText desc_field;

    AppCompatButton skip_button;
    AppCompatButton enter_button;

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

        enter_button = detail_frag_view.findViewById(R.id.xml_enter_details_button);
        enter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_title = title_field.getText().toString();
                //If title is unique, proceed. Else display error message and wait for a unique
                //title
                input_first_name = first_field.getText().toString();
                input_last_name = last_field.getText().toString();
                input_date = date_field.getText().toString();
                input_desc = desc_field.getText().toString();
                new_file_name = ListFragment.directory.toString() + '/' + input_title + ".3gp";
                title_field.getText().clear();
                first_field.getText().clear();
                last_field.getText().clear();
                date_field.getText().clear();
                desc_field.getText().clear();
                //TODO setter<-constructor

                ListFragment.listOfRecordings.add(new recording(new_file_name, input_title, input_first_name, input_first_name));
                startActivity(i);

            }
        });

        skip_button = detail_frag_view.findViewById(R.id.xml_skip_details_button);
        skip_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Figure out way to generate unique filenames
                new_file_name = ListFragment.directory.toString() + '/' + input_title + ".3gp";
                ListFragment.listOfRecordings.add(new recording(new_file_name, input_title));
                startActivity(i);
                //Comes back here
            }
        });
        //Goes on onPause while in other activity



        // Inflate the layout for this fragment
        return detail_frag_view;
    }

}
