 package com.baro.ui.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import com.baro.AccountActivity;
import com.baro.ui.create.CreateActivity;
import com.baro.R;
import com.baro.ui.share.ShareActivity;

 public class MainActivity extends AppCompatActivity {

    private ImageButton accountButton;
    private ImageButton shareButton;
    private ImageButton createButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        configureAccountButton();
        configureShareButton();
        configureCreateButton();
    }


    private void configureAccountButton() {
        accountButton = findViewById(R.id.btn_account);
        accountButton.setOnClickListener(v -> {
            Intent intentAccountActivity = new Intent(
                    MainActivity.this,
                    AccountActivity.class);
            startActivity(intentAccountActivity);
        });
    }

    private void configureCreateButton() {
        createButton = findViewById(R.id.btn_create);

        createButton.setOnClickListener(v -> {
            Intent intentCreateCourse = new Intent(
                    MainActivity.this,
                    CreateActivity.class);
            startActivity(intentCreateCourse);
        });
    }

    private void configureShareButton() {
        shareButton = findViewById(R.id.btn_share);

        shareButton.setOnClickListener(v -> {
            Intent intentShareActivity = new Intent(
                    MainActivity.this,
                    ShareActivity.class);
            startActivity(intentShareActivity);
        });
    }
}