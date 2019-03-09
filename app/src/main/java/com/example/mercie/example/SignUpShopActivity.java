package com.example.mercie.example;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpShopActivity extends AppCompatActivity {


    private Button sigupshop;
    private EditText shopemail;
    private EditText shoppassword;
    private EditText shopconfirmpassword;
    private FirebaseAuth mAuth;
    private ProgressDialog progressdialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_shop);

        mAuth = FirebaseAuth.getInstance();

        progressdialog = new ProgressDialog(SignUpShopActivity.this);

        shopemail = findViewById(R.id.shopemail);
        shoppassword = findViewById(R.id.shoppassword);
        shopconfirmpassword = findViewById(R.id.shopconfirmpassword);

        sigupshop = findViewById(R.id.sigupshop);
        sigupshop.setOnClickListener(view -> createUserAccount());
    }

    private void createUserAccount() {

        String email = shopemail.getText().toString();
        String password = shoppassword.getText().toString();
        String confirmpassword = shopconfirmpassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            shopemail.setError("Email Field must not be empty");
        } else if (TextUtils.isEmpty(password)) {
            shoppassword.setError("Password Field must not be empty");
        } else if (TextUtils.isEmpty(confirmpassword)) {
            shopconfirmpassword.setError("Confirm password Field must not be empty");
        } else if (!password.equals(confirmpassword)) {
            Toast.makeText(this, "passwords do not match", Toast.LENGTH_SHORT).show();
        } else {

            progressdialog.setTitle("Creating Account");
            progressdialog.setMessage("Please Wait...");
            progressdialog.setCanceledOnTouchOutside(true);
            progressdialog.show();
            mAuth.createUserWithEmailAndPassword(email, password).
                    addOnCompleteListener(
                            task -> {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(SignUpShopActivity.this, RegisterActivity.class);
                                    intent.putExtra("GROUP", "shops");
                                    startActivity(intent);
                                    finish();
                                } else {
                                    String message = task.getException().getMessage();
                                    Toast.makeText(SignUpShopActivity.this, "Something went wrong: " + message, Toast.LENGTH_SHORT).show();
                                }
                                progressdialog.dismiss();
                            }
                    );

        }
    }
}
