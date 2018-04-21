package fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.denver.recorder_ui.R;
import com.google.android.gms.plus.PlusOneButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class CameraTestFrag extends Fragment {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView temp_image_view;
    Button temp_button;
    File image_directory;
    String file_path;
    public CameraTestFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View camera_frag_view = inflater.inflate(R.layout.fragment_camera_test, container, false);
        temp_image_view = camera_frag_view.findViewById(R.id.picture_holder);
        image_directory = getActivity().getDir("images", Context.MODE_PRIVATE);
        File test = new File(image_directory.toString() + "/Test.jpg");
        if(test.exists()) {
            Bitmap bp = BitmapFactory.decodeFile(test.getAbsolutePath());
            temp_image_view.setImageBitmap(bp);
        }
        temp_button = camera_frag_view.findViewById(R.id.camera_button);
        temp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        image_directory = getActivity().getDir("images", Context.MODE_PRIVATE);
        Toast.makeText(getActivity(), image_directory.toString(), Toast.LENGTH_SHORT).show();
        return camera_frag_view;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
           try {
               File image = new File(image_directory, "Test.jpg");
               Toast.makeText(getActivity(), image.getAbsolutePath(), Toast.LENGTH_SHORT).show();
               Bitmap bp = (Bitmap) data.getExtras().get("data");
               FileOutputStream out = new FileOutputStream(image);
               bp.compress(Bitmap.CompressFormat.JPEG, 90, out);
               out.flush();
               out.close();
               temp_image_view.setImageBitmap(bp);
           }
           catch (Exception e){
               Toast.makeText(getActivity(),"SOMETHING FUCKED UP", Toast.LENGTH_SHORT).show();
           }
        }
    }

}
