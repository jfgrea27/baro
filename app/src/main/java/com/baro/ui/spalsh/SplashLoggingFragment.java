package com.baro.ui.spalsh;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
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
            if(checkPermissions()) {

            }

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


    // Permissions

    private boolean checkPermissions() {
        return false;
//
//        if (!(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)){
//            if(shouldShowRequestPermissionRationale(getString(R.string.permssion_camera_info_message))) {
//                Toast.makeText(getView().getContext(), R.string.permssion_camera_info_message, Toast.LENGTH_LONG).show();
//            }
//
//            ActivityResultContracts.RequestPermission();
//        } else {
//            hasInternetAccess = true;
//        }
//
//        if (!(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED )) {
//            Toast.makeText(getView().getContext(), R.string.accessStorage, Toast.LENGTH_SHORT).show();
//            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, READ_WRITE_PERMISSION);
//        } else {
//            hasReadWriteStorageAccess = true;
//        }
    }

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {

                } else {
                    Toast.makeText(getView().getContext(), "Neec camera access", Toast.LENGTH_SHORT).show();
                }
            });
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