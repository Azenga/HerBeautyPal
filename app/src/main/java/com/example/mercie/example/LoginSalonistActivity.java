package com.example.mercie.example;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class LoginSalonistActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_salonist);

        Button loginsalonist = findViewById(R.id.loginsalonist);
        TextView signupsalonist = findViewById(R.id.signupsalonist);

        signupsalonist.setOnClickListener(
                view -> {
                    startActivity(new Intent(this, SignUpSalonistActivity.class));
                    finish();
                });

        loginsalonist.setOnClickListener(
                view -> {
                    startActivity(new Intent(this, SalonistDashboardActivity.class));
                    finish();
                });
    }
}
