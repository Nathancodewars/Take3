package com.example.nath.take3app.activity.homepage;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.nath.take3app.R;
import com.example.nath.take3app.activity.logins.MainActivity;
import com.example.nath.take3app.activity.logins.loginActivity;
import com.example.nath.take3app.activity.utility.bottomNavigationViewHelper;
import com.example.nath.take3app.activity.utility.topRightMenuHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Date;

import static android.os.Environment.getExternalStorageDirectory;

public class homePageActivity extends AppCompatActivity {

    private static final String TAG = "homePageActivity";
    private Context mContext = homePageActivity.this;
    private int activity_num = 0;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public static final int CAMERA_REQUEST_CODE = 55;
    private Uri mImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate: Starting.");
        setupBottomNavigationView();
        setUpToolBar();


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.middleMainContainer, fragmentHomePage.newInstance());
        transaction.commit();

        setupFirebaseAuth();


    }


    /*
     * BottomNavigationView Setup
     */
    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting up bottom navigation.");
        final BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        bottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        setUpToolBar();
        bottomNavigationViewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.ic_home:
                        selectedFragment = fragmentHomePage.newInstance();
                        activity_num = 0;
                        fragmenTrans(selectedFragment, bottomNavigationViewEx);

                        break;

                    case R.id.ic_camera:
                        //launch camera
                        dispatchTakePictureIntent();
//                        selectedFragment = fragmentHomePage.newInstance();
//                        activity_num = 0;
//                        fragmenTrans(selectedFragment, bottomNavigationViewEx);
                        break;


                    case R.id.ic_about_us:
                        selectedFragment = fragmentAboutUs.newInstance();
                        activity_num = 2;
                        fragmenTrans(selectedFragment, bottomNavigationViewEx);
                        break;
                }
                return false;

            }


        });
    }

    public void fragmenTrans(Fragment selectedFragment, BottomNavigationViewEx bottomNavigationViewEx) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.middleMainContainer, selectedFragment);
        //transaction.addToBackStack(getString());
        transaction.commit();

        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(activity_num);
        menuItem.setChecked(true);
    }


    public void nextActTest() {
        Log.d(TAG, "nextActTest: start intent");
        Intent intent = new Intent(mContext, submitActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }







    /*
    * Setup Top right nav
     */

    private void setUpToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.topTabs);
        setSupportActionBar(toolbar);
        topRightMenuHelper.enableToolBar(mContext, toolbar);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_donate:
                        break;
                    case R.id.action_help:
                        break;
                    case R.id.action_settings:
                        break;
                    case R.id.action_Logout:
                        mAuth.signOut();
                        break;
                }


                return false;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_right_menu, menu);
        return true;
    }


    private File createTemporaryFile(String part, String ext) throws Exception {
        File tempDir = getExternalStorageDirectory();
        tempDir = new File(tempDir.getAbsolutePath() + "/.temp/");
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
        return File.createTempFile(part, ext, tempDir);
    }

/*
----------------------------------------------Firebase----------------------------------------------
 */


    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: setting up Firebase auth.");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                //check if the user is logged in
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    Log.d(TAG, "onAuthStateChanged navigating back to main screen");
                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }
                // ...
            }
        };


    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }


    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }




/*
       ----------------------------camera----------------------------------------
 */

    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Log.d(TAG, "dispatchTakePictureIntent: starting.");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d(TAG, "dispatchTakePictureIntent: error" + ex);
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Log.d(TAG, "dispatchTakePictureIntent: photofile not null takepicture intent.");

                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileProvider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                Log.d(TAG, "dispatchTakePictureIntent: photofile not null takepicture intent. = " + photoURI);

                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                setResult(this.RESULT_OK, takePictureIntent);
            }
        }
        Log.d(TAG, "dispatchTakePictureIntent: finished.");
    }


    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        Log.d(TAG, "createImageFile: storage dir " + storageDir);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Log.d(TAG, "galleryAddPic: starting.");

        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResultL testing result code = " + resultCode);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == -1) {
            Log.d(TAG, "onActivityResult: done taking a photo.");
            Log.d(TAG, "onActivityResult: attempting to navigate to final share screen.");


//            galleryAddPic();
//            Log.d(TAG, "onActivityResult: data = "+ data);
//            bitmap = (Bitmap) data.getExtras().get("data");
//            Log.d(TAG, "onActivityResult: data = "+ data.getExtras().get("data"));

            try {
                Log.d(TAG, "onActivityResult: received new bitmap from camera: ");
                Intent intent = new Intent(mContext, submitActivity.class);
                intent.putExtra(getString(R.string.selected_bitmap), mCurrentPhotoPath);
                startActivity(intent);
            } catch (NullPointerException e) {
                Log.d(TAG, "onActivityResult: NullPointerException: " + e.getMessage());
            }
        }
    }
}

