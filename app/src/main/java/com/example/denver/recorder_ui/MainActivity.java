package com.example.denver.recorder_ui;

import android.content.pm.PackageManager;
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
import fragments.ListFragment;
import fragments.DetailFragment;
import fragments.SearchFragment;


public class MainActivity extends AppCompatActivity {

    protected static String test = "DID THIS FUCKING WORK?";
    private TextView toolBarText;
    private Toolbar myToolbar;

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

                    toolBarText.setText(R.string.title_home);
                    switchToListFragment();
                    return true;
                case R.id.navigation_record:

                    toolBarText.setText(R.string.title_record);
                    switchToDetailFragment();
                    return true;
                case R.id.navigation_search:

                    toolBarText.setText(R.string.title_search);
                    switchToSearchFragment();
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

        //Create Top Bar TextView
        toolBarText = (TextView) findViewById(R.id.tool_text);

        //Create Bottom Navigation Menu
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        //Create Navigation Bar Item Select Listener
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Create Toolbar
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //Select the active Action Bar
        setSupportActionBar(myToolbar);
        //Remove the app name from the action bar.
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        //Initialize the home fragment
        Fragment newFragment = new ListFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,newFragment);
        transaction.addToBackStack(null);
        transaction.commit();





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

//    public void switchToRecordFragment(){
//        Fragment newFragment = new DetailFragment();
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.fragment_container,newFragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }

    public void switchToSearchFragment(){
        Fragment newFragment = new SearchFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


}
