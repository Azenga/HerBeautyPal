package com.example.mercie.example;

import android.app.ProgressDialog;
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
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginShopActivity extends AppCompatActivity {

    private ProgressDialog progressdialog;
    private TextInputEditText emailTIET, pwdTIET;

    //Firebase Variables
    private FirebaseAuth mAuth;
    private FirebaseFirestore mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_shop);

        progressdialog = new ProgressDialog(this);

        //Init Firebase Variables
        mAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();

        Button shopLoginBtn = findViewById(R.id.shop_login_btn);
        shopLoginBtn.setOnClickListener(view -> signUser());

        TextView shopSignupTV = findViewById(R.id.shop_signup_tv);
        shopSignupTV.setOnClickListener(view -> startActivity(new Intent(this, SignUpShopActivity.class)));

        //TextInputEditTexts
        emailTIET = findViewById(R.id.shop_login_email_tiet);
        pwdTIET = findViewById(R.id.shop_login_pwd_tiet);
    }

    private void signUser() {

        String email = emailTIET.getText().toString();
        String pwd = pwdTIET.getText().toString();

        if (TextUtils.isEmpty(email)) {
            emailTIET.setError("Email is required");
            emailTIET.requestFocus();
            return;
        } else if (TextUtils.isEmpty(pwd)) {
            pwdTIET.setError("Password is required");
            pwdTIET.requestFocus();
            return;
        }

        progressdialog.setTitle("Attempting Signin");
        progressdialog.setMessage("Please wait...");
        progressdialog.setCanceledOnTouchOutside(true);
        progressdialog.show();

        mAuth.signInWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                confirmShopOwner(mAuth.getCurrentUser().getUid());
                            } else {
                                Toast.makeText(this, "Signin error: " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                progressdialog.dismiss();
                            }
                        }
                );

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            progressdialog.setTitle("Authenticating");
            progressdialog.setMessage("Please wait...");
            progressdialog.setCanceledOnTouchOutside(true);
            progressdialog.show();
            confirmShopOwner(user.getUid());
        }
    }

    private void confirmShopOwner(String uid) {

        mDb.collection("shops")
                .document(uid)
                .get()
                .addOnCompleteListener(

                        task -> {
                            if (task.isSuccessful()) {
                                if (task.getResult().exists()) {
                                    startActivity(new Intent(this, BeautyShopDashboardActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(this, "Use the correct group to sign in", Toast.LENGTH_SHORT).show();
                                }
                            } else {

                                Toast.makeText(this, "Error: " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                            }

                            progressdialog.dismiss();
                        }

                );

    }
}
