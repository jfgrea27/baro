package com.baro.ui.spalsh;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.annotation.VisibleForTesting;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.baro.R;
import com.baro.constants.AppTags;
import com.baro.constants.FileEnum;
import com.baro.constants.JSONEnum;
import com.baro.dialogs.ImageDialog;
import com.baro.helpers.FileHelper;
import com.baro.helpers.JSONHelper;
import com.baro.models.User;
import com.baro.ui.main.MainActivity;

import org.json.JSONObject;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.baro.constants.AppCodes.CAMERA_ROLL_SELECTION;
import static com.baro.constants.AppCodes.GALLERY_SELECTION;

@RequiresApi(api = Build.VERSION_CODES.N)
public class SplashLoggingFragment extends Fragment implements ImageDialog.OnInputListener {

    private ImageButton photoThumbnailButton;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private ImageButton nextButton;

    private boolean cameraPermission = false;
    private boolean readPermission = false;
    private boolean writePermission = false;

    private Uri photoUri = null;

    public SplashLoggingFragment() {

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
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void configurePhotoThumbnailButton(View view) {

        photoThumbnailButton = view.findViewById(R.id.btn_account);

        photoThumbnailButton.setOnClickListener(v -> {
            checkCameraPermissions();
            if (cameraPermission & readPermission & writePermission) {

                ImageDialog imageDialog = new ImageDialog(this);
                imageDialog.show(getParentFragmentManager(), AppTags.THUMBNAIL_SELECTION.toString());
            }
        });
    }

    ActivityResultLauncher<String> getGalleryContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onActivityResult(Uri uri) {

                    photoUri = uri;
                    photoThumbnailButton.setImageURI(uri);
                }
            });


    ActivityResultLauncher<Uri> getCameraContent = registerForActivityResult(
            new ActivityResultContracts.TakePicture(),
            result -> {
                if (result) {
                    Path userMetaDataPath = Paths.get(
                            String.valueOf(getActivity().getExternalFilesDir(null)),
                            FileEnum.USER_DIRECTORY.key,
                            FileEnum.PHOTO_THUMBNAIL_FILE.key);

                    File userThumbnailFile = FileHelper.createFileAtPath(userMetaDataPath);
                    photoUri = Uri.fromFile(userThumbnailFile);
                    photoThumbnailButton.setImageURI(photoUri);

                }
            });

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void sendInput(int choice) {
        if (choice == CAMERA_ROLL_SELECTION.code) {
            Path userMetaDataPath = Paths.get(
                    String.valueOf(getActivity().getExternalFilesDir(null)),
                    FileEnum.USER_DIRECTORY.key,
                    FileEnum.PHOTO_THUMBNAIL_FILE.key);

            File userThumbnailFile = FileHelper.createFileAtPath(userMetaDataPath);
            Uri photoURI = FileProvider.getUriForFile(getContext(), getActivity().getApplicationContext().getPackageName() + ".fileprovider", userThumbnailFile);

            getCameraContent.launch(photoURI);
        } else if (choice == GALLERY_SELECTION.code) {
            getGalleryContent.launch("image/*");

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void configureNextButton(View view) {
        nextButton = view.findViewById(R.id.btn_next);

        nextButton.setOnClickListener(v -> {
            if (usernameEditText.getText().length() > 5) {

                UserCredentialsSave userCredentialsSave = new UserCredentialsSave();
                userCredentialsSave.execute();
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

    public void checkCameraPermissions() {
        ArrayList<String> permissionsToBeGranted = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(
                getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            permissionsToBeGranted.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        } else {
            readPermission = true;
        }

        if (ContextCompat.checkSelfPermission(
                getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            permissionsToBeGranted.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        } else {
            writePermission = true;
        }

        if (ContextCompat.checkSelfPermission(
                getContext(), Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            permissionsToBeGranted.add(Manifest.permission.CAMERA);
        } else {
            cameraPermission = true;
        }
        if (permissionsToBeGranted.size() > 0) {
            String[] permissions = permissionsToBeGranted.toArray(new String[0]);
            requestPermissionLauncher.launch(permissions);
        }
    }

    @VisibleForTesting
    private ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), permissions -> {
                for (Map.Entry<String, Boolean> permission   : permissions.entrySet()) {
                    handlePermission(permission.getKey(), permission.getValue());
                };
            });

    public void handlePermission(String permission, boolean isGranted) {
        switch (permission) {
            case Manifest.permission.READ_EXTERNAL_STORAGE:
                if (!isGranted) {
                    Toast.makeText(getContext(), R.string.read_storage_permission, Toast.LENGTH_LONG).show();
                } else{
                    readPermission = true;
                }
                break;
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                if (!isGranted) {
                    Toast.makeText(getContext(), R.string.write_storage_permission, Toast.LENGTH_LONG).show();
                } else{
                    writePermission = true;
                }
                break;
            case Manifest.permission.CAMERA:
                if (!isGranted) {
                    Toast.makeText(getContext(), R.string.need_camera_access_toast, Toast.LENGTH_LONG).show();
                } else{
                    cameraPermission = true;
                }
                break;
            default:
                break;
        }
    }



    private class UserCredentialsSave extends AsyncTask<Void, Void, Boolean> {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Boolean doInBackground(Void... voids) {
            // Save the Meta information
            saveCredentials();
            // Save Photo URI
            savePhotoUri();

            return true;
        }

        protected void onPostExecute(Boolean result) {
            if (result) {
                Intent startMainActivity = new Intent(
                        getActivity(),
                        MainActivity.class);
                startActivity(startMainActivity);
                getActivity().finish();
            } else {
                Toast.makeText(getContext(), R.string.error_saving_credentials, Toast.LENGTH_LONG).show();
            }
        }

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

        @RequiresApi(api = Build.VERSION_CODES.O)
        private void savePhotoUri() {
            if (photoUri != null) {
                Path userThumbnailPicturePath = Paths.get(
                        String.valueOf(getContext().getExternalFilesDir(null)),
                        FileEnum.USER_DIRECTORY.key,
                        FileEnum.PHOTO_THUMBNAIL_FILE.key);
                File file = new File(userThumbnailPicturePath.toString());

                FileHelper.writeUriToFile(file, photoUri, getActivity().getContentResolver());
            }
        }
    }


}