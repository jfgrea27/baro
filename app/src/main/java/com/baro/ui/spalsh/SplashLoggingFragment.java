package com.baro.ui.spalsh;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.baro.R;
import com.baro.constants.FileEnum;
import com.baro.constants.JSONEnum;
import com.baro.helpers.FileHelper;
import com.baro.helpers.JSONHelper;
import com.baro.ui.main.MainActivity;

import org.json.JSONObject;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.UUID;

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_logging, container, false);
        configurePhotoThumbnailButton(view);
        configureUsernameEditText(view);
        configurePasswordEditText(view);
        configureNextButton(view);
        return view;
    }
    private void configurePhotoThumbnailButton(View view) {

        photoThumbnailButton = view.findViewById(R.id.btn_account);

        photoThumbnailButton.setOnClickListener(v -> {
            // TODO Implement this
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void configureNextButton(View view) {
        nextButton = view.findViewById(R.id.btn_next);

        nextButton.setOnClickListener(v -> {
            if (usernameEditText.getText().length() > 5) {
                saveCredentials();
                Intent startMainActivity = new Intent(
                        getActivity(),
                        MainActivity.class);
                // Passing User to MainActivity
                startActivity(startMainActivity);
                getActivity().finish();
            } else {
                // TODO discuss if we need a username..
                Toast.makeText(
                        getContext(),
                        R.string.please_enter_valid_username_toast,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void configureUsernameEditText(View view) {
        usernameEditText = view.findViewById(R.id.edit_text_username);
    }

    private void configurePasswordEditText(View view) {
        passwordEditText = view.findViewById(R.id.edit_text_password);
    }

    // Controller
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveCredentials() {
        HashMap<String, String> credentialDetails = new HashMap<>();
        UUID userUUID = UUID.randomUUID();
        credentialDetails.put(JSONEnum.USER_NAME_KEY.key, usernameEditText.getText().toString());
        credentialDetails.put(JSONEnum.USER_UUID_KEY.key, userUUID.toString());

        JSONObject jsonCredentials = JSONHelper.createJSONFromHashMap(credentialDetails);

        Path userMetaDataPath = Paths.get(
                String.valueOf(getActivity().getExternalFilesDir(null)),
                FileEnum.USER_DIRECTORY.key,
                FileEnum.META_DATA_FILE.key);

        File userMetaDataFile = FileHelper.createFileAtPath(userMetaDataPath);

        FileHelper.writeToFile(userMetaDataFile, jsonCredentials.toString());
    }


}