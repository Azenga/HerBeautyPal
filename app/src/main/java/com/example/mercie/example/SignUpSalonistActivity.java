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

public class SignUpSalonistActivity extends AppCompatActivity {

    private EditText salonistemailET, salonistpasswordET, salonistconfirmpasswordET;
    private FirebaseAuth mAuth;
    private ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_salonist);

        mAuth = FirebaseAuth.getInstance();
        progressdialog = new ProgressDialog(SignUpSalonistActivity.this);

        Button sigupsalonist = findViewById(R.id.sigupsalonist_btn);
        sigupsalonist.setOnClickListener(view -> createUserAccount());

        salonistemailET = findViewById(R.id.salonistemail);
        salonistpasswordET = findViewById(R.id.salonistpassword);
        salonistconfirmpasswordET = findViewById(R.id.salonistconfirmpassword);

    }

    private void createUserAccount() {

        String email = salonistemailET.getText().toString();
        String password = salonistpasswordET.getText().toString();
        String confirmpassword = salonistconfirmpasswordET.getText().toString();

        if (TextUtils.isEmpty(email)) {
            salonistemailET.setError("Email Field must not be empty");
        } else if (TextUtils.isEmpty(password)) {
            salonistpasswordET.setError("Password Field must not be empty");
        } else if (TextUtils.isEmpty(confirmpassword)) {
            salonistconfirmpasswordET.setError("Confirm password Field must not be empty");
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
                            startActivity(new Intent(this, SetupSalonistActivity.class));
                        } else {
                            String message = task.getException().getMessage();
                            Toast.makeText(this, "Something went wrong: " + message, Toast.LENGTH_SHORT).show();
                        }
                        progressdialog.dismiss();
                    }
            );

        }
    }
}
