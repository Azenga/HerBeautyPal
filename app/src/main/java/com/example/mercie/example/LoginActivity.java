package com.example.mercie.example;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    //ADDED
    private TextInputEditText emailTIET, pwdTIET;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        Button login = findViewById(R.id.login);

        //ADDED
        emailTIET = findViewById(R.id.email_tiet);
        pwdTIET = findViewById(R.id.pwd_tiet);
        mAuth = FirebaseAuth.getInstance();


        login.setOnClickListener(
                v -> {

                    String email = emailTIET.getText().toString();
                    String pwd = pwdTIET.getText().toString();

                    if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pwd)) {
                        mAuth.signInWithEmailAndPassword(email, pwd).
                                addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(this, HomeActivity.class));
                                    } else {
                                        Toast.makeText(this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(this, "Fill in all the fields", Toast.LENGTH_SHORT).show();
                    }

                });


        TextView signup = findViewById(R.id.signup);
        signup.setOnClickListener(
                view -> {
                    startActivity(new Intent(this, SignupActivity.class));
                    finish();
                }
        );

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            startActivity(new Intent(this, HomeActivity.class));
        }
    }
}
