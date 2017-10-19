package com.example.nath.take3app.activity.homepage;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nath.take3app.R;
import com.example.nath.take3app.activity.utility.firebaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.File;

/**
 * Created by nath on 15-Oct-17.
 */

public class submitActivity extends AppCompatActivity {

    private static final String TAG = "submitActivity";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private firebaseHelper mFirebaseMethods;

    //for the images
    private ImageView mImageView;
    private String mCurrentPhotoPath;

    private TextView mtvUpload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);


        setupFirebaseAuth();
        mFirebaseMethods = new firebaseHelper(submitActivity.this);


        mImageView = (ImageView) findViewById(R.id.imageShare);

        Log.d(TAG, "onCreate: Starting.");
        Bundle bundle = getIntent().getExtras();
        mCurrentPhotoPath = bundle.getString(getString(R.string.selected_bitmap));
        setPic();

//        String test = mCurrentPhotoPath.substring(mCurrentPhotoPath.lastIndexOf("/")+1);
//        Log.d(TAG, "onCreate: testing  mCurrentPhotoPath= " + test);
        galleryAddPic();



        TextView mtvUpload = (TextView) findViewById(R.id.tvUpload);
        mtvUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onCreate: clicked mtvUpload");

                mFirebaseMethods.uploadNewPhoto(mCurrentPhotoPath);
                Toast.makeText(submitActivity.this, "trying to upload", Toast.LENGTH_SHORT).show();
            }
        });



    }


    private void setPic() {
        Log.d(TAG, "setPic: starting.");

        // Get the dimensions of the View
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();
        Log.d(TAG, "setPic: targetW." + targetW + " targetH " + targetH);

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int x = 0;
        if (targetW != 0) {
            x = photoW / targetW;
        }
        int y = 0;
        if (targetH != 0) {
            y = photoH / targetH;
        }

        int scaleFactor = Math.min(x, y);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

        bitmap = rotateImage(bitmap, 90);
        mImageView.setImageBitmap(bitmap);

    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    private void galleryAddPic() {
        Log.d(TAG, "galleryAddPic: starting.");

        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

/*
----------------------------------------firebase----------------------------------------------------

 */


    /**
     * Setup the firebase auth object
     */
    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
//        Log.d(TAG, "onDataChange: image count: " + imageCount);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();


                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
}







    /**
     * CAMERA STUFF NOT USELESS---------------------------------------------------------------------
     */
//
//
//    static final int REQUEST_IMAGE_CAPTURE = 1;
//
////    private void dispatchTakePictureIntent() {
////        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
////            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
////        }
////    }
//
//
//
//
////    @Override
////    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
////        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
////            setPic();
////            galleryAddPic();
////
////
////
////
////
////
////            Log.d(TAG,"onActivityResult print data " +data);
//////            Bundle extras = data.getExtras();
//////            Bitmap imageBitmap = (Bitmap) extras.get("data");
//////            mImageView.setImageBitmap(imageBitmap);
////        }
////    }
//
//    String mCurrentPhotoPath;
//
//    private File createImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
////        File storageDir = getExternalFilesDir("Pictures/Environment.DIRECTORY_PICTURES);
////        File storageDir = getFilesDir ();
//
//
//
////        File file = new File(context.getFilesDir(), filename);
//
//
//
//
//
////        storageDir = openFileInput(Environment.DIRECTORY_PICTURES);
//        Log.d(TAG, "createImageFile: storage dir " + storageDir);
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//
//        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = image.getAbsolutePath();
//        return image;
//    }
//
//    static final int REQUEST_TAKE_PHOTO = 1;
//
//    private void dispatchTakePictureIntent() {
//        Log.d(TAG, "dispatchTakePictureIntent: starting.");
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // Ensure that there's a camera activity to handle the intent
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            // Create the File where the photo should go
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
//                // Error occurred while creating the File
//            }
//            // Continue only if the File was successfully created
//            if (photoFile != null) {
//                Uri photoURI = FileProvider.getUriForFile(this,
//                        "com.example.android.fileProvider",
//                        photoFile);
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
//                setResult(this.RESULT_OK, takePictureIntent);
//            }
//        }
//        Log.d(TAG, "dispatchTakePictureIntent: finished.");
//
//    }
//
//    private void galleryAddPic() {
//        Log.d(TAG, "galleryAddPic: starting.");
//
//        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        File f = new File(mCurrentPhotoPath);
//        Uri contentUri = Uri.fromFile(f);
//        mediaScanIntent.setData(contentUri);
//        this.sendBroadcast(mediaScanIntent);
//    }
//
//
//    private void setPic() {
//        Log.d(TAG, "setPic: starting.");
//
//        // Get the dimensions of the View
//        int targetW = mImageView.getWidth();
//        int targetH = mImageView.getHeight();
//        Log.d(TAG, "setPic: targetW." +targetW +" targetH " + targetH);
//
//        // Get the dimensions of the bitmap
//        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//        bmOptions.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
//        int photoW = bmOptions.outWidth;
//        int photoH = bmOptions.outHeight;
//
//        // Determine how much to scale down the image
//        int x = 0;
//        if(targetW !=0 ){
//            x = photoW/targetW;
//        }
//        int y = 0;
//        if(targetH != 0){
//            y = photoH/targetH;
//        }
//
//        int scaleFactor = Math.min(x, y);
//
//        // Decode the image file into a Bitmap sized to fill the View
//        bmOptions.inJustDecodeBounds = false;
//        bmOptions.inSampleSize = scaleFactor;
//        bmOptions.inPurgeable = true;
//
//        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
//
//        bitmap = rotateImage(bitmap, 90);
//        mImageView.setImageBitmap(bitmap);
//
//    }
//
//    /**
//     * gets the image url from the incoming intent and displays the chosen image
//     */
//
//    private String imgUrl;
//    private void setImage(){
//
//        intent = getIntent();
//        ImageView image = (ImageView) findViewById(R.id.imageShare);
//        if(intent.hasExtra(getString(R.string.selected_bitmap))){
//            bitmap = (Bitmap) intent.getParcelableExtra(getString(R.string.selected_bitmap));
//            Log.d(TAG, "setImage: got new bitmap");
//            image.setImageBitmap(bitmap);
//        }
//    }
//
//
//    public static Bitmap rotateImage(Bitmap source, float angle) {
//        Matrix matrix = new Matrix();
//        matrix.postRotate(angle);
//        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
//                matrix, true);
//    }
//
//
//
//






    /**
        1) photos data model

        2) add properties to the photo objects

        3) count the number of photos

        4) upload the photo to firebase storage and insert to nodes

        5)



     */