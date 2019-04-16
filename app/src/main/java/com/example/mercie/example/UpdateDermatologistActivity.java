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

import com.example.mercie.example.models.Dermatologist;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateDermatologistActivity extends AppCompatActivity {

    private static final String TAG = "UpdateShopActivity";

    //Widgets
    private EditText nameET, locationET, contactET, websiteET;
    private CircleImageView profilePicCIV;
    private Button changeProfilePicBtn, updateBtn;
    private ProgressDialog progressDialog;

    //Firebase Variables
    private FirebaseAuth mAuth;
    private FirebaseFirestore mDb;
    private StorageReference mRef;

    //REQUEST CODES
    public static final int RC_CHOOSE_IMAGE = 22;
    public static final int RC_CROP_IMAGE = 10;

    private Dermatologist mDermatologist = null;

    private boolean imageSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_dermatologist);

        //Init Firebase Variables
        mAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();
        mRef = FirebaseStorage.getInstance().getReference().child("avatars");

        initComponents();

        changeProfilePicBtn.setOnClickListener(view -> openChooseImageActivity());
        updateBtn.setOnClickListener(view -> uploadProfilePicture());

    }

    private void uploadProfilePicture() {

        progressDialog.setTitle("Updating Profile");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();

        if (imageSelected) {

            Bitmap bitmap = ((BitmapDrawable) profilePicCIV.getDrawable()).getBitmap();
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
                                    updateShopInfo(imageName);
                                }
                        )
                        .addOnFailureListener(e -> Log.e(TAG, "updateShopInfo: Profile Picturee", e));

            }
        } else {
            updateShopInfo(null);
        }


    }

    private void updateShopInfo(String imageName) {

        String name = nameET.getText().toString().trim();
        String location = locationET.getText().toString().trim();
        String contact = contactET.getText().toString().trim();
        String website = websiteET.getText().toString().trim();

        if (!emptyRequiredFields()) {

            mDermatologist.setOfficialName(name);
            mDermatologist.setLocation(location);
            mDermatologist.setMobile(contact);
            mDermatologist.setWebsite(website);
            mDermatologist.setProfilePicName(imageName);

            mDb.collection("shops")
                    .document(mAuth.getCurrentUser().getUid())
                    .set(mDermatologist)
                    .addOnSuccessListener(
                            aVoid -> {
                                Toast.makeText(this, "Shop owner profile successfully updated", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(this, BeautyShopDashboardActivity.class));
                                progressDialog.dismiss();
                            }
                    )
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Update Failed try again after some time", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "updateShopInfo: Sending to firestore", e);
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

    private void initComponents() {

        //EditeTexts
        nameET = findViewById(R.id.name_et);
        locationET = findViewById(R.id.location_et);
        contactET = findViewById(R.id.contact_et);
        websiteET = findViewById(R.id.website_et);

        //CircleImageView
        profilePicCIV = findViewById(R.id.profile_pic_civ);

        //Buttons
        changeProfilePicBtn = findViewById(R.id.change_profile_pic_btn);
        updateBtn = findViewById(R.id.update_btn);

        //ProgressDialog
        progressDialog = new ProgressDialog(this);

    }

    private void updateUI(Dermatologist dermatologist) {

        nameET.setText(dermatologist.getOfficialName());
        locationET.setText(dermatologist.getLocation());
        contactET.setText(dermatologist.getMobile());
        websiteET.setText(dermatologist.getWebsite());

        if (dermatologist.getProfilePicName() != null) {
            StorageReference profilePicRef = mRef.child(dermatologist.getProfilePicName());
            final long MB = 1024 * 1024;
            profilePicRef.getBytes(MB)
                    .addOnSuccessListener(bytes -> {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        profilePicCIV.setImageBitmap(bitmap);

                    })
                    .addOnFailureListener(e -> Log.e(TAG, "updateUI: Image Failure", e));
        }

    }

    private void getDermatologist(String uid) {

        mDb.collection("dermatologists")
                .document(uid)
                .get()
                .addOnSuccessListener(
                        documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                Dermatologist dermatologist = documentSnapshot.toObject(Dermatologist.class);

                                this.mDermatologist = dermatologist;

                                if (dermatologist != null) updateUI(dermatologist);
                            } else {
                                Toast.makeText(this, "No details", Toast.LENGTH_SHORT).show();
                            }
                        }
                )
                .addOnFailureListener(e -> Log.w(TAG, "getDermatologist: Failure", e));

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
                    profilePicCIV.setImageURI(data.getData());

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
            profilePicCIV.setImageBitmap(bitmap);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(this, SigninAsActivity.class));
            finish();
        } else {
            getDermatologist(user.getUid());
        }
    }
}
