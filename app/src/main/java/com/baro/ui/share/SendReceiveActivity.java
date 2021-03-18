package com.baro.ui.share;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import com.baro.R;

public class SendReceiveActivity extends AppCompatActivity {

    private ImageButton sendButton;
    private ImageButton receiveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_receive);

        configureSendButton();
        configureReceiveButton();
    }



    private void configureSendButton() {
        sendButton = findViewById(R.id.btn_send);

        sendButton.setOnClickListener(v -> {
            Intent intentSendActivity = new Intent(SendReceiveActivity.this,
                    BluetoothSendActivity.class);
            startActivity(intentSendActivity);
        });
    }

    private void configureReceiveButton() {
        receiveButton = findViewById(R.id.btn_receive);

        receiveButton.setOnClickListener(v -> {
            Intent intentReceiveActivity = new Intent(SendReceiveActivity.this,
                    BluetoothReceiveActivity.class);
            startActivity(intentReceiveActivity);
        });
    }
}