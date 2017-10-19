package com.example.nath.take3app.activity.utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nath.take3app.R;
import com.example.nath.take3app.activity.dataModels.User;
import com.example.nath.take3app.activity.homepage.homePageActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.File;


/**
 * Created by nath on 11-Oct-17.
 */

public class firebaseHelper {


    private static final String TAG = "firebaseHelper";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Context mContext;
    private String userID;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private Boolean regResult;

    private StorageReference mStorageReference;
    private double mPhotoUploadProgress = 0;

    public firebaseHelper(Context context) {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mStorageReference = FirebaseStorage.getInstance().getReference();
        mContext = context;

        if(mAuth.getCurrentUser() != null){
            userID = mAuth.getCurrentUser().getUid();
        }
    }



    /**
     * Register a new email and password to Firebase Authentication
     * @param email
     * @param password
     * @param name
     */
    public void registerNewEmail(final String email, String password, final String name){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(mContext, R.string.reg_failed,
                                    Toast.LENGTH_SHORT).show();
                        }else if(task.isSuccessful()){
                            userID = mAuth.getCurrentUser().getUid();
                            Log.d(TAG, "onComplete: Authstate changed: " + userID);
                            Toast.makeText(mContext, R.string.reg_success,
                                    Toast.LENGTH_SHORT).show();

                        }

                }
            });
    }

    /*
    add new user
     */
    public void addNewUser(String fullName){
        User user = new User(fullName);

        myRef.child(mContext.getString(R.string.dbname_personal_details))
                .child(userID)
                .setValue(user);


    }

    /*
    upload photo
     */
    public void uploadNewPhoto(String imgUrl){
        Log.d(TAG, "uploadNewPhoto: start uploadNewPhoto");

        StorageReference storageReference = mStorageReference
                .child("photos/users/" + userID + "/"+ imgUrl.substring(imgUrl.lastIndexOf("/")+1));


        Bitmap bm =ImageManager.getBitmap((imgUrl));

        byte[] bytes = ImageManager.getBytesFromBitmap(bm);
        Log.d(TAG, "uploadNewPhoto: bm size: " + bm.getByteCount());


        UploadTask uploadTask = null;
        uploadTask = storageReference.putBytes(bytes);


        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri firebaseUri = taskSnapshot.getDownloadUrl();

                //add photo nodes

                //navigate to new feed

                Toast.makeText(mContext, "Photo uploaded successfully", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onSuccess: photo upload success");
                Intent intent = new Intent(mContext, homePageActivity.class);
                mContext.startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                ((Activity)mContext).finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"onFailure: failed to upload.");
                Toast.makeText(mContext, "Photo failed to upload", Toast.LENGTH_SHORT).show();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100*taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                Log.d(TAG,"onProgress: upload progress1: " + progress );
                if(progress - 15 > mPhotoUploadProgress){
                    Toast.makeText(mContext, "photo upload in progress: " + String.format("%.0f", progress) + "%", Toast.LENGTH_SHORT).show();

                    mPhotoUploadProgress = progress;
                }
                Log.d(TAG,"onProgress: upload progress2: " + progress + "% done");
            }
        });



    }








}
