package com.example.denver.recorder_ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import fragments.CameraTestFrag;
import fragments.ListFragment;
import fragments.DetailFragment;
import fragments.SearchFragment;


public class MainActivity extends AppCompatActivity {

    Fragment dFrag = new DetailFragment();
    public static MediaRecorder recorder = null;

    boolean editable = true;

// Permission Request Variables
    protected static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    protected boolean permissionToRecordAccepted = false;
    protected String[] permissions = {android.Manifest.permission.RECORD_AUDIO};

//    This is the control for the bottom navigation, uses the switch statement to determine what to do
//    when any of the buttons are selected.
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    //toolBarText.setText(R.string.title_home);
                    switchToListFragment();
                    return true;
                case R.id.navigation_record:

                    //toolBarText.setText(R.string.title_record);
                    switchToFragment(dFrag);
                    return true;

            }
            return false;
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted)
            finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        setContentView(R.layout.activity_main);

        //Create Bottom Navigation Menu
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        //Create Navigation Bar Item Select Listener
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        //Initialize the home fragment
        switchToListFragment();





    }

    public void switchToListFragment(){
        Fragment newFragment = new ListFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void switchToDetailFragment(){
        Fragment newFragment = new DetailFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

   public void switchToCameraTestFragment(){
        Fragment newFragment = new CameraTestFrag();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void switchToFragment(Fragment frag){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,frag);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


}
