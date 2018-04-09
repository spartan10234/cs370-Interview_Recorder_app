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

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    EditText title_field;
    EditText interviewee_field;
    EditText date_field;

    AppCompatButton skip_button;

    //Strings to hold user inputs
    protected String input_title = null;
    protected String input_interviewee = null;
    protected String input_date = null;

    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View detail_frag_view = inflater.inflate(R.layout.fragment_detail_entry, container, false);
        skip_button = detail_frag_view.findViewById(R.id.xml_skip_details_button);
        skip_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: create intent, switch to record activity, have a button in record
                Intent i = new Intent(getActivity(), RecordActivity.class);
                startActivity(i);
            }
        });



        // Inflate the layout for this fragment
        return detail_frag_view;
    }

}
