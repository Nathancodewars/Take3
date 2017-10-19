package com.example.nath.take3app.activity.homepage;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.nath.take3app.R;
import com.example.nath.take3app.activity.utility.Permissions;
import com.example.nath.take3app.activity.utility.bottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class cameraActivity extends AppCompatActivity {

    private static final String TAG ="cameraActivity";
    private Context mContext = cameraActivity.this;
    private static final int activity_num = 1;
    public static final int VERIFY_PERMISSIONS_REQUEST = 1;
    public static final int CAMERA_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_home);
        setupBottomNavigationView();

        Log.d(TAG, "onCreate: starting up.");


        if(checkPermissionArray(Permissions.PERMISSIONS)){
            //Permissions allowed
        }else{
            //verify Permissions
            verifyPermissions(Permissions.PERMISSIONS);
        }

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);



    }







    /**
     * chcek an array of permission
     * @param permissions
     * @return
     */
    public boolean checkPermissionArray(String[] permissions){
        Log.d(TAG, "checkPermissionArray: checking array permissions");
        for(int i=0; i<permissions.length; i++){
            String check = permissions[i];
            if(!checkPermission(check)){
                return false;
            }
        }
        return true;
    }

    /**
     * check single permission
     * @param permission
     * @return
     */
    public boolean checkPermission(String permission){
        Log.d(TAG, "checkPermission: checking a single permission:"+ permission);
        int permissionRequest = ActivityCompat.checkSelfPermission(cameraActivity.this, permission);
        if(permissionRequest != PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "checkPermission: permission was not granted for:"+ permission);
            return false;
        }else{
            Log.d(TAG, "checkPermission: permission was granted for: "+ permission);
            return true;
        }
    }

    public void verifyPermissions(String[] permission){
        Log.d(TAG, "verifyPermission: verifying permission" + permission);

        ActivityCompat.requestPermissions(
                cameraActivity.this,
                permission,
                VERIFY_PERMISSIONS_REQUEST
        );
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
