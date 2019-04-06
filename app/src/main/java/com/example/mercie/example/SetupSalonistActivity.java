package com.example.mercie.example;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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

public class SetupSalonistActivity extends AppCompatActivity {


    private CircleImageView profileIV;

    private EditText officialNameET, mobileNUmberET, locationET, websiteET;
    private Button chooseImageBtn, setupBtn;

    private ProgressDialog progressDialog;


    //Firebase Variable
    private FirebaseAuth mAuth;
    private StorageReference mRef;
    private FirebaseFirestore mFirestore;

    private final static int CHOOSE_IMAGE_REQUEST_CODE = 999;
    private final static int CROP_IMAGE_REQUEST_CODE = 998;
    private final static int STORAGE_REQUEST_CODE = 997;

    private Bitmap profileBitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_salonist);


        //Init firebase variables
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseStorage.getInstance().getReference().child("avatars");
        mFirestore = FirebaseFirestore.getInstance();

        initOtherComponents();

        progressDialog = new ProgressDialog(this);

        chooseImageBtn.setOnClickListener(view -> chooseAnImage());
        setupBtn.setOnClickListener(view -> uploadDetails());

    }

    private void uploadDetails() {

        //Compulsory fields
        String name = officialNameET.getText().toString();
        String location = locationET.getText().toString();
        String contact = mobileNUmberET.getText().toString();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(location) && !TextUtils.isEmpty(contact)) {
            //Additional fields
            String website = websiteET.getText().toString();

            progressDialog.setTitle("Setup Salon Details");
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCanceledOnTouchOutside(true);

            if (profileBitmap != null) {

                //Converting the bitmap to a byte array
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                profileBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                byte[] data = baos.toByteArray();

                //ProfilePicture Reference
                StorageReference profilePicRef = mRef.child(mAuth.getCurrentUser().getUid() + ".jpg");

                progressDialog.show();
                UploadTask profilePicUploadTask = profilePicRef.putBytes(data);

                profilePicUploadTask.addOnFailureListener(e -> Toast.makeText(this, "Error uploading pic: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show())
                        .addOnSuccessListener(
                                taskSnapshot -> {
                                    String fileName = taskSnapshot.getMetadata().getName();
                                    Salonist salonist = new Salonist(name, location, contact, website, fileName);

                                    addSalonistToFirestore(salonist);
                                }
                        )
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Upload profile pic error: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        });

            } else {
                Salonist salonist = new Salonist(name, location, contact, website, null);

                Toast.makeText(this, "You have not uploaded an avatar", Toast.LENGTH_SHORT).show();
                progressDialog.show();
                addSalonistToFirestore(salonist);
            }

        } else {
            Toast.makeText(this, "Fill in all the compulsory fields i.e (Name, Location, Phone)", Toast.LENGTH_SHORT).show();
        }

    }

    private void addSalonistToFirestore(Salonist salonist) {

        if (mAuth.getCurrentUser() != null) {
            mFirestore.collection("salonists")
                    .document(mAuth.getCurrentUser().getUid())
                    .set(salonist)
                    .addOnCompleteListener(
                            task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(this, "Your profile has been updated", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(this, SalonistDashboardActivity.class));
                                    finish();
                                } else
                                    Toast.makeText(this, "A fatal error occurred: " + task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                    );
        }

    }

    //Checking whether the app has permissions to read and write to external storage
    private void chooseAnImage() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openIntentToChooseImageFromFiles();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_REQUEST_CODE);
            }
        } else {
            openIntentToChooseImageFromFiles();
        }

    }

    //A function to choose Image from the android files
    private void openIntentToChooseImageFromFiles() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_IMAGE_REQUEST_CODE);
    }


    //Initialize other widgets other than the spinners
    private void initOtherComponents() {
        //EditTexts
        officialNameET = findViewById(R.id.ss_official_name_et);
        locationET = findViewById(R.id.ss_location_et);
        mobileNUmberET = findViewById(R.id.ss_mobile_number_et);
        websiteET = findViewById(R.id.ss_website_et);

        //ImageViews
        profileIV = findViewById(R.id.profile_civ); //Remember this is CircularImageView

        //Buttons
        chooseImageBtn = findViewById(R.id.setup_salonist_change_profile_btn);
        setupBtn = findViewById(R.id.salonist_setup_btn);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case STORAGE_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openIntentToChooseImageFromFiles();
                } else {
                    Toast.makeText(this, "Sorry, Requested Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case CHOOSE_IMAGE_REQUEST_CODE:
                    if (data != null) openCroppingIntent(data.getData());
                    break;
                case CROP_IMAGE_REQUEST_CODE:
                    if (data != null) populateIV(data);
                    break;
                default:
                    super.onActivityResult(requestCode, resultCode, data);
            }

        }
    }

    //Fill the ImageView with cropping results
    private void populateIV(Intent data) {

        Bundle bundle = data.getExtras();
        profileBitmap = bundle.getParcelable("data");
        profileIV.setImageBitmap(profileBitmap);

    }

    //Function to start an activity to crop image
    private void openCroppingIntent(Uri uri) {

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

        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);

        if (activities.size() > 0) startActivityForResult(intent, CROP_IMAGE_REQUEST_CODE);
        else
            Toast.makeText(this, "Install an application to crop images", Toast.LENGTH_SHORT).show();
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


