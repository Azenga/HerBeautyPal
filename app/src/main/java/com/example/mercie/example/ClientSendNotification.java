package com.example.mercie.example;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mercie.example.models.Salon;
import com.example.mercie.example.models.SalonService;
import com.example.mercie.example.models.SalonistNotification;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

public class ClientSendNotification extends AppCompatActivity {

    private static final String TAG = "ClientSendNotification";

    public static final String SERVICE = "salon-service";
    public static final String SALON_ID = "salon-id";

    //Widgets
    private TextView salonNameTV, serviceNameTV, dateTV, timeTV;
    private Button requestServiceBtn;
    private ProgressDialog progressDialog;

    private DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
        month += 1;

        String date = String.format("%d/%d/%d", dayOfMonth, month, year);
        dateTV.setText(date);
    };

    private TimePickerDialog.OnTimeSetListener timeSetListener = (view, hourOfDay, minute) -> {
        String time = String.format("%d:%d", hourOfDay, minute);
        timeTV.setText(time);
    };


    private FirebaseAuth mAuth;
    private FirebaseFirestore mDb;

    private SalonService service = null;
    private String salonId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_send_notification);

        initWidgets();
        initPickers();

        mAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();

        if (getIntent() != null) {
            service = (SalonService) getIntent().getSerializableExtra(SERVICE);
            salonId = getIntent().getStringExtra(SALON_ID);
        }

        requestServiceBtn.setOnClickListener(view -> {

            String date = dateTV.getText().toString().trim();
            String time = timeTV.getText().toString().trim();
            String salonName = salonNameTV.getText().toString().trim();
            Timestamp timestamp = new Timestamp(new Date());

            SalonistNotification notification = new SalonistNotification("service", service.getServiceName(), salonName, time, date, timestamp, mAuth.getCurrentUser().getUid());

            mDb
                    .collection("salonistnotifications")
                    .document(salonId)
                    .collection("Notifications")
                    .add(notification)
                    .addOnCompleteListener(
                            task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(this, "Request Sent", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(this, HomeActivity.class));
                                } else {
                                    Toast.makeText(this, "Send Failed", Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, "onCreate: Sending Notification Failed", task.getException());
                                }
                            }
                    );
        });

    }

    private void initPickers() {
        dateTV.setOnClickListener(
                view -> {
                    Calendar calendar = Calendar.getInstance();

                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog = new DatePickerDialog(
                            this,
                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                            dateSetListener,
                            year,
                            month,
                            day
                    );

                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
        );

        timeTV.setOnClickListener(
                view -> {
                    Calendar calendar = Calendar.getInstance();
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(Calendar.MINUTE);

                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                            this,
                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                            timeSetListener,
                            hour,
                            minute,
                            DateFormat.is24HourFormat(this)
                    );

                    timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    timePickerDialog.show();
                }
        );
    }

    private void initWidgets() {
        salonNameTV = findViewById(R.id.salon_name_tv);
        serviceNameTV = findViewById(R.id.service_name_tv);
        dateTV = findViewById(R.id.choose_date_tv);
        timeTV = findViewById(R.id.choose_time_tv);

        requestServiceBtn = findViewById(R.id.request_service_btn);

        progressDialog = new ProgressDialog(this);
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            startActivity(new Intent(this, SigninAsActivity.class));
            finish();
        } else {
            updateUI();
        }
    }

    private void updateUI() {
        mDb.collection("salons")
                .document(salonId)
                .get()
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                if (task.getResult().exists()) {
                                    Salon salon = task.getResult().toObject(Salon.class);
                                    salonNameTV.setText(salon.getName());
                                } else {
                                    Log.e(TAG, "updateUI: An error occurred", task.getException());
                                }
                            }
                        }
                );
        serviceNameTV.setText(service.getServiceName());
    }
}
