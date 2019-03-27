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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mercie.example.models.Shop;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupShopActivity extends AppCompatActivity {

    //Widgets
    private Spinner spinnerDay, spinnerDayTo, spinnerTime, spinnerTimeTo;
    private EditText officialNameET, locationET, mobileNumberET, websiteET;
    private Button changeImageBtn, finishSetupBtn;
    private CircleImageView profileImageView;
    private ProgressDialog progressDialog;

    private static final int STORAGE_PERMISSION_REQUEST_CODE = 999;
    private static final int CHOOSE_IMAGE_REQUEST_CODE = 998;
    private static final int CROP_REQUEST_CODE = 997;

    private Bitmap profileIVBitmap = null;

    //Firebase Variables
    private FirebaseAuth mAuth;
    private StorageReference mRef;
    private FirebaseFirestore mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_shop);

        //Init Firebase Vars
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseStorage.getInstance().getReference().child("avatars");
        mDb = FirebaseFirestore.getInstance();

        initSpinners();
        initOtherWidgets();


    }

    private void initOtherWidgets() {
        //EditTexts
        officialNameET = findViewById(R.id.ssh_official_name_et);
        locationET = findViewById(R.id.ssh_location_et);
        mobileNumberET = findViewById(R.id.ssh_mobile_number_et);
        websiteET = findViewById(R.id.ssh_website_et);

        //Buttons
        changeImageBtn = findViewById(R.id.setup_shop_change_profile_btn);
        changeImageBtn.setOnClickListener(view -> checkAppPermmisions());
        finishSetupBtn = findViewById(R.id.shop_setup_btn);
        finishSetupBtn.setOnClickListener(view -> finishShopSetup());

        //ImageView
        profileImageView = findViewById(R.id.ssh_profile_iv);

        //ProgressDialog
        progressDialog = new ProgressDialog(this);
    }

    private void finishShopSetup() {
        String officialName = officialNameET.getText().toString();
        String location = locationET.getText().toString();
        String mobile = mobileNumberET.getText().toString();
        String website = websiteET.getText().toString();
        String dayFrom = "Day: " + spinnerDay.getSelectedItem() + " Time: " + spinnerTime.getSelectedItem();
        String dayTo = "Day: " + spinnerDayTo.getSelectedItem() + " Time: " + spinnerTimeTo.getSelectedItem();

        if (!TextUtils.isEmpty(officialName) && !TextUtils.isEmpty(location) && !TextUtils.isEmpty(mobile)) {

            progressDialog.setTitle("Setting up Beauty Shop");
            progressDialog.setMessage("Please Wait");
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.show();

            if (profileIVBitmap == null) {
                Toast.makeText(this, "You did not choose an Image", Toast.LENGTH_SHORT).show();
                addShopToFirebase(new Shop(officialName, location, mobile, website, null, dayFrom, dayTo));
            } else {

                StorageReference profilePicRef = mRef.child(mAuth.getCurrentUser().getUid() + ".jpg");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                profileIVBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = profilePicRef.putBytes(data);

                uploadTask.addOnSuccessListener(
                        taskSnapshot -> {
                            String profileImageName = taskSnapshot.getMetadata().getName();

                            addShopToFirebase(new Shop(officialName, location, mobile, website, profileImageName, dayFrom, dayTo));

                        }
                ).addOnFailureListener(e -> Toast.makeText(this, "Error uploading image: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());

            }
        } else {

            Toast.makeText(this, "Fill in all the required fields", Toast.LENGTH_SHORT).show();

        }

    }

    private void addShopToFirebase(Shop shop) {
        mDb.collection("shops")
                .document(mAuth.getCurrentUser().getUid())
                .set(shop)
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Shop setup was successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(this, BeautyShopDashboardActivity.class));
                                finish();

                            } else {
                                Toast.makeText(this, "Shop Setup Error: " + task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }

                            progressDialog.dismiss();
                        }
                );
    }


    private void checkAppPermmisions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openChooseImageActivity();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST_CODE);
            }

        } else {
            openChooseImageActivity();
        }
    }

    private void openChooseImageActivity() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_IMAGE_REQUEST_CODE);
    }

    //Initializing and populating the spinners
    private void initSpinners() {
        String[] weekDays = getResources().getStringArray(R.array.week_days);
        String[] dayWorkHours = getResources().getStringArray(R.array.day_time);

        ArrayAdapter<String> weekDaysAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, weekDays);
        ArrayAdapter<String> dayWorkHoursAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dayWorkHours);

        spinnerDay = findViewById(R.id.spinner_day_from);
        spinnerDay.setAdapter(weekDaysAdapter);

        spinnerDayTo = findViewById(R.id.spinner_day_to);
        spinnerDayTo.setAdapter(weekDaysAdapter);

        spinnerTime = findViewById(R.id.spinner_time_from);
        spinnerTime.setAdapter(dayWorkHoursAdapter);

        spinnerTimeTo = findViewById(R.id.spinner_time_to);
        spinnerTimeTo.setAdapter(dayWorkHoursAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case STORAGE_PERMISSION_REQUEST_CODE:
                    openChooseImageActivity();
                    break;

                default:
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case CHOOSE_IMAGE_REQUEST_CODE:
                    openCroppingIntent(data.getData());
                    break;
                case CROP_REQUEST_CODE:
                    populateProfileImageView(data);
                    break;
                default:
                    super.onActivityResult(requestCode, resultCode, data);

            }
        }
    }

    private void populateProfileImageView(Intent data) {
        Bundle bundle = data.getExtras();

        profileIVBitmap = bundle.getParcelable("data");

        profileImageView.setImageBitmap(profileIVBitmap);

    }

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

        if (activities.size() > 0) startActivityForResult(intent, CROP_REQUEST_CODE);
        else Toast.makeText(this, "Install a cropping application", Toast.LENGTH_SHORT).show();

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
