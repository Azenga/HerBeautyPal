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

        Button derma, client, owner, salonist;

        client = findViewById(R.id.client);
        client.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        salonist = findViewById(R.id.salonist);
        salonist.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginSalonistActivity.class));
            finish();
        });

        owner = findViewById(R.id.owner);
        owner.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginShopActivity.class));
            finish();
        });

        derma = findViewById(R.id.derma);
        derma.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginDermatologistActivity.class));
            finish();
        });
    }
}
