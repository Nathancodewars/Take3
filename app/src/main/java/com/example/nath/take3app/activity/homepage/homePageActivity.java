package com.example.nath.take3app.activity.homepage;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.nath.take3app.R;
import com.example.nath.take3app.activity.utility.bottomNavigationViewHelper;
import com.example.nath.take3app.activity.utility.topRightMenuHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class homePageActivity extends AppCompatActivity {

    private static final String TAG ="homePageActivity";
    private Context mContext = homePageActivity.this;
    private static final int activity_num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate: Starting.");


        setupBottomNavigationView();
        setUpToolBar();
    }


    /*
     * BottomNavigationView Setup
     */
    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up bottom navigation.");

        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);

        bottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        bottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);

        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(activity_num);
        menuItem.setChecked(true);

        setUpToolBar();
    }


    /*
    * Setup Top right nav
     */

    private void setUpToolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.topTabs);
        setSupportActionBar(toolbar);

        topRightMenuHelper.enableToolBar(mContext, toolbar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.top_right_menu, menu);
        return true;
    }

}
