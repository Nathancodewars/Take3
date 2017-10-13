package com.example.nath.take3app.activity.homepage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nath.take3app.R;

/**
 * Created by nath on 11-Oct-17.
 */

public class fragmentCamera extends Fragment{

    public static Fragment newInstance() {
        fragmentCamera fragment = new fragmentCamera();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }
}
