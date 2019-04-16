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
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mercie.example.models.Salonist;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateSalonActivity extends AppCompatActivity {
    private static final String TAG = "UpdateSalonActivity";

    //REQUEST CODES
    public static final int RC_CHOOSE_IMAGE = 22;
    public static final int RC_CROP_IMAGE = 10;

    //Widgets
    private EditText nameET, locationET, contactET, websiteET;
    private CircleImageView profileCIV;
    private Button changeImgBtn, editBtn;
    private ProgressDialog progressDialog;

    private Salonist salonist = null;
    private boolean imageSelected = false;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mDb;
    private StorageReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_salon);

        initWidgets();

        //Init Firebase Variables
        mAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();
        mRef = FirebaseStorage.getInstance().getReference().child("avatars");

        changeImgBtn.setOnClickListener(v -> openChooseImageActivity());
        editBtn.setOnClickListener(v -> uploadProfilePicture());

    }

    private void initWidgets() {
        nameET = findViewById(R.id.name_et);
        locationET = findViewById(R.id.location_et);
        contactET = findViewById(R.id.contact_et);
        websiteET = findViewById(R.id.website_et);

        profileCIV = findViewById(R.id.profile_civ);

        changeImgBtn = findViewById(R.id.change_img_btn);
        editBtn = findViewById(R.id.edit_btn);

        progressDialog = new ProgressDialog(this);
    }


    private void uploadProfilePicture() {

        progressDialog.setTitle("Updating Profile");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();

        if (imageSelected) {

            Bitmap bitmap = ((BitmapDrawable) profileCIV.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            byte[] bytes = baos.toByteArray();

            if (mAuth.getCurrentUser() != null) {
                StorageReference profilePicRef = mRef.child(mAuth.getCurrentUser().getUid() + ".jpg");

                UploadTask profilePicUploadTask = profilePicRef.putBytes(bytes);

                profilePicUploadTask
                        .addOnSuccessListener(
                                taskSnapshot -> {
                                    String imageName = taskSnapshot.getMetadata().getName();
                                    updateSalonistInfo(imageName);
                                }
                        )
                        .addOnFailureListener(e -> Log.e(TAG, "updateSalonistInfo: Profile Picturee", e));

            }
        } else {
            updateSalonistInfo(null);
        }


    }

    private void updateSalonistInfo(String imageName) {

        String name = nameET.getText().toString().trim();
        String location = locationET.getText().toString().trim();
        String contact = contactET.getText().toString().trim();
        String website = websiteET.getText().toString().trim();

        if (!emptyRequiredFields()) {

            salonist.setName(name);
            salonist.setLocation(location);
            salonist.setContact(contact);
            salonist.setWebsite(website);
            salonist.setProfilePicName(imageName);

            mDb.collection("salonists")
                    .document(mAuth.getCurrentUser().getUid())
                    .set(salonist)
                    .addOnSuccessListener(
                            aVoid -> {
                                Toast.makeText(this, "Salonist profile successfully updated", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(this, SalonistDashboardActivity.class));
                                progressDialog.dismiss();
                            }
                    )
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Update Failed try again after some time", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "updateSalonistInfo: Sending to firestore", e);
                        progressDialog.dismiss();
                    });

        }
    }

    private boolean emptyRequiredFields() {

        if (nameET.getText().toString().trim().isEmpty()) {
            nameET.requestFocus();
            nameET.setError("Name is Required");
            return true;
        } else if (locationET.getText().toString().trim().isEmpty()) {
            locationET.requestFocus();
            locationET.setError("Location is Required");
            return true;
        } else if (contactET.getText().toString().trim().isEmpty()) {
            contactET.requestFocus();
            contactET.setError("Location is Required");
            return true;
        } else {
            return false;
        }

    }

    private void openChooseImageActivity() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, RC_CHOOSE_IMAGE);
    }

    private void updateUI(Salonist salonist) {

        nameET.setText(salonist.getName());
        locationET.setText(salonist.getLocation());
        contactET.setText(salonist.getContact());
        websiteET.setText(salonist.getWebsite());

        if (salonist.getProfilePicName() != null) {
            StorageReference profilePicRef = mRef.child(salonist.getProfilePicName());
            final long MB = 1024 * 1024;
            profilePicRef.getBytes(MB)
                    .addOnSuccessListener(bytes -> {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        profileCIV.setImageBitmap(bitmap);

                    })
                    .addOnFailureListener(e -> Log.e(TAG, "updateUI: Image Failure", e));
        }

    }

    private void getSalonist(String uid) {

        mDb.collection("salonists")
                .document(uid)
                .get()
                .addOnSuccessListener(
                        documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                Salonist salonist = documentSnapshot.toObject(Salonist.class);

                                this.salonist = salonist;

                                if (salonist != null) updateUI(salonist);
                            } else {
                                Toast.makeText(this, "No details", Toast.LENGTH_SHORT).show();
                            }
                        }
                )
                .addOnFailureListener(e -> Log.w(TAG, "getSalonist: Failure", e));

    }

    private void openCroppingActivity(Uri uri) {
        imageSelected = false;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        intent.putExtra("crop", true);
        intent.putExtra("outputX", 96);
        intent.putExtra("outputY", 96);
        intent.putExtra("aspectX", 96);
        intent.putExtra("aspectY", 96);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);

        PackageManager packageManager = getPackageManager();

        List<ResolveInfo> croppingApps = packageManager.queryIntentActivities(intent, 0);

        if (croppingApps.size() > 0) startActivityForResult(intent, RC_CROP_IMAGE);
        else Toast.makeText(this, "Please, Install a cropping App", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case RC_CHOOSE_IMAGE:
                    imageSelected = true;
                    profileCIV.setImageURI(data.getData());

                    Snackbar.make(findViewById(android.R.id.content), "Profile Picture Image", Snackbar.LENGTH_LONG)
                            .setAction("Crop the Image", view -> openCroppingActivity(data.getData()))
                            .show();
                    break;
                case RC_CROP_IMAGE:
                    imageSelected = true;
                    populateProfilePicture(data);
                    break;
                default:
                    super.onActivityResult(requestCode, resultCode, data);
            }
        } else {
            Toast.makeText(this, "Process Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private void populateProfilePicture(Intent data) {
        Bundle bundle = data.getExtras();
        Bitmap bitmap = bundle.getParcelable("data");
        if (bitmap != null)
            profileCIV.setImageBitmap(bitmap);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(this, SigninAsActivity.class));
            finish();
        } else {
            getSalonist(user.getUid());
        }
    }
}

