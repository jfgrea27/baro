package com.baro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageButton;

public class AccountActivity extends AppCompatActivity {

    private ImageButton accountButton;
    private ImageButton followersButton;
    private ImageButton settingsButton;
    private ImageButton deleteLocalButton;
    private ImageButton deleteRemoteButton;
    private RecyclerView courseRecycleView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        configureAccountButton();
        configureFollowersButton();
        configureSettingsButton();
        configureDeleteLocalButton();
        configureDeleteRemoteButton();
        configureRecycleView();
    }

    private void configureAccountButton() {
        accountButton = findViewById(R.id.btn_account);

        accountButton.setOnClickListener(v -> {
            // TODO complete this method
            // Should allow user to change their picture
        });
    }


    private void configureFollowersButton() {
        followersButton = findViewById(R.id.btn_followers);

        followersButton.setOnClickListener(v -> {
            // TODO complete this method
            // Should allow user to see their followers
        });
    }


    private void configureSettingsButton() {
        settingsButton = findViewById(R.id.btn_settings);

        settingsButton.setOnClickListener(v -> {
            // TODO complete this method
            // Should allow user to enter the settings page
        });
    }


    private void configureDeleteLocalButton() {
        deleteLocalButton = findViewById(R.id.btn_delete_local);

        deleteLocalButton.setOnClickListener(v -> {
            // TODO complete this method
            // Should allow user to delete course from local
        });
    }

    private void configureDeleteRemoteButton() {
        deleteRemoteButton = findViewById(R.id.btn_delete_remote);

        deleteRemoteButton.setOnClickListener(v -> {
            // TODO complete this method
            // Should allow user to delete course from remote
        });
    }

    private void configure() {
        accountButton = findViewById(R.id.btn_account);

        accountButton.setOnClickListener(v -> {
            // TODO complete this method
            // Should allow user to change their picture
        });
    }


    private void configureRecycleView() {
        // TODO Implement this method
    }

}