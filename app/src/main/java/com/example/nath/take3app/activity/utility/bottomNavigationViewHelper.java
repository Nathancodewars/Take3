package com.example.nath.take3app.activity.utility;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.example.nath.take3app.R;
import com.example.nath.take3app.activity.homepage.aboutUsActivity;
import com.example.nath.take3app.activity.homepage.cameraActivity;
import com.example.nath.take3app.activity.homepage.homePageActivity;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class bottomNavigationViewHelper {

    private static final String TAG = "BottomNavigationViewHel";

    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx){
        Log.d(TAG, "setupBottomNavigationVIew: setting up botNavBar");

        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
    }

    public static void enableNavigation (final Context context, BottomNavigationViewEx view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item){
                switch(item.getItemId()){

                    case R.id.ic_home:
                        Intent intent = new Intent(context, homePageActivity.class);
                        context.startActivity(intent);
                        break;

                    case R.id.ic_camera:
                        Intent intent2 = new Intent(context, cameraActivity.class);
                        context.startActivity(intent2);
                        break;

                    case R.id.ic_about_us:
                        Intent intent3 = new Intent(context, aboutUsActivity.class);
                        context.startActivity(intent3);
                        break;



                }


                return false;

            }




        });

    }


}
