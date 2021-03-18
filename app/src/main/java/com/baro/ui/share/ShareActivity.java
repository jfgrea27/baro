package com.baro.ui.share;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.baro.R;

public class ShareActivity extends AppCompatActivity {

    private ImageButton internetButton;
    private ImageButton localButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        configureInternetButton();
        configureLocalButton();
    }



    private void configureInternetButton() {
        internetButton = findViewById(R.id.btn_internet);

        internetButton.setOnClickListener(v -> {
            Intent intentUploadActivity = new Intent(ShareActivity.this,
                    UploadActivity.class);
            startActivity(intentUploadActivity);
        });
    }

    private void configureLocalButton() {
        localButton = findViewById(R.id.btn_local);

        localButton.setOnClickListener(v -> {
            Intent intentSendReceiveActivity = new Intent(ShareActivity.this,
                    SendReceiveActivity.class);
            startActivity(intentSendReceiveActivity);
        });
    }
}