package com.example.mercie.example;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RegisterActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Button upload;
    private ImageView chooseImage;
    private EditText name_et, phone_number, gender_et, location_address;
    private Uri mImageUri;
    private FirebaseAuth mAuth;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private FirebaseFirestore mStoreDb;

    String group = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        group = getIntent().getStringExtra("GROUP");

        mAuth = FirebaseAuth.getInstance();
        mStoreDb = FirebaseFirestore.getInstance();

        name_et = findViewById(R.id.name_et);
        phone_number = findViewById(R.id.phone_et);
        gender_et = findViewById(R.id.gender_et);
        location_address = findViewById(R.id.address_et);

        chooseImage = findViewById(R.id.avatar_iv);
        chooseImage.setOnClickListener(view -> openFileChooser());
        upload = findViewById(R.id.register_btn);

        mStorageRef = FirebaseStorage.getInstance().getReference().child("avatar");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child(group);

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
            chooseImage.setImageURI(mImageUri);
        }
    }

    private String getFileExtension(Uri uri) {

        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));

    }

    public void uploadFile() {
        FirebaseUser user = mAuth.getCurrentUser();
        String user_id = mAuth.getCurrentUser().getUid();

        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(user_id + '.' + getFileExtension(mImageUri));
            fileReference.putFile(mImageUri)
                    .addOnCompleteListener(
                            task -> {
                                if (task.isSuccessful()) {

                                    String name = name_et.getText().toString().trim();
                                    String phone = phone_number.getText().toString().trim();
                                    String location = location_address.getText().toString().trim();
                                    String gender = gender_et.getText().toString().trim();
                                    String imageUrl = task.getResult().getMetadata().getName();


                                    UserModel userModel = new UserModel(name, phone, location, gender, imageUrl);

                                    //mDatabaseRef.child(user.getUid()).setValue(userModel);
                                    mStoreDb.collection(group)
                                            .document(user_id)
                                            .set(userModel)
                                            .addOnCompleteListener(
                                                    task1 -> {
                                                        if (task1.isSuccessful()) {
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
                                                            Toast.makeText(this, "Update details error: " + task1.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                            );


                                } else {
                                    Toast.makeText(this, "An error occurred: " + task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                    );

        } else {
            Toast.makeText(this, "No File Selected", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(this, SigninAsActivity.class));
            finish();
        }
    }
}
