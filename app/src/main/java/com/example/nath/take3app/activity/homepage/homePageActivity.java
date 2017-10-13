package com.example.nath.take3app.activity.homepage;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.nath.take3app.R;
import com.example.nath.take3app.activity.logins.MainActivity;
import com.example.nath.take3app.activity.logins.loginActivity;
import com.example.nath.take3app.activity.utility.bottomNavigationViewHelper;
import com.example.nath.take3app.activity.utility.topRightMenuHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class homePageActivity extends AppCompatActivity {

    private static final String TAG ="homePageActivity";
    private Context mContext = homePageActivity.this;
    private int activity_num = 0;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;



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
    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up bottom navigation.");
        final BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        bottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        setUpToolBar();
        bottomNavigationViewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item){

                Fragment selectedFragment = null;
                switch(item.getItemId()){
                    case R.id.ic_home:
                        selectedFragment = fragmentHomePage.newInstance();
                        activity_num = 0;
                        break;

                    case R.id.ic_camera:
                        selectedFragment = fragmentCamera.newInstance();
                        activity_num = 1;
                        break;

                    case R.id.ic_about_us:
                        selectedFragment = fragmentAboutUs.newInstance();
                        activity_num = 2;
                        break;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.middleMainContainer, selectedFragment);
//                transaction.addToBackStack(getString());
                transaction.commit();

                Menu menu = bottomNavigationViewEx.getMenu();
                MenuItem menuItem = menu.getItem(activity_num);
                menuItem.setChecked(true);
                return false;

            }




        });
    }


    /*
    * Setup Top right nav
     */

    private void setUpToolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.topTabs);
        setSupportActionBar(toolbar);
        topRightMenuHelper.enableToolBar(mContext, toolbar);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
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
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.top_right_menu, menu);
        return true;
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







}
