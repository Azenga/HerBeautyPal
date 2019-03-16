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
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupActivity extends AppCompatActivity {

    private EditText useremail, userpassword, userconfirmpassword;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mStoreDb;

    private ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        mStoreDb = FirebaseFirestore.getInstance();

        useremail = findViewById(R.id.useremail);
        userpassword = findViewById(R.id.userpassword);
        userconfirmpassword = findViewById(R.id.userconfirmpassword);
        progressdialog = new ProgressDialog(this);

        Button signup = findViewById(R.id.sigup);
        signup.setOnClickListener(view -> createUserAccount());


    }

    private void createUserAccount() {

        String email = useremail.getText().toString();
        String password = userpassword.getText().toString();
        String confirmpassword = userconfirmpassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            useremail.setError("Email Field must not be empty");
        } else if (TextUtils.isEmpty(password)) {
            userpassword.setError("Password Field must not be empty");
        } else if (TextUtils.isEmpty(confirmpassword)) {
            userconfirmpassword.setError("Confirm password Field must not be empty");
        } else if (!password.equals(confirmpassword)) {
            Toast.makeText(this, "passwords do not match", Toast.LENGTH_SHORT).show();
        } else {
            progressdialog.setTitle("Creating Account");
            progressdialog.setMessage("Please Wait...");
            progressdialog.setCanceledOnTouchOutside(true);
            progressdialog.show();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(
                            task -> {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(this, RegisterActivity.class);
                                    startActivity(intent);
                                    finish();
                                    progressdialog.dismiss();
                                } else {

                                    String message = task.getException().getLocalizedMessage();

                                    Toast.makeText(this, "Error creating account: " + message, Toast.LENGTH_LONG).show();

                                    progressdialog.dismiss();

                                }
                            }
                    );

        }
    }
}
