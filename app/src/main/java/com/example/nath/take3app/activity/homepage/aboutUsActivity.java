package com.example.nath.take3app.activity.homepage;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.nath.take3app.R;
import com.example.nath.take3app.activity.utility.bottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class aboutUsActivity extends AppCompatActivity {

    private static final String TAG ="aboutUsActivity";
    private Context mContext = aboutUsActivity.this;
    private static final int activity_num = 2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupBottomNavigationView();
        Log.d(TAG, "onCreate: starting up.");
    }

    /*
    * BottomNavigationView Setup
     */
    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up bottom navigation.");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        bottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
//        bottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);

        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(activity_num);
        menuItem.setChecked(true);

    }

}
