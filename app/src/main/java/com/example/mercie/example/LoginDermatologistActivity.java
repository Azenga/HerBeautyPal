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

public class LoginDermatologistActivity extends AppCompatActivity {

    private Button logindermatologist;
    private TextView signupdermatologist;

    private TextInputEditText emailTIET, pwdTIET;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_dermatologist);

        mAuth = FirebaseAuth.getInstance();

        logindermatologist = findViewById(R.id.logindermatologist);
        signupdermatologist = findViewById(R.id.signupdermatologist);

        emailTIET = findViewById(R.id.ld_email_tiet);
        pwdTIET = findViewById(R.id.ld_pwd_et);

        signupdermatologist.setOnClickListener(view -> startActivity(new Intent(LoginDermatologistActivity.this, SignUpDermatologistActivity.class)));
        logindermatologist.setOnClickListener(
                view -> {
                    String email = emailTIET.getText().toString();
                    String pwd = pwdTIET.getText().toString();

                    if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pwd)) {
                        mAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(
                                task -> {
                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(this, HomeActivity.class));
                                    } else {
                                        Toast.makeText(this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                        );
                    } else {
                        Toast.makeText(this, "Fill in all the fields", Toast.LENGTH_SHORT).show();
                    }
                }

        );
    }
}
