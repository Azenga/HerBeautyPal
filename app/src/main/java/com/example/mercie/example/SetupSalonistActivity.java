package com.example.mercie.example;

import android.Manifest;
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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mercie.example.dialogs.ServiceDialogPrompt;
import com.example.mercie.example.models.Salon;
import com.example.mercie.example.models.SalonService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupSalonistActivity extends AppCompatActivity implements ServiceDialogPrompt.ServiceDialogPromptListener {

    private Spinner dayFromSpinner;
    private Spinner dayToSpinner;
    private Spinner timeFromSpinner;
    private Spinner timeToSpinner;
    private CircleImageView profileIV;

    private EditText officialNameET, mobileNUmberET, locationET, websiteET;
    private ImageView hairCbIV, nailsCBIV, skinCareCBIV, makeUpCBIV;
    private CheckBox hairCB, nailsCB, skinCareCB, makeUpCB;
    private Button chooseImageBtn, setupBtn;

    private List<SalonService> services;

    //Firebase Variable
    private FirebaseAuth mAuth;
    private StorageReference mRef;
    private FirebaseFirestore mFirestore;


    //CheckBoxes Listener
    private CheckBox.OnCheckedChangeListener checkBoxListeners = new CheckBox.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int id = buttonView.getId();

            if (isChecked) {

                switch (id) {

                    case R.id.hair_cb:
                        hairCbIV.setVisibility(View.VISIBLE);
                        break;
                    case R.id.nails_cb:
                        nailsCBIV.setVisibility(View.VISIBLE);
                        break;
                    case R.id.skin_care_cb:
                        skinCareCBIV.setVisibility(View.VISIBLE);
                        break;
                    case R.id.make_up_cb:
                        makeUpCBIV.setVisibility(View.VISIBLE);
                        break;
                }
            } else {
                switch (id) {

                    case R.id.hair_cb:
                        hairCbIV.setVisibility(View.GONE);
                        break;
                    case R.id.nails_cb:
                        nailsCBIV.setVisibility(View.GONE);
                        break;
                    case R.id.skin_care_cb:
                        skinCareCBIV.setVisibility(View.GONE);
                        break;
                    case R.id.make_up_cb:
                        makeUpCBIV.setVisibility(View.GONE);
                        break;
                }
            }
        }
    };

    private final static int CHOOSE_IMAGE_REQUEST_CODE = 999;
    private final static int CROP_IMAGE_REQUEST_CODE = 998;
    private final static int STORAGE_REQUEST_CODE = 997;

    private Bitmap profileBitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_salonist);

        services = new ArrayList<>();

        //Init firebase variables
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseStorage.getInstance().getReference().child("avatars");
        mFirestore = FirebaseFirestore.getInstance();

        initSpinners();

        initOtherComponents();

        hairCB.setOnCheckedChangeListener(checkBoxListeners);
        nailsCB.setOnCheckedChangeListener(checkBoxListeners);
        skinCareCB.setOnCheckedChangeListener(checkBoxListeners);
        makeUpCB.setOnCheckedChangeListener(checkBoxListeners);

        hairCbIV.setOnClickListener(view -> openDialogBox(view));
        nailsCBIV.setOnClickListener(view -> openDialogBox(view));
        skinCareCBIV.setOnClickListener(view -> openDialogBox(view));
        makeUpCBIV.setOnClickListener(view -> openDialogBox(view));

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
            String openFfrom = "Date: " + dayFromSpinner.getSelectedItem().toString() + " Time: " + timeFromSpinner.getSelectedItem().toString();
            String openTo = "Date: " + dayToSpinner.getSelectedItem().toString() + " Time: " + timeToSpinner.getSelectedItem().toString();

            if (profileBitmap != null) {

                //Converting the bitmap to a byte array
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                profileBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                byte[] data = baos.toByteArray();

                //ProfilePicture Reference
                StorageReference profilePicRef = mRef.child(mAuth.getCurrentUser().getUid() + ".jpg");

                UploadTask profilePicUploadTask = profilePicRef.putBytes(data);

                profilePicUploadTask.addOnFailureListener(e -> Toast.makeText(this, "Error uploading pic: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show())
                        .addOnSuccessListener(
                                taskSnapshot -> {
                                    String fileName = taskSnapshot.getMetadata().getName();
                                    Salon salon = new Salon(name, location, contact, website, openFfrom, openTo, fileName);

                                    proceedDetailsToFirestore(salon);
                                }
                        );

            } else {
                Salon salon = new Salon(name, location, contact, website, openFfrom, openTo, null);

                Toast.makeText(this, "You have not uploaded an avatar", Toast.LENGTH_SHORT).show();

                proceedDetailsToFirestore(salon);
            }


        } else {
            Toast.makeText(this, "Fill in all the compulsory fields i.e (Name, Location, Phone)", Toast.LENGTH_SHORT).show();
        }

    }

    private void proceedDetailsToFirestore(Salon salon) {
        mFirestore.collection("salons")
                .document(mAuth.getCurrentUser().getUid())
                .set(salon)
                .addOnCompleteListener(
                        task1 -> {
                            if (task1.isSuccessful()) {
                                Toast.makeText(this, "Your profile has been updated", Toast.LENGTH_SHORT).show();

                                Thread thread = new Thread() {
                                    @Override
                                    public void run() {
                                        try {
                                            sleep(2000);
                                            startActivity(new Intent(SetupSalonistActivity.this, SalonistDashboardActivity.class));
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

    //This function should open a dialog to write the service offered in the respective package
    private void openDialogBox(View view) {

        // Open the relevant dialog according to the clicked CheckBox Image
        ServiceDialogPrompt dialogPrompt = null;

        switch (view.getId()) {

            case R.id.hair_cb_iv:
                dialogPrompt = ServiceDialogPrompt.getInstance("Hair");
                break;
            case R.id.nails_cb_iv:
                dialogPrompt = ServiceDialogPrompt.getInstance("Nails");
                break;
            case R.id.skin_care_cb_iv:
                dialogPrompt = ServiceDialogPrompt.getInstance("Skin Care");
                break;
            case R.id.make_up_cb_iv:
                dialogPrompt = ServiceDialogPrompt.getInstance("Make Up");
                break;
            default:
                dialogPrompt = null;
        }

        dialogPrompt.show(getSupportFragmentManager(), "Add A SalonService");

        Toast.makeText(this, "Show a dialog box to choose service", Toast.LENGTH_SHORT).show();
    }

    //Initialize other widgets other than the spinners
    private void initOtherComponents() {
        //EditTexts
        officialNameET = findViewById(R.id.ss_official_name_et);
        locationET = findViewById(R.id.ss_location_et);
        mobileNUmberET = findViewById(R.id.ss_mobile_number_et);
        websiteET = findViewById(R.id.ss_website_et);

        //ImageViews
        profileIV = findViewById(R.id.profile_iv); //Remember this is CircularImageView

        hairCbIV = findViewById(R.id.hair_cb_iv);
        nailsCBIV = findViewById(R.id.nails_cb_iv);
        skinCareCBIV = findViewById(R.id.skin_care_cb_iv);
        makeUpCBIV = findViewById(R.id.make_up_cb_iv);

        //CheckBoxes
        hairCB = findViewById(R.id.hair_cb);
        nailsCB = findViewById(R.id.nails_cb);
        skinCareCB = findViewById(R.id.skin_care_cb);
        makeUpCB = findViewById(R.id.make_up_cb);

        //Buttons
        chooseImageBtn = findViewById(R.id.setup_salonist_change_profile_btn);
        setupBtn = findViewById(R.id.salonist_setup_btn);
    }

    //Initializing and populating the spinners
    private void initSpinners() {
        String[] weekDays = getResources().getStringArray(R.array.week_days);
        String[] dayWorkHours = getResources().getStringArray(R.array.day_time);

        ArrayAdapter<String> weekDaysAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, weekDays);
        ArrayAdapter<String> dayWorkHoursAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dayWorkHours);

        dayFromSpinner = findViewById(R.id.spinner_day_from);
        dayFromSpinner.setAdapter(weekDaysAdapter);

        dayToSpinner = findViewById(R.id.spinner_day_to);
        dayToSpinner.setAdapter(weekDaysAdapter);

        timeFromSpinner = findViewById(R.id.spinner_time_from);
        timeFromSpinner.setAdapter(dayWorkHoursAdapter);

        timeToSpinner = findViewById(R.id.spinner_time_to);
        timeToSpinner.setAdapter(dayWorkHoursAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case STORAGE_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openIntentToChooseImageFromFiles();
                } else {
                    Toast.makeText(this, "Sorry Permission denied", Toast.LENGTH_SHORT).show();
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
        intent.putExtra("scaleUpIfneeded", true);
        intent.putExtra("return-data", true);

        PackageManager packageManager = getPackageManager();

        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);

        if (activities.size() > 0) startActivityForResult(intent, CROP_IMAGE_REQUEST_CODE);
        else
            Toast.makeText(this, "Install an application to crop images", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addService(SalonService service) {
        services.add(service);
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


