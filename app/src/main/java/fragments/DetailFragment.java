package fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.denver.recorder_ui.MainActivity;
import com.example.denver.recorder_ui.R;
import com.example.denver.recorder_ui.RecordActivity;
import com.example.denver.recorder_ui.recording;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    EditText title_field;
    EditText interviewee_field;
    EditText date_field;

    AppCompatButton skip_button;
    AppCompatButton enter_button;

    //Strings to hold user inputs
    public static String input_title = "Untitled";
    protected String input_interviewee = "Unnamed";
    protected String input_date = "Undated";
    public static String fileName = null;



    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View detail_frag_view = inflater.inflate(R.layout.fragment_detail_entry, container, false);
        title_field = detail_frag_view.findViewById(R.id.edit_title);
        interviewee_field=detail_frag_view.findViewById(R.id.edit_interviewee);
        date_field = detail_frag_view.findViewById(R.id.edit_date);
        final Intent i = new Intent(getActivity(), RecordActivity.class);

        enter_button = detail_frag_view.findViewById(R.id.xml_enter_details_button);
        enter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_title = title_field.getText().toString();
                //If title is unique, proceed. Else display error message and wait for a unique
                //title
                input_interviewee = interviewee_field.getText().toString();
                input_date = date_field.getText().toString();
                fileName = ListFragment.directory.toString() + '/' + input_title + ".3gp";
                title_field.getText().clear();
                interviewee_field.getText().clear();
                date_field.getText().clear();
                ListFragment.listOfRecordings.add(new recording(fileName, input_title, input_interviewee, input_interviewee));
                startActivity(i);

            }
        });

        skip_button = detail_frag_view.findViewById(R.id.xml_skip_details_button);
        skip_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Figure out way to generate unique filenames
                fileName = ListFragment.directory.toString() + '/' + input_title + ".3gp";
                ListFragment.listOfRecordings.add(new recording(fileName, input_title));
                startActivity(i);
            }
        });



        // Inflate the layout for this fragment
        return detail_frag_view;
    }

}
