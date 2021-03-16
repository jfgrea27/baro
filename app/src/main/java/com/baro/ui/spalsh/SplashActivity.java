package com.baro.ui.spalsh;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.baro.R;
import com.baro.constants.FileEnum;
import com.baro.constants.JSONEnum;
import com.baro.helpers.FileHelper;
import com.baro.helpers.JSONHelper;
import com.baro.ui.main.MainActivity;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class SplashActivity extends AppCompatActivity {


    private boolean loggedIn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        if (savedInstanceState == null) {

            checkLoggedIn();

            if (loggedIn) {
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .add(R.id.fragment_container_view, SplashLoadingFragment.class, null)
                        .commit();

            } else{
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .add(R.id.fragment_container_view, SplashLoggingFragment.class, null)
                        .commit();

            }
        }
    }

    private void checkLoggedIn() {

    }


    private class LogingData extends AsyncTask<String, Void, Boolean> {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Boolean doInBackground(String... params) {

            Path userMetaDataPath = Paths.get(
                    String.valueOf(getExternalFilesDir(null)),
                    FileEnum.USER_DIRECTORY.key,
                    FileEnum.META_DATA_FILE.key);

            File userMetaDataFile = FileHelper.getFileAtPath(userMetaDataPath);

            if (userMetaDataFile != null) {
                String contentMetaData = FileHelper.readFile(userMetaDataFile);
                JSONObject jsonMetaData = JSONHelper.createJSONFromString(contentMetaData);
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {
            // TODO complete this

        }
    }
}