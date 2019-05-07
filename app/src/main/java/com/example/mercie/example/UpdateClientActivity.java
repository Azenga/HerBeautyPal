package com.example.mercie.example;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mercie.example.models.Client;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateClientActivity extends AppCompatActivity {

    private static final String TAG = "UpdateClientActivity";

    public static final int RC_CHOOSE_IMAGE = 999;
    public static final int RC_CROP_IMAGE = 998;

    private EditText nameET, locationET, contactET;
    private CircleImageView profilePicCIV;
    private Button changeProfilePicButton, updateBtn;

    private boolean imageChanged = false;

    private Toolbar toolbar;


    private Client mCurrentClient = null;


    private FirebaseAuth mAuth;
    private FirebaseFirestore mDb;
    private StorageReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_client);

        initWidgets();

        if (getSupportActionBar() == null) setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Edit Profile");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        changeProfilePicButton.setOnClickListener(
                view -> {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, RC_CHOOSE_IMAGE);
                }
        );

        mAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();
        mRef = FirebaseStorage.getInstance().getReference("avatars");


        updateBtn.setOnClickListener(view -> updateProfile());
    }

    private void updateProfile() {
        String name = String.valueOf(nameET.getText());
        String location = String.valueOf(locationET.getText());
        String contact = String.valueOf(contactET.getText());

        ProgressDialog progressDialog = ProgressDialog.show(this, "Updating profile", "Please wait...");
        if (imageChanged) {

            Bitmap bitmap = ((BitmapDrawable) profilePicCIV.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            byte[] bytes = baos.toByteArray();

            StorageReference profilePicRef = mRef.child(mAuth.getCurrentUser().getUid() + ".jpg");

            profilePicRef.putBytes(bytes)
                    .addOnSuccessListener(
                            taskSnapshot -> {
                                String imageName = taskSnapshot.getMetadata().getName();
                                mCurrentClient.setImageUrl(imageName);
                            }
                    )
                    .addOnFailureListener(
                            e -> {
                                Toast.makeText(this, "Uploading Image Failed", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "updateProfile: Uploading Image", e);
                            }
                    );

        }

        if (containEmptyFields()) return;

        mCurrentClient.setName(name.trim());
        mCurrentClient.setAddress(location.trim());
        mCurrentClient.setContact(contact.trim());

        mDb.document("clients/" + mAuth.getCurrentUser().getUid())
                .set(mCurrentClient)
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(this, HomeActivity.class));
                            } else {
                                Toast.makeText(this, "Upload Failed", Toast.LENGTH_SHORT).show();
                            }

                            progressDialog.dismiss();
                        }
                );


    }

    private boolean containEmptyFields() {
        if (TextUtils.isEmpty(String.valueOf(nameET.getText()))) {
            nameET.setError("Name is required");
            nameET.requestFocus();
            return true;
        } else if (TextUtils.isEmpty(String.valueOf(locationET.getText()))) {
            locationET.setError("Location is required");
            locationET.requestFocus();
            return true;
        } else if (TextUtils.isEmpty(String.valueOf(contactET.getText()))) {
            contactET.setError("Contact is required");
            contactET.requestFocus();
            return true;
        } else return false;
    }

    private void initWidgets() {
        //Toolbar
        toolbar = findViewById(R.id.toolbar);

        //EditText
        nameET = findViewById(R.id.name_et);
        locationET = findViewById(R.id.location_et);
        contactET = findViewById(R.id.contact_et);

        //CircleImageView
        profilePicCIV = findViewById(R.id.profile_pic_civ);

        //Button
        changeProfilePicButton = findViewById(R.id.change_profile_pic_btn);
        updateBtn = findViewById(R.id.update_btn);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case RC_CHOOSE_IMAGE:
                    openCroppingActivity(data.getData());
                    break;

                case RC_CROP_IMAGE:
                    populateCIV(data);
                    break;

                default:
                    super.onActivityResult(requestCode, resultCode, data);

            }
        }
    }

    private void populateCIV(Intent intent) {
        Bundle bundle = intent.getExtras();

        Bitmap bitmap = bundle.getParcelable("data");

        if (bitmap != null) profilePicCIV.setImageBitmap(bitmap);

        imageChanged = true;
    }

    private void openCroppingActivity(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        intent.putExtra("crop", true);
        intent.putExtra("outputX", 256);
        intent.putExtra("outputY", 256);
        intent.putExtra("aspectX", true);
        intent.putExtra("aspectY", true);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);

        PackageManager packageManager = getPackageManager();

        List<ResolveInfo> apps = packageManager.queryIntentActivities(intent, 0);

        if (apps.size() > 0) startActivityForResult(intent, RC_CROP_IMAGE);
        else
            Toast.makeText(this, "Please, Installing a cropping activity", Toast.LENGTH_SHORT).show();

    }


    private void updateUI() {
        if (mCurrentClient != null) {
            nameET.setText(mCurrentClient.getName());
            locationET.setText(mCurrentClient.getAddress());
            contactET.setText(mCurrentClient.getContact());

            if (mCurrentClient.getImageUrl() != null) {
                StorageReference profilePicRef = mRef.child(mCurrentClient.getImageUrl());
                final long MB = 1024 * 1024;

                profilePicRef.getBytes(MB)
                        .addOnSuccessListener(
                                bytes -> {
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    profilePicCIV.setImageBitmap(bitmap);
                                }
                        )
                        .addOnFailureListener(
                                e -> {
                                    Log.e(TAG, "updateUI: Getting Image", e);
                                    Toast.makeText(this, "Error getting Image", Toast.LENGTH_SHORT).show();
                                }
                        );
            }
        } else {
            Toast.makeText(this, "Empty profile", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(this, SigninAsActivity.class));
            finish();
        } else {
            getClient(user.getUid());
        }
    }

    private void getClient(String uid) {
        mDb.document("clients/" + uid)
                .get()
                .addOnSuccessListener(
                        documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                Client client = documentSnapshot.toObject(Client.class);
                                mCurrentClient = client;

                                updateUI();
                            }
                        }
                )
                .addOnFailureListener(
                        e -> Log.e(TAG, "getClient: Error", e)
                );
    }


}
