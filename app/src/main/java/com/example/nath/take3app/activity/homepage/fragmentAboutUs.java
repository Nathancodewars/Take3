package com.example.nath.take3app.activity.homepage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.nath.take3app.R;

/**
 * Created by nath on 11-Oct-17.
 */

public class fragmentAboutUs extends Fragment{

    private static final String TAG = "fragmentAboutUs";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        return view;
    }






    public static Fragment newInstance() {
        fragmentAboutUs fragment = new fragmentAboutUs();
        return fragment;
    }
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_about, container, false);
//    }

}
