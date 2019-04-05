package com.example.mercie.example;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mercie.example.dialogs.ServiceDialogPrompt;
import com.example.mercie.example.models.Salon;
import com.example.mercie.example.models.SalonService;
import com.example.mercie.example.models.Salonist;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class RegisterSalonActivity extends AppCompatActivity implements ServiceDialogPrompt.ServiceDialogPromptListener {

    private Spinner dayFromSpinner;
    private Spinner dayToSpinner;
    private Spinner timeFromSpinner;
    private Spinner timeToSpinner;

    private ImageView hairCbIV, nailsCBIV, skinCareCBIV, makeUpCBIV, coverIV;
    private CheckBox hairCB, nailsCB, skinCareCB, makeUpCB;
    private EditText nameET, locationET, mobileET, websiteET;
    private ProgressDialog progressDialog;

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

    //Request code for getting an image from the fs
    private static final int GET_IMAGE_RC = 999;

    //Global image uri for the salon cover image
    private Uri mImageUri = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_salon);

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (getSupportActionBar() == null) setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Register Salon");

        services = new ArrayList<>();

        initSpinners();
        initComponents();


        //Init firebase variables
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseStorage.getInstance().getReference().child("cover_images");
        mFirestore = FirebaseFirestore.getInstance();

        hairCB.setOnCheckedChangeListener(checkBoxListeners);
        nailsCB.setOnCheckedChangeListener(checkBoxListeners);
        skinCareCB.setOnCheckedChangeListener(checkBoxListeners);
        makeUpCB.setOnCheckedChangeListener(checkBoxListeners);

        hairCbIV.setOnClickListener(view -> openDialogBox(view));
        nailsCBIV.setOnClickListener(view -> openDialogBox(view));
        skinCareCBIV.setOnClickListener(view -> openDialogBox(view));
        makeUpCBIV.setOnClickListener(view -> openDialogBox(view));

        coverIV.setOnClickListener(
                view -> {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, GET_IMAGE_RC);
                }
        );

    }

    private void initComponents() {
        hairCbIV = findViewById(R.id.hair_cb_iv);
        nailsCBIV = findViewById(R.id.nails_cb_iv);
        skinCareCBIV = findViewById(R.id.skin_care_cb_iv);
        makeUpCBIV = findViewById(R.id.make_up_cb_iv);
        coverIV = findViewById(R.id.cover_image_iv);

        //CheckBoxes
        hairCB = findViewById(R.id.hair_cb);
        nailsCB = findViewById(R.id.nails_cb);
        skinCareCB = findViewById(R.id.skin_care_cb);
        makeUpCB = findViewById(R.id.make_up_cb);

        //EditTexts
        nameET = findViewById(R.id.name_et);
        locationET = findViewById(R.id.location_et);
        mobileET = findViewById(R.id.mobile_et);
        websiteET = findViewById(R.id.website_et);
        progressDialog = new ProgressDialog(this);
    }

    private void addSalonServicesToFirestore(String salonId) {
        if (services != null) {
            progressDialog.setTitle("Adding Services");
            for (SalonService service : services) {
                mFirestore.collection("services").document(salonId).collection("Services").add(service);
            }
        }

        Toast.makeText(this, "Services Added", Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
        startActivity(new Intent(this, SalonistDashboardActivity.class));
        finish();

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
        if (dialogPrompt != null)
            dialogPrompt.show(getSupportFragmentManager(), "Add A SalonService");

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
    public void addService(SalonService service) {
        services.add(service);
    }

    public void registerSalon(View view) {
        //Getting filled Details
        String name = nameET.getText().toString().trim();
        String location = locationET.getText().toString().trim();
        String contact = mobileET.getText().toString().trim();
        String website = websiteET.getText().toString().trim();
        String openFfrom = "Day: " + dayFromSpinner.getSelectedItem().toString() + " Time: " + timeFromSpinner.getSelectedItem().toString();
        String openTo = "Day: " + dayToSpinner.getSelectedItem().toString() + " Time: " + timeToSpinner.getSelectedItem().toString();

        if (emptyCompulsoryFields()) return;

        if (mImageUri == null) {
            Toast.makeText(this, "A salon must have an image", Toast.LENGTH_SHORT).show();
        } else {
            //Upload the salon cover image first
            StorageReference coverImageRef = mRef.child(System.currentTimeMillis() + '.' + getFileExtension(mImageUri));

            progressDialog.setTitle("Registering Salon");
            progressDialog.setMessage("Please Wait");
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.show();

            UploadTask coverImageUploadTask = coverImageRef.putFile(mImageUri);
            coverImageUploadTask
                    .addOnSuccessListener(
                            taskSnapshot -> {
                                String coverImageName = taskSnapshot.getMetadata().getName();
                                Salon salon = new Salon(name, location, contact, website, openFfrom, openTo, mAuth.getCurrentUser().getUid());
                                salon.setCoverImage(coverImageName);
                                addSalonToFireStore(salon);
                            }
                    )
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(this, "Upload error: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                    });
        }

    }

    private boolean emptyCompulsoryFields() {
        if (nameET.getText().toString().trim().isEmpty()) {
            nameET.requestFocus();
            nameET.setError("Name is required");
            return true;
        } else if (locationET.getText().toString().trim().isEmpty()) {
            locationET.setError("Location is required");
            locationET.requestFocus();
            return true;
        } else if (mobileET.getText().toString().trim().isEmpty()) {
            mobileET.setError("Contact is required");
            mobileET.requestFocus();
            return true;
        } else return false;
    }

    private void addSalonToFireStore(Salon salon) {
        mFirestore.collection("salons")
                .add(salon)
                .addOnSuccessListener(
                        documentReference -> {
                            Toast.makeText(this, "Adding services...", Toast.LENGTH_SHORT).show();
                            String salonId = documentReference.getId();
                            addSalonServicesToFirestore(salonId);

                        }
                )
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver resolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(resolver.getType(uri));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            startActivity(new Intent(this, SigninAsActivity.class));
            finish();
        } else {
            getSalonDetails(user.getUid());
        }

    }

    private void getSalonDetails(String uid) {

        mFirestore.collection("salonists")
                .document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(
                        documentSnapshot -> {

                            Salonist salonist = documentSnapshot.toObject(Salonist.class);

                            populateDefaults(salonist);

                        }
                )
                .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());

    }

    private void populateDefaults(Salonist salonist) {
        locationET.setText(salonist.getLocation());
        mobileET.setText(salonist.getContact());
        websiteET.setText(salonist.getWebsite());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case GET_IMAGE_RC:
                    this.mImageUri = data.getData();
                    coverIV.setImageURI(mImageUri);
                    break;
                default:
                    super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}
