package com.baro;

import android.Manifest;
import android.app.Activity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.material.snackbar.Snackbar;

public class Permissions implements ActivityCompat.OnRequestPermissionsResultCallback {

    private final Activity activity;

    public Permissions(Activity activity) {
        this.activity = activity;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }
}
