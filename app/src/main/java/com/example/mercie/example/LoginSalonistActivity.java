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

public class LoginSalonistActivity extends AppCompatActivity {

    //Firebase variables
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private TextInputEditText emailTIET, pwdTIET;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_salonist);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        Button loginsalonist = findViewById(R.id.loginsalonist);
        TextView signupsalonist = findViewById(R.id.signupsalonist);

        emailTIET = findViewById(R.id.salonist_email_tiet);
        pwdTIET = findViewById(R.id.salonist_password_tiet);

        progressDialog = new ProgressDialog(this);

        signupsalonist.setOnClickListener(
                view -> {
                    startActivity(new Intent(this, SignUpSalonistActivity.class));
                    finish();
                });

        loginsalonist.setOnClickListener(view -> signInSalonist());
    }

    private void signInSalonist() {

        String email = emailTIET.getText().toString();
        String pwd = pwdTIET.getText().toString();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pwd)) {
            progressDialog.setTitle("Attempting Login");
            progressDialog.setMessage("Please wait...");
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener(
                            task -> {
                                if (task.isSuccessful()) {

                                    checkWhetherUserReallyASaloonist(mAuth.getCurrentUser().getUid());

                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(this, "Login error: " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                                }
                            }
                    );
        } else {
            Toast.makeText(this, "Fill in all the fields", Toast.LENGTH_SHORT).show();
        }
    }

    public void checkWhetherUserReallyASaloonist(String userId) {
        mFirestore.collection("salonists")
                .document(userId)
                .get()
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {

                                if (task.getResult().exists()) {
                                    startActivity(new Intent(this, SalonistDashboardActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(this, "You are not a Saloonist", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(this, "Login error: " + task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }
                            progressDialog.dismiss();
                        }
                );
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {

            progressDialog.setTitle("Authenticating");
            progressDialog.setMessage("Please wait...");
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.show();

            checkWhetherUserReallyASaloonist(user.getUid());
        }
    }
}
