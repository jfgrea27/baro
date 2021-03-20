package com.baro.ui.spalsh;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.baro.R;
import com.baro.constants.FileEnum;
import com.baro.constants.IntentEnum;
import com.baro.constants.JSONEnum;
import com.baro.helpers.FileHelper;
import com.baro.helpers.JSONHelper;
import com.baro.models.User;
import com.baro.ui.main.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class  SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1000;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        configureProgressBar();


        if (savedInstanceState == null) {

            new Handler().postDelayed((Runnable) () -> {
                getCurrentlyLoggingInUser();
            }, SPLASH_TIME_OUT);

        }
    }

    private void configureProgressBar() {
        progressBar = findViewById(R.id.progress_bar);
    }

    private void getCurrentlyLoggingInUser() {
        UserCredentialsTask userCredentialsTask = new UserCredentialsTask();
        userCredentialsTask.execute();
    }


    private class UserCredentialsTask extends AsyncTask<Void, Void, User> {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected User doInBackground(Void... voids) {

            Path userMetaDataPath = Paths.get(
                    String.valueOf(getExternalFilesDir(null)),
                    FileEnum.USER_DIRECTORY.key,
                    FileEnum.META_DATA_FILE.key);

            File userMetaDataFile = FileHelper.getFileAtPath(userMetaDataPath);

            if (userMetaDataFile != null) {
                String contentMetaData = FileHelper.readFile(userMetaDataFile);
                JSONObject jsonMetaData = JSONHelper.createJSONFromString(contentMetaData);
                try {
                    User user = new User(
                            UUID.fromString((String) jsonMetaData.get(JSONEnum.USER_UUID_KEY.key)),
                            (String) jsonMetaData.get(JSONEnum.USER_NAME_KEY.key));
                    return user;
                } catch (JSONException e) {
                    return null;
                }
            }
            return null;

        }

        protected void onPostExecute(User result) {


            if (result != null) {
                Toast.makeText(SplashActivity.this,
                        getString(R.string.toast_logging_in_user) + result.getUsername(),
                        Toast.LENGTH_LONG)
                        .show();
                Intent startMainActivity = new Intent(
                        SplashActivity.this,
                        MainActivity.class);
                // Passing User to MainActivity
                startActivity(startMainActivity);
                SplashActivity.this.finish();
            } else {
                Toast.makeText(SplashActivity.this,
                        getString(R.string.toast_no_username_found),
                        Toast.LENGTH_LONG)
                        .show();

                progressBar.setVisibility(View.INVISIBLE);

                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .add(R.id.fragment_container_view, SplashLoggingFragment.class, null)
                        .commit();

            }
        }
    }
}