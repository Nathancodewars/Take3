package com.example.nath.take3app.activity.homepage;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nath.take3app.R;
import com.example.nath.take3app.activity.utility.ImageManager;
import com.example.nath.take3app.activity.utility.PackageManagerUtils;
import com.example.nath.take3app.activity.utility.FirebaseHelper;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.api.client.json.gson.GsonFactory;
import org.w3c.dom.Text;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * Created by nath on 15-Oct-17.
 */

public class submitActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION = 001;

    private static final String TAG = "submitActivity";
    private Context mContext = submitActivity.this;
    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseHelper mFirebaseMethods;

    //for the images
    private ImageView mImageView;
    private String mCurrentPhotoPath;
    private Bitmap mBitmap;
    private String visionResult;

    // google vision
    private static final String CLOUD_VISION_API_KEY = "AIzaSyDsPqMg6tNj2y9iZZy5ML_WxiOgUlqnLc8";
    private static final String ANDROID_PACKAGE_HEADER = "X-Android-Package";
    private static final String ANDROID_CERT_HEADER = "X-Android-Cert";

    //loading bar
    private ProgressBar mProgressBar;
    private TextView mTVPleaseWait, mtvUploadBTN;

//    location
    private double currentLatti;
    private double currentLongi;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: Starting.");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);

        //init variables
        init();

        //firebaseauth check
        setupFirebaseAuth();

        //set taken pic frame.
        setPic();




        //onclick listener for upload btn (tv)
        mtvUploadBTNinit();




    }

    private void init(){
        mImageView = (ImageView) findViewById(R.id.imageShare);
        mtvUploadBTN = (TextView) findViewById(R.id.tvUpload);
        mFirebaseMethods = new FirebaseHelper(submitActivity.this);
        Bundle bundle = getIntent().getExtras();
        mCurrentPhotoPath = bundle.getString(getString(R.string.selected_bitmap));
        mProgressBar = (ProgressBar) findViewById(R.id.activity_submit_progress_circle);
        mTVPleaseWait = (TextView) findViewById(R.id.activity_submit_tv_please_wait);

        mProgressBar.setVisibility(View.GONE);
        mTVPleaseWait.setVisibility(View.GONE);

        mBitmap = ImageManager.getBitmap((mCurrentPhotoPath));



    }








    private ArrayList<String> addresses = new ArrayList<String>();



    private void callCloudVision(final Bitmap bitmap) throws IOException {
        // Switch text to loading
//        mImageDetails.setText(R.string.loading_message);
        // Do the real work in an async task, because we need to use the network anyway
        new AsyncTask<Object, Void, String>() {
            @Override
            protected String doInBackground(Object... params) {
                try {
                    HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
                    JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

                    VisionRequestInitializer requestInitializer =
                            new VisionRequestInitializer(CLOUD_VISION_API_KEY) {
                                /**
                                 * We override this so we can inject important identifying fields into the HTTP
                                 * headers. This enables use of a restricted cloud platform API key.
                                 */
                                @Override
                                protected void initializeVisionRequest(VisionRequest<?> visionRequest)
                                        throws IOException {
                                    super.initializeVisionRequest(visionRequest);

                                    String packageName = getPackageName();
                                    visionRequest.getRequestHeaders().set(ANDROID_PACKAGE_HEADER, packageName);

                                    String sig = PackageManagerUtils.getSignature(getPackageManager(), packageName);

                                    visionRequest.getRequestHeaders().set(ANDROID_CERT_HEADER, sig);
                                }
                            };

                    Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
                    builder.setVisionRequestInitializer(requestInitializer);

                    Vision vision = builder.build();

                    BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                            new BatchAnnotateImagesRequest();
                    batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {{
                        AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();

                        // Add the image
                        Image base64EncodedImage = new Image();
                        // Convert the bitmap to a JPEG
                        // Just in case it's a format that Android understands but Cloud Vision
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
                        byte[] imageBytes = byteArrayOutputStream.toByteArray();

                        // Base64 encode the JPEG
                        base64EncodedImage.encodeContent(imageBytes);
                        annotateImageRequest.setImage(base64EncodedImage);

                        // add the features we want
                        annotateImageRequest.setFeatures(new ArrayList<Feature>() {{
                            Feature labelDetection = new Feature();
                            labelDetection.setType("LABEL_DETECTION");
                            labelDetection.setMaxResults(10);
                            add(labelDetection);
                        }});

                        // Add the list of one thing to the request
                        add(annotateImageRequest);
                    }});

                    Vision.Images.Annotate annotateRequest =
                            vision.images().annotate(batchAnnotateImagesRequest);
                    // Due to a bug: requests to Vision API containing large images fail when GZipped.
                    annotateRequest.setDisableGZipContent(true);
                    Log.d(TAG, "created Cloud Vision request object, sending request");

                    BatchAnnotateImagesResponse response = annotateRequest.execute();
                    addresses = ChangeToArrayList(response);
                    return convertResponseToString(response);

//                    return ChangeToArrayList(response);
                } catch (GoogleJsonResponseException e) {
                    Log.d(TAG, "failed to make API request because " + e.getContent());
                } catch (IOException e) {
                    Log.d(TAG, "failed to make API request because of other IOException " +
                            e.getMessage());
                }
                return "Cloud Vision API request failed. Check logs for details.";
            }

            protected void onPostExecute(String result) {
//                mImageDetails.setText(result);

                Log.d(TAG, "SPECIAL TAG XOL:" + result);
                visionResult = result;
                Log.d(TAG, "SPECIAL TAG XOL2:" + visionResult);
////                addresses = ChangeToArrayList();


                Intent intent = new Intent(mContext, displayResultActivity.class);

                intent.putExtra(mContext.getString(R.string.vision_result), visionResult);
                intent.putStringArrayListExtra("addressesTemp", addresses);
                intent.putExtra(mContext.getString(R.string.selected_image), mCurrentPhotoPath);


                Log.d(TAG, "onSuccess: puextra string addresses: "+ addresses);
                Log.d(TAG, "onSuccess: puextra string: "+ visionResult);

                startActivity(intent);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mBitmap.recycle();

                finish();

            }
        }.execute();
    }

    private ArrayList<String> ChangeToArrayList(BatchAnnotateImagesResponse response){
        ArrayList<String> Temp = new ArrayList<String>();
        List<EntityAnnotation> labels = response.getResponses().get(0).getLabelAnnotations();
        if (labels != null){
            for (EntityAnnotation label : labels){
                Temp.add(label.getDescription());
            }
        }
        return Temp;
    }
    private String convertResponseToString(BatchAnnotateImagesResponse response) {
        String message = " ";

        List<EntityAnnotation> labels = response.getResponses().get(0).getLabelAnnotations();
        if (labels != null) {
            for (EntityAnnotation label : labels) {
                message += String.format(Locale.US, "%.3f: %s", label.getScore(), label.getDescription());
                message += "\n";
            }
        } else {
            message += "nothing";
        }

        return message;
    }




    private void mtvUploadBTNinit(){
        mtvUploadBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onCreate: clicked mtvUpload");
                mProgressBar.setVisibility(View.VISIBLE);
                mTVPleaseWait.setVisibility(View.VISIBLE);

                locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                getLocation();

                Log.d(TAG, "onCreate: mtvUploadBTNinit visionResult: "+ visionResult);
//                Bitmap newBitmap = rotateImage(mBitmap, 270);
                mFirebaseMethods.uploadNewPhoto(mBitmap, mCurrentPhotoPath, visionResult, currentLatti, currentLongi);

                Toast.makeText(mContext, R.string.trying_to_upload, Toast.LENGTH_SHORT).show();

                try {
                    callCloudVision(mBitmap);
                    // Code that might throw
                    // an exception.
                } catch (Exception e) {
                    // Handle it.
                }
            }
        });
    }

    private void setPic() {
        Log.d(TAG, "setPic: starting.");
        try{
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();


            FileInputStream fis = new FileInputStream(mCurrentPhotoPath);
            BitmapFactory.decodeStream(fis, null, bmOptions);
            fis.close();




            // Get the dimensions of the View
            int targetW = mImageView.getWidth();
            int targetH = mImageView.getHeight();
            Log.d(TAG, "setPic: targetW." + targetW + " targetH " + targetH);

            // Get the dimensions of the bitmap
            bmOptions.inJustDecodeBounds = true;

    //        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
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



            fis = new FileInputStream(mCurrentPhotoPath);
            mBitmap = BitmapFactory.decodeStream(fis, null, bmOptions);
            fis.close();

            mBitmap = rotateImage(mBitmap, 270);
            mImageView.setImageBitmap(mBitmap);




        }catch (Exception e){

        }
//        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);


    }

    public Bitmap rotateImage(Bitmap source, float angle) {
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

    private void getLocation() {
        if( ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (location != null){
                currentLatti = location.getLatitude();
                currentLongi = location.getLongitude();
                Log.d(TAG,"getLocation: currentLatti : " + currentLatti);
                Log.d(TAG,"getLocation: currentLongi : " + currentLongi);







//                ((EditText)findViewById(R.id.etLocationLat)).setText("Latitude: " + latti);
//                ((EditText)findViewById(R.id.etLocationLong)).setText("Longitude: " + longi);
            } else {
//                ((EditText)findViewById(R.id.etLocationLat)).setText("Unable to find correct location.");
//                ((EditText)findViewById(R.id.etLocationLong)).setText("Unable to find correct location. ");
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,@NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION:
                getLocation();
                break;
        }
    }



}










    /**
        1) photos data model

        2) add properties to the photo objects

        3) count the number of photos

        4) upload the photo to firebase storage and insert to nodes

        5)



     */