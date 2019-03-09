package com.example.mercie.example;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class LoginShopActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_shop);

        Button loginshop = findViewById(R.id.loginshop);
        TextView signupshop = findViewById(R.id.signupshop);
        signupshop.setOnClickListener(
                view -> {
                    startActivity(new Intent(this, SignUpShopActivity.class));
                    finish();
                });
        loginshop.setOnClickListener(
                view -> {
                    startActivity(new Intent(this, HomeActivity.class));
                    finish();
                });
    }
}
