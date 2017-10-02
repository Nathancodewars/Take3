package com.example.nath.take3app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nath.take3app.R;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;


import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText mEtUsername;
    private EditText mEtPassword;
    private Button mBtnLogin;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private final String TAG = "WutFace";


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        mEtUsername = (EditText)findViewById(R.id.activity_main_et_username);
//        mEtPassword = (EditText)findViewById(R.id.activity_main_et_password);
        mBtnLogin = (Button)findViewById(R.id.activity_main_btn_login);
        mAuth = FirebaseAuth.getInstance();

        /** login button
         *
         */

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                startloginActivity();
            }
        });


        // firebase login
//        mBtnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String email = mEtUsername.getText().toString().trim();
//                String password = mEtPassword.getText().toString();
//
//                if(isValidLogin(email, password)) {
//                    mAuth.signInWithEmailAndPassword(email, password)
//                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<AuthResult> task) {
//                                    Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
//
//                                    // If sign in fails, display a message to the user. If sign in succeeds
//                                    // the auth state listener will be notified and logic to handle the
//                                    // signed in user can be handled in the listener.
//                                    if (!task.isSuccessful()) {
//                                        Exception authEx = task.getException();
//                                        String errorMsg = "";
//                                        Log.w(TAG, "signInWithEmail:failed", task.getException());
//                                        Toast.makeText(MainActivity.this, R.string.auth_failed,
//                                                Toast.LENGTH_SHORT).show();
//
//                                    } else {
//                                        startHomeActivity();
//                                    }
//
//
//                                 }
//                            });
//
//                } else {
//                    Toast.makeText(MainActivity.this, R.string.auth_mtx,
//                            Toast.LENGTH_SHORT).show();
//                }
//            }
//
//        });







    }
    private boolean isValidEmail(String email){
        return !email.isEmpty();
    }

    private boolean isValidPassword(String password){
        return !password.isEmpty();
    }

    private boolean isValidLogin(String email, String password){
        return (isValidEmail(email) && isValidPassword(password));
    }

    private void startHomeActivity(){
        startActivity(new Intent(MainActivity.this, homePageActivity.class));

        finish();
    }

    private void startloginActivity(){
        startActivity(new Intent(MainActivity.this, loginActivity.class));
        finish();
    }
}
