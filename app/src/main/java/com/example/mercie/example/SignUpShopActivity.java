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

public class SignUpShopActivity extends AppCompatActivity {


    private Button signupShopBtn;
    private EditText shopEmailTIET;
    private EditText shopPwdTIET;
    private EditText shopCpwdTIET;
    private FirebaseAuth mAuth;
    private ProgressDialog progressdialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_shop);

        mAuth = FirebaseAuth.getInstance();

        progressdialog = new ProgressDialog(this);

        shopEmailTIET = findViewById(R.id.shop_email_tiet);
        shopPwdTIET = findViewById(R.id.shop_pwd_tiet);
        shopCpwdTIET = findViewById(R.id.shop_cpwd_tiet);

        signupShopBtn = findViewById(R.id.signup_shop_btn);
        signupShopBtn.setOnClickListener(view -> createUserAccount());
    }

    private void createUserAccount() {

        String email = shopEmailTIET.getText().toString();
        String password = shopPwdTIET.getText().toString();
        String confirmpassword = shopCpwdTIET.getText().toString();

        if (TextUtils.isEmpty(email)) {
            shopEmailTIET.setError("Email Field must not be empty");
            shopEmailTIET.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            shopPwdTIET.setError("Password Field must not be empty");
            shopPwdTIET.requestFocus();
        } else if (TextUtils.isEmpty(confirmpassword)) {
            shopCpwdTIET.setError("Confirm password Field must not be empty");
            shopCpwdTIET.requestFocus();
        } else if (!password.equals(confirmpassword)) {
            Toast.makeText(this, "passwords do not match", Toast.LENGTH_SHORT).show();
            shopCpwdTIET.requestFocus();
        } else {

            progressdialog.setTitle("Creating Account");
            progressdialog.setMessage("Please Wait...");
            progressdialog.setCanceledOnTouchOutside(true);
            progressdialog.show();

            mAuth.createUserWithEmailAndPassword(email, password).
                    addOnCompleteListener(
                            task -> {
                                if (task.isSuccessful()) {

                                    Intent intent = new Intent(this, SetupShopActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    String message = task.getException().getMessage();

                                    Toast.makeText(this, "Signup error: " + message, Toast.LENGTH_LONG).show();
                                }

                                progressdialog.dismiss();
                            }
                    );

        }
    }
}
