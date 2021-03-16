package com.baro.ui.spalsh;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.baro.R;

public class SplashLoggingFragment extends Fragment {

    private ImageButton photoThumbnailButton;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private ImageButton nextButton;

    public SplashLoggingFragment() {
        // Required empty public constructor
    }

    public static SplashLoggingFragment newInstance() {
        SplashLoggingFragment fragment = new SplashLoggingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_splash, container, false);
        configurePhotoThumbnailButton(view);
        configureUsernameEditText(view);
        configurePasswordEditText(view);
        configureNextButton(view);
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }
    private void configurePhotoThumbnailButton(View view) {

        photoThumbnailButton = view.findViewById(R.id.btn_account);

        photoThumbnailButton.setOnClickListener(v -> {
            // TODO Implement this
        });
    }

    private void configureNextButton(View view) {
        nextButton = view.findViewById(R.id.btn_next);

        nextButton.setOnClickListener(v -> {
            // TODO Implement this
        });
    }

    private void configureUsernameEditText(View view) {
        usernameEditText = view.findViewById(R.id.edit_text_username);
    }

    private void configurePasswordEditText(View view) {
        passwordEditText = view.findViewById(R.id.edit_text_password);
    }
}