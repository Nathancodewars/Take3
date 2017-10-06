package com.example.nath.take3app.activity.menuFiles;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;

import com.example.nath.take3app.R;
import com.example.nath.take3app.activity.utility.topRightMenuHelper;

public class settingsActivity extends AppCompatActivity {

    private static final String TAG ="settingsActivity";
    private Context mContext = settingsActivity.this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: Starting.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
