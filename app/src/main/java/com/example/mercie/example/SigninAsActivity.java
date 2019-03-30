package com.example.mercie.example;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class SigninAsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_as);

        Button dermatologistSigninBtn, clientSignInBtn, shopOwnerSigninBtn, shopSigninBtn;

        clientSignInBtn = findViewById(R.id.client);
        clientSignInBtn.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginActivity.class));
        });

        shopSigninBtn = findViewById(R.id.salonist);
        shopSigninBtn.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginSalonistActivity.class));
        });

        shopOwnerSigninBtn = findViewById(R.id.owner);
        shopOwnerSigninBtn.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginShopActivity.class));
        });

        dermatologistSigninBtn = findViewById(R.id.derma);
        dermatologistSigninBtn.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginDermatologistActivity.class));
        });
    }
}
