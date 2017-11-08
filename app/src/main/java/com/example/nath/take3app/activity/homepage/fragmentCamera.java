package com.example.nath.take3app.activity.homepage;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.nath.take3app.R;
import com.example.nath.take3app.activity.utility.Permissions;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by nath on 11-Oct-17.
 */

public class fragmentCamera extends Fragment{


    private static final String TAG = "fragmentAboutUs";
    public static final int VERIFY_PERMISSIONS_REQUEST = 1;

    public static final int CAMERA_REQUEST_CODE = 55;
    private Button openCameraBTN;





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        openCameraBTN = (Button) view.findViewById(R.id.activity_fragment_camera_CameraOpenButton);
        openCameraBTN.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        return view;

        // Ensure that there's a camera activity to handle the intent





    }

    public static Fragment newInstance() {
        fragmentCamera fragment = new fragmentCamera();
        return fragment;
    }

/*
       ----------------------------camera----------------------------------------
 */

    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Log.d(TAG, "dispatchTakePictureIntent: starting.");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
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

                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.example.android.fileProvider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                Log.d(TAG, "dispatchTakePictureIntent: photofile not null takepicture intent. = " + photoURI);

                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                getActivity().setResult(getActivity().RESULT_OK, takePictureIntent);
            }
        }
        Log.d(TAG, "dispatchTakePictureIntent: finished.");
    }


    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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
        getActivity().sendBroadcast(mediaScanIntent);
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
                Intent intent = new Intent(getActivity(), submitActivity.class);
                intent.putExtra(getString(R.string.selected_bitmap), mCurrentPhotoPath);
                startActivity(intent);
            } catch (NullPointerException e) {
                Log.d(TAG, "onActivityResult: NullPointerException: " + e.getMessage());
            }
        }
    }





}
