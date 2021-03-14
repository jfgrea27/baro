package com.baro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;

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
        accountButton =findViewById(R.id.btn_account);

        accountButton.setOnClickListener(v -> {
            // TODO complete this method
        });
    }

    private void configureCreateButton() {
        createButton =findViewById(R.id.btn_create);

        createButton.setOnClickListener(v -> {
            // TODO complete this method
        });
    }

    private void configureShareButton() {
        shareButton = findViewById(R.id.btn_share);

        shareButton.setOnClickListener(v -> {
            // TODO complete this method
        });
    }

}