package com.example.nath.take3app.activity.homepage;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nath.take3app.R;
import com.example.nath.take3app.activity.utility.Permissions;
/**
 * Created by nath on 11-Oct-17.
 */

public class fragmentCamera extends Fragment{


    private static final String TAG = "fragmentAboutUs";
    public static final int VERIFY_PERMISSIONS_REQUEST = 1;

    public static final int CAMERA_REQUEST_CODE = 55;





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        return view;








    }

    public static Fragment newInstance() {
        fragmentCamera fragment = new fragmentCamera();
        return fragment;
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
        int permissionRequest = ActivityCompat.checkSelfPermission(getActivity(), permission);
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
                getActivity(),
                permission,
                VERIFY_PERMISSIONS_REQUEST
        );
    }

}
