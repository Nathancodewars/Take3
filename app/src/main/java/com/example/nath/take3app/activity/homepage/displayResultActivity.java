package com.example.nath.take3app.activity.homepage;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nath.take3app.R;
import com.example.nath.take3app.activity.recycleView.TextboxChecker;
import com.example.nath.take3app.activity.recycleView.ViewAdapter;
import com.example.nath.take3app.activity.utility.*;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 * Created by nath on 20-Oct-17.
 */

public class displayResultActivity extends AppCompatActivity{
    private static final String TAG = "displayResultActivity";
    private Context mContext = displayResultActivity.this;

    private String visionResult;
    private String imgUrl;
    private ArrayList<String> addressTemp;
    private ArrayList<TextboxChecker> arrayListTextboxChecker;

    //firebase
    private FirebaseHelper mfirebaseHelper;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    //widgets
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private TextView mFinishBTN;
    private ProgressBar mProgressBar;
    private TextView mPleaseWaitTV;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starting.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_result);
        init();
        HideProgressBar();

        Log.d(TAG, "onCreate: visionResult." + visionResult);
        initReclerView();
        initFinishBTN();
        keyboardHide();
    }

    /** Method to hide keyboard
     *
     */
    private void keyboardHide(){
        findViewById(R.id.activity_display_result_container).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });
    }

    private void initReclerView(){
        recyclerView = (RecyclerView) findViewById(R.id.activity_display_result_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        arrayListTextboxChecker = new ArrayList<>();


        for(int i=0; i<addressTemp.size(); i++){
            TextboxChecker textboxChecker = new TextboxChecker();
            textboxChecker.setText(addressTemp.get(i));
            arrayListTextboxChecker.add(textboxChecker);
        }

        mAdapter = new ViewAdapter(mContext ,arrayListTextboxChecker);
        recyclerView.setAdapter(mAdapter);
    }


    private void initFinishBTN(){
        mFinishBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowProgressBar();
                Boolean addedTheCategorySucess = false;
                Boolean userSelectedCategory = false;

                for(int i=0; i<arrayListTextboxChecker.size(); i++) {

                    if (arrayListTextboxChecker.get(i).isSelected()) {
                        userSelectedCategory = true;
                        if (arrayListTextboxChecker.get(i).getQty() != null) {
                            mfirebaseHelper.UpdatePhotoData(imgUrl,
                                    arrayListTextboxChecker.get(i).getText(),
                                    arrayListTextboxChecker.get(i).getQty());
                            addedTheCategorySucess = true;
                        } else {
                            addedTheCategorySucess = false;
                            Toast.makeText(mContext, R.string.please_add_qty, Toast.LENGTH_SHORT).show();
                        }

                    }
                }


                if(!userSelectedCategory){
                    Toast.makeText(mContext, R.string.please_add_category, Toast.LENGTH_SHORT).show();
                }

                if(addedTheCategorySucess){
                    mfirebaseHelper.VisionBoolean(imgUrl, true);
                    Intent intent = new Intent(mContext, homePageActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }else{
                    HideProgressBar();

                }


            }
        });
    }
    private void init(){
        Log.d(TAG, "init: starting.");
        //get extras
        Bundle extras = getIntent().getExtras();
        visionResult = extras.getString(mContext.getString(R.string.vision_result));
        addressTemp = extras.getStringArrayList("addressesTemp");
        imgUrl = extras.getString(mContext.getString(R.string.selected_image));

        //firebase
        mfirebaseHelper = new FirebaseHelper(mContext);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        //init wids
        mProgressBar = (ProgressBar) findViewById(R.id.activity_display_result_progress_circle);
        mPleaseWaitTV = (TextView) findViewById(R.id.activity_display_result_tv_please_wait);
        mFinishBTN = (TextView) findViewById(R.id.result_top_finish);


    }

    private void HideProgressBar(){
        mProgressBar.setVisibility(View.GONE);
        mPleaseWaitTV.setVisibility(View.GONE);
    }

    private void ShowProgressBar(){
        mProgressBar.setVisibility(View.VISIBLE);
        mPleaseWaitTV.setVisibility(View.VISIBLE);

    }













}
