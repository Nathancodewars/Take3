package com.example.nath.take3app.activity.logins;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.nath.take3app.R;


import com.example.nath.take3app.activity.homepage.homePageActivity;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText mEtUsername;
    private EditText mEtPassword;
    private Button mBtnLogin;

    private Context mContext = MainActivity.this;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    private final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Log.d(TAG, "onCreate: starting mainActivity");
        setupFirebaseAuth();


        mAuth = FirebaseAuth.getInstance();

        setupFirebaseAuth();
        registerSetup();














    }





    private void startHomeActivity(){
        startActivity(new Intent(MainActivity.this, homePageActivity.class));
        finish();
    }

    private void startloginActivity(){
        startActivity(new Intent(MainActivity.this, loginActivity.class));
        finish();
    }



    private void registerSetup(){

        Button btnLogin = (Button) findViewById(R.id.activity_main_btn_register);
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: register setup btn onClick");

                Intent intent = new Intent(mContext, registerActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }













/*
----------------------------------------------Firebase----------------------------------------------
 */





    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up Firebase auth.");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                //check if the user is logged in
                checkCurrentUser(user);
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


    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        checkCurrentUser(mAuth.getCurrentUser());
    }


    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void checkCurrentUser(FirebaseUser user){
        Log.d(TAG, "checkCurrentUser: check if user is logged in.");
        if(user ==null){
            Intent intent = new Intent(mContext, loginActivity.class);
            startActivity(intent);
        }
//        else{
//            Intent intent = new Intent(MainActivity.this, homePageActivity.class);
//            startActivity(intent);
//            finish();
//        }
    }





}



