package com.example.nath.take3app.activity.utility;

import android.Manifest;

/**
 * Created by nath on 15-Oct-17.
 */

public class Permissions {

    public static final String[] PERMISSIONS= {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    public static final String[] CAMERA_PERMISSION = {
            Manifest.permission.CAMERA,
    };
    public static final String[] READ_EXTERNAL_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    public static final String[] WRITE_EXTERNAL_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    public static final String[] ACCESS_FINE_LOCATION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
    };
    public static final String[] ACCESS_COARSE_LOCATION = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
    };
}
