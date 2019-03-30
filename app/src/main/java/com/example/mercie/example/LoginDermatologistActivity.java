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

public class LoginDermatologistActivity extends AppCompatActivity {

    private Button loginDermatologistBtn;
    private TextView signupDermatologistBtn;

    private TextInputEditText emailTIET, pwdTIET;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mDb;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_dermatologist);

        //Init firebase Variables
        mAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();

        loginDermatologistBtn = findViewById(R.id.logindermatologist);
        signupDermatologistBtn = findViewById(R.id.signupdermatologist);

        progressDialog = new ProgressDialog(this);

        emailTIET = findViewById(R.id.ld_email_tiet);
        pwdTIET = findViewById(R.id.ld_pwd_et);

        signupDermatologistBtn.setOnClickListener(view -> startActivity(new Intent(this, SignUpDermatologistActivity.class)));
        loginDermatologistBtn.setOnClickListener(
                view -> {
                    String email = emailTIET.getText().toString();
                    String pwd = pwdTIET.getText().toString();

                    if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pwd)) {

                        progressDialog.setTitle("Attempting Login");
                        progressDialog.setMessage("Please Wait...");
                        progressDialog.setCanceledOnTouchOutside(true);
                        progressDialog.show();

                        mAuth.signInWithEmailAndPassword(email, pwd)
                                .addOnCompleteListener(
                                        task -> {
                                            if (task.isSuccessful()) {
                                                confirmDermatologist(mAuth.getCurrentUser().getUid());
                                            } else {
                                                progressDialog.dismiss();
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


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            progressDialog.setTitle("Authenticating");
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.show();

            confirmDermatologist(user.getUid());
        }
    }

    private void confirmDermatologist(String uid) {

        mDb.collection("dermatologists")
                .document(uid)
                .get()
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {

                                if (task.getResult().exists()) {
                                    startActivity(new Intent(this, DermatologistHomeActivity.class));
                                    finish();

                                } else {
                                    Toast.makeText(this, "Attempt login with the correct group", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(this, "Error: " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }

                            progressDialog.dismiss();
                        }
                );

    }

}
