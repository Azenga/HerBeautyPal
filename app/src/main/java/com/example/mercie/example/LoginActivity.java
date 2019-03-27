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

public class LoginActivity extends AppCompatActivity {

    //ADDED
    private TextInputEditText emailTIET, pwdTIET;

    private ProgressDialog progressDialog;

    //Firebase Variables
    private FirebaseAuth mAuth;
    private FirebaseFirestore mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Button login = findViewById(R.id.login);

        //ADDED
        emailTIET = findViewById(R.id.email_tiet);
        pwdTIET = findViewById(R.id.pwd_tiet);

        progressDialog = new ProgressDialog(this);

        //Initializing Firebse Variables
        mAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();


        login.setOnClickListener(
                v -> {

                    String email = emailTIET.getText().toString();
                    String pwd = pwdTIET.getText().toString();

                    if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pwd)) {

                        progressDialog.setTitle("Attempting Login");
                        progressDialog.setMessage("Please wait...");
                        progressDialog.setCanceledOnTouchOutside(true);
                        progressDialog.show();

                        mAuth.signInWithEmailAndPassword(email, pwd).
                                addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {

                                        //Check whether the user is a client first before logging him/her in
                                        checkWhetherUserIsAclient(mAuth.getCurrentUser().getUid());

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

    public void checkWhetherUserIsAclient(String userId) {

        mDb.collection("clients")
                .document(userId)
                .get()
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {

                                if (task.getResult().exists()) {

                                    startActivity(new Intent(this, HomeActivity.class));
                                    finish();

                                } else {
                                    Toast.makeText(this, "You are not a client choose a relevant group", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(this, "Login error: " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
            checkWhetherUserIsAclient(user.getUid());

        }
    }
}
