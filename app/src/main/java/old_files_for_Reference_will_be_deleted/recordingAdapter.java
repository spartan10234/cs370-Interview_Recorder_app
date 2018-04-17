package old_files_for_Reference_will_be_deleted;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.denver.recorder_ui.R;

import java.io.File;
import java.util.ArrayList;

public class recordingAdapter extends ArrayAdapter<recording> {

    protected ListView listView;

    public recordingAdapter(Activity context, ArrayList<recording> recordingList){
        super(context, 0, recordingList);
    }

    public View getView(int position, View convertView, ViewGroup parent){

        View listItemView = convertView;

        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);

        }

        final recording currentRecording = getItem(position);

        TextView fileNameTextView = listItemView.findViewById(R.id.list_item_title);
        fileNameTextView.setText(currentRecording.getRecordingTitle());

        TextView detailsTextView = listItemView.findViewById(R.id.list_item_details_button);
        detailsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Switch to the detail view activity with details drawn from the recording object
                Toast.makeText(getContext(), currentRecording.getInterviewee() + ' ' + currentRecording.getDate(), Toast.LENGTH_SHORT).show();
            }
        });

        TextView deleteTextView = listItemView.findViewById(R.id.list_item_delete_button);
        deleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileToDelete = currentRecording.getFullFileName();
                File file = new File(fileToDelete);
                boolean deleted = file.delete();
                remove(currentRecording);
            }
        });

        return listItemView;
    }

}
