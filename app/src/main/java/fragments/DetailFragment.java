package fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.denver.recorder_ui.R;
import com.example.denver.recorder_ui.RecordActivity;

import java.io.File;
import java.util.List;

import database.RecordingDatabase;
import database.RecordingEntity;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    EditText title_field;
    EditText first_field;
    EditText last_field;
    EditText date_field;
    EditText desc_field;
    ImageView photo_holder;

    private AppCompatButton enter_button;
    private AppCompatButton photo_button;

    //Database
    RecordingDatabase RD;
    List<RecordingEntity> list = null;
    RecordingEntity item;

    //For Image File
    File image_directory;
    private Uri imageUri;
    private String image_file_path = "";


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

        RD = RecordingDatabase.getRecordingDatabase(getContext());
        image_directory = getActivity().getDir("images", Context.MODE_PRIVATE);


        title_field = detail_frag_view.findViewById(R.id.edit_title);
        first_field = detail_frag_view.findViewById(R.id.edit_first_name);
        last_field = detail_frag_view.findViewById(R.id.edit_last_name);
        date_field = detail_frag_view.findViewById(R.id.edit_date);
        desc_field = detail_frag_view.findViewById(R.id.edit_description);
        photo_holder = detail_frag_view.findViewById(R.id.photo_view);
        final Intent i = new Intent(getActivity(), RecordActivity.class);

        enter_button = detail_frag_view.findViewById(R.id.enter_details_button);
        enter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getData()) {
                    i.putExtra("FILE_NAME", new_file_name);
                    startActivityForResult(i, 2);
                }


            }
        });
        photo_button = detail_frag_view.findViewById(R.id.multiuse_button);
        photo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        return detail_frag_view;
    }

    private void dispatchTakePictureIntent() {
        if(!title_field.getText().toString().isEmpty()) {
            input_title = title_field.getText().toString();
            title_field.setEnabled(false);
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            String filename = input_title + ".jpg";
            File photo = new File(image_directory, filename);
            image_file_path = photo.getAbsolutePath();
            imageUri = Uri.parse(photo.getAbsolutePath());
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
        else
            Toast.makeText(getActivity(), "Please Provide a Title for Recording First", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if(resultCode == Activity.RESULT_OK){

                    try {
//                        photo_holder.setImageURI(imageUri);
//                        photo_holder.setVisibility(View.VISIBLE);

                       File test = new File(image_file_path);
                        if(test.exists()) {
                            Bitmap bp = BitmapFactory.decodeFile(test.getAbsolutePath());
                            photo_holder.setImageBitmap(bp);
                        }
                        else
                            Toast.makeText(getActivity(), "NO FILE FOUND", Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e){
                        Toast.makeText(getActivity(),"SOMETHING FUCKED UP", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case 2:
                if(resultCode == Activity.RESULT_OK){
                    create_entry();
                    title_field.getText().clear();
                    first_field.getText().clear();
                    last_field.getText().clear();
                    date_field.getText().clear();
                    desc_field.getText().clear();
                }
                else
                    Toast.makeText(getActivity(), "File Creation Failed", Toast.LENGTH_SHORT).show();
           default:
               Toast.makeText(getActivity(), "Unexpected Error", Toast.LENGTH_SHORT).show();

        }

        }

        boolean getData () {
            if (!title_field.getText().toString().isEmpty()) {
                input_title = title_field.getText().toString();
                //Check title uniqueness
                list = RD.RecordingDao().searchTitle(input_title);
                if (list.size() == 0) {
                    Toast.makeText(getActivity(), "Please Enter Unique Name", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            if (!first_field.getText().toString().isEmpty()) {
                input_first_name = first_field.getText().toString();
            }
            if (last_field.getText().toString().isEmpty()) {
                input_last_name = last_field.getText().toString();
            }
            if (date_field.getText().toString().isEmpty()) {
                input_date = date_field.getText().toString();
            }
            if (desc_field.getText().toString().isEmpty()) {
                input_desc = desc_field.getText().toString();
            }

            new_file_name = ListFragment.directory.toString() + '/' + input_title + ".3gp";
            return true;
        }
        void create_entry () {
            //  RD = RecordingDatabase.getRecordingDatabase(getContext());
            item = new RecordingEntity();
            item.setFirstName(input_first_name);
            item.setLastName(input_last_name);
            item.setLength("0:00");
            item.setDate(input_date);
            item.setDescription(input_desc);
            item.setImgFile(image_file_path);
            item.setTitle(input_title);
            item.setAudioFile(new_file_name);
            RD.RecordingDao().insert(item);
        }

    }
