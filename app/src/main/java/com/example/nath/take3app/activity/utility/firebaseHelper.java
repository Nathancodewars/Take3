package com.example.nath.take3app.activity.utility;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.nath.take3app.R;
import com.example.nath.take3app.activity.dataModels.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


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


    public firebaseHelper(Context context) {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

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






    public void addNewUser(String fullName){
        User user = new User(fullName);

        myRef.child(mContext.getString(R.string.dbname_personal_details))
                .child(userID)
                .setValue(user);


    }

}
