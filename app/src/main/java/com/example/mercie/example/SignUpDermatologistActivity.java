package com.example.mercie.example;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SignUpDermatologistActivity extends AppCompatActivity {
    private Button sigupdermatologist;
    private EditText dermaemail;
    private EditText dermapassword;
    private EditText dermaconfirmpassword;
    private FirebaseAuth mAuth;
    private ProgressDialog progressdialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_dermatologist);

        sigupdermatologist = findViewById(R.id.sigupdermatologist);
        mAuth = FirebaseAuth.getInstance();

        progressdialog = new ProgressDialog(SignUpDermatologistActivity.this);
        sigupdermatologist.setOnClickListener(view -> createUserAccount());

        dermaemail = findViewById(R.id.dermaemail);
        dermapassword = findViewById(R.id.dermapassword);
        dermaconfirmpassword = findViewById(R.id.dermaconfirmpassword);

    }

    private void createUserAccount() {

        String email = dermaemail.getText().toString();
        String password = dermapassword.getText().toString();
        String confirmpassword = dermaconfirmpassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            dermaemail.setError("Email Field must not be empty");
        } else if (TextUtils.isEmpty(password)) {
            dermapassword.setError("Password Field must not be empty");
        } else if (TextUtils.isEmpty(confirmpassword)) {
            dermaconfirmpassword.setError("Confirm password Field must not be empty");
        } else if (!password.equals(confirmpassword)) {
            Toast.makeText(this, "passwords do not match", Toast.LENGTH_SHORT).show();
        } else {
            progressdialog.setTitle("Creating Account");
            progressdialog.setMessage("Please Wait...");
            progressdialog.setCanceledOnTouchOutside(true);
            progressdialog.show();
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                    task -> {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(this, RegisterActivity.class);
                            intent.putExtra("GROUP", "dermatologists");
                            startActivity(intent);
                            finish();
                        } else {
                            String message = task.getException().getMessage();
                            Toast.makeText(SignUpDermatologistActivity.this, "Something went wrong: " + message, Toast.LENGTH_SHORT).show();
                        }
                        progressdialog.dismiss();
                    }
            );

        }
    }
}
