package com.example.nath.take3app.activity.logins;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nath.take3app.R;
import com.example.nath.take3app.activity.homepage.homePageActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.nath.take3app.activity.utility.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class registerActivity extends AppCompatActivity {


    private final String TAG = "registerActivity";
    private Context mContext = registerActivity.this;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Button btnRegister;
    private String email, password, full_name;
    private EditText mEmail, mPassword, mName;
    private FirebaseHelper firebaseHelper;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private ProgressBar mProgressBar;
    private TextView mLoadingPleaseWait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Log.d(TAG, "onCreate: starting registerActivity");

        firebaseHelper = new FirebaseHelper(mContext);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();




        setupFirebaseAuth();
        initNames();
        initBtnRegister();
        keyboardHide();


        Log.d(TAG, "onCreate: finished registerActivity");
    }

    /** Method to hide keyboard
     *
     */
    private void keyboardHide(){
        findViewById(R.id.activity_register_main_container).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });
    }


    /**
     * init names
     */
    private void initNames(){
        Log.d(TAG, "initNames: init.");
        btnRegister = (Button) findViewById(R.id.activity_register_btn_register);
        mEmail = (EditText) findViewById(R.id.activity_register_et_email);
        mPassword = (EditText) findViewById(R.id.activity_register_et_password);
        mName = (EditText) findViewById(R.id.activity_register_et_full_name);
        mLoadingPleaseWait = (TextView) findViewById(R.id.activity_register_tv_please_wait);
        mProgressBar = (ProgressBar) findViewById(R.id.activity_register_progress_bar);
        mProgressBar.setVisibility(View.GONE);
        mLoadingPleaseWait.setVisibility(View.GONE);
    }


    /**
     * init the button for register
     */
    private void initBtnRegister(){
        btnRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d(TAG, "initBtnRegister,onClick:btn click");
                email = mEmail.getText().toString().trim();
                password = mPassword.getText().toString();
                full_name = mName.getText().toString();

                if(checkInputs(email, full_name, password)){
                    Log.d(TAG, "initBtnRegister,onClick:inputs are correct");
//                    mLoadingPleaseWait.setVisibility(View.VISIBLE);
//                    mProgressBar.setVisibility(View.VISIBLE);
                    firebaseHelper.registerNewEmail(email,password,full_name);
                }

                Log.d(TAG, "initBtnRegister,onClick:finished");

            }

        });


    }

    private boolean checkInputs(String email, String username, String password){
        Log.d(TAG, "checkInputs: checking inputs for null values.");
        if(email.equals("") || username.equals("") || password.equals("")){
            Toast.makeText(mContext, R.string.fill_in_all_fields, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }



/*
----------------------------------------------Firebase----------------------------------------------
 */





    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {

                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d(TAG, "onDataChange:adding nwe user");

                            //check

                            //add user personal data ie.name
                            firebaseHelper.addNewUser(full_name);

                            Intent intent = new Intent(mContext, homePageActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d(TAG, "DatabaseError:databaseError");


                        }
                    });

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    mProgressBar.setVisibility(View.GONE);
                    mLoadingPleaseWait.setVisibility(View.GONE);
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
        Log.d(TAG, "wtf wtf");
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
