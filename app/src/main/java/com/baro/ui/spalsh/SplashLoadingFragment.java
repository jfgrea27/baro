package com.baro.ui.spalsh;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baro.R;
import com.baro.ui.main.MainActivity;

public class SplashLoadingFragment extends Fragment {

    private static int SPLASH_TIME_OUT = 1000;


    public SplashLoadingFragment() {
        // Required empty public constructor
    }

    public static SplashLoadingFragment newInstance() {
        SplashLoadingFragment fragment = new SplashLoadingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent startMainActivity = new Intent(getActivity(),  MainActivity.class);
                startActivity(startMainActivity);
                getActivity().finish();
            }
        }, SPLASH_TIME_OUT);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }


}