package com.example.mercie.example;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mercie.example.models.Client;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RegisterActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView chooseImageIV;
    private EditText nameET, phoneNumberET, genderET, locationAddressET;
    private Uri mImageUri;
    private FirebaseAuth mAuth;

    private StorageReference mStorageRef;
    private FirebaseFirestore mStoreDb;
    private String userUid;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mStoreDb = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference().child("avatars");

        nameET = findViewById(R.id.name_tv);
        phoneNumberET = findViewById(R.id.phone_et);
        genderET = findViewById(R.id.gender_et);
        locationAddressET = findViewById(R.id.address_et);

        progressDialog = new ProgressDialog(this);

        chooseImageIV = findViewById(R.id.avatar_iv);
        chooseImageIV.setOnClickListener(view -> openFileChooser());

        Button upload = findViewById(R.id.register_btn);
        upload.setOnClickListener(view -> uploadFile());
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK &&
                data != null && data.getData() != null) {
            mImageUri = data.getData();
            chooseImageIV.setImageURI(mImageUri);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver resolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(resolver.getType(uri));
    }

    public void uploadFile() {
        String name = nameET.getText().toString().trim();
        String phone = phoneNumberET.getText().toString().trim();
        String location = locationAddressET.getText().toString().trim();
        String gender = genderET.getText().toString().trim();

        progressDialog.setTitle("Setup Client");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();

        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(userUid + '.' + getFileExtension(mImageUri));

            fileReference.putFile(mImageUri)
                    .addOnCompleteListener(
                            task -> {
                                if (task.isSuccessful()) {
                                    String imageUrl = task.getResult().getMetadata().getName();
                                    Client userModel = new Client(name, phone, location, gender, imageUrl);

                                    addClientToFirestore(userModel);
                                } else {
                                    Toast.makeText(this, "An error occurred: " + task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                    );

        } else {
            Toast.makeText(this, "No File Selected", Toast.LENGTH_SHORT).show();

            Client client = new Client(name, phone, location, gender, null);
            addClientToFirestore(client);
        }

    }

    private void addClientToFirestore(Client userModel) {
        mStoreDb.collection("clients")
                .document(userUid)
                .set(userModel)
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Your profile has been updated", Toast.LENGTH_SHORT).show();

                                Thread thread = new Thread() {
                                    @Override
                                    public void run() {
                                        try {
                                            sleep(2000);
                                            startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                                            finish();
                                            super.run();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                thread.start();
                            } else {
                                Toast.makeText(this, "Update details error: " + task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();

                            }

                        }
                );
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(this, SigninAsActivity.class));
            finish();
        } else {
            userUid = user.getUid();
        }
    }
}
