package com.example.mercie.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mercie.example.models.Notification;

public class NotificationResponseActivity extends AppCompatActivity {

    private Notification notification = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_response);

        if (getIntent() != null)
            notification = (Notification) getIntent().getSerializableExtra("notification");


    }
}
