package com.example.nath.take3app.activity.utility;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import com.example.nath.take3app.R;
import com.example.nath.take3app.activity.menuFiles.donateActivity;
import com.example.nath.take3app.activity.homepage.homePageActivity;
import com.example.nath.take3app.activity.menuFiles.settingsActivity;

public class topRightMenuHelper {
    private static final String TAG ="topRightMenuHelper";


    public static void enableToolBar(final Context context, Toolbar toolbar){

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem item){
                Log.d(TAG, "OnMenuItemClick: clicked menu item " + item);
                switch(item.getItemId()){
                    case R.id.action_donate:
                        Log.d(TAG, "case action donate:inside " + item);
                        Intent intent1 = new Intent(context, donateActivity.class);
                        context.startActivity(intent1);
                        break;


                    case R.id.action_Logout:
                        Intent intent4 = new Intent(context, homePageActivity.class);
                        context.startActivity(intent4);
                        break;
                }
                return false;
            }


        });


    }

}
