package com.example.mercie.example;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mercie.example.models.SalonistNotification;
import com.example.mercie.example.models.Reservation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class NotificationResponseActivity extends AppCompatActivity {

    private static final String TAG = "NotificationResponseAct";

    private SalonistNotification notification = null;

    //Widgets
    private TextView clientNameTV, serviceNameTV, chooseDateTV, chooseTimeTV;
    private ImageButton gobackIB;
    private Button setReservationBtn;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TimePickerDialog.OnTimeSetListener timeSetListener;

    private FirebaseFirestore mDb;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_response);

        mDb = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        if (getIntent() != null)
            notification = (SalonistNotification) getIntent().getSerializableExtra("notification");

        initComponents();
        initPickers();

        serviceNameTV.setText(notification.getServiceName());

        mDb.collection("clients")
                .document(notification.getClientId())
                .get()
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {

                                DocumentSnapshot doc = task.getResult();

                                if (doc.exists()) {
                                    clientNameTV.setText(doc.getString("name"));

                                } else {
                                    Log.d(TAG, "onBindViewHolder: The document does not exist");
                                }

                            } else {
                                Log.e(TAG, "onBindViewHolder: ", task.getException());
                            }
                        }
                );


        gobackIB.setOnClickListener(view -> startActivity(new Intent(this, SalonistDashboardActivity.class)));

        setReservationBtn.setOnClickListener(view -> setReservation());


    }

    private void setReservation() {
        //Get Time
        String date = chooseDateTV.getText().toString().trim();
        String time = chooseTimeTV.getText().toString().trim();

        Reservation reservation = new Reservation("Service", notification.getServiceName(), time, date, notification.getSalonName(), mAuth.getCurrentUser().getUid());
        reservation.setAgreedUpon(false);

        mDb.collection("clientsreservations")
                .document(notification.getClientId())
                .collection("Reservations")
                .add(reservation)
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {


                                mDb
                                        .collection("salonistnotifications")
                                        .document(mAuth.getCurrentUser().getUid())
                                        .collection("Notifications")
                                        .document(notification.getId())
                                        .delete();

                                Toast.makeText(this, "Reservation Send", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(this, SalonistDashboardActivity.class));
                                finish();
                            } else {

                                Toast.makeText(this, "Error: " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                );

    }

    private void initPickers() {

        chooseDateTV.setOnClickListener(
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

        dateSetListener = (view, year, month, dayOfMonth) -> {
            month += 1;

            String date = String.format("%d/%d/%d", dayOfMonth, month, year);

            chooseDateTV.setText(date);
        };

        chooseTimeTV.setOnClickListener(
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

        timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time = String.format("%d:%d", hourOfDay, minute);

                chooseTimeTV.setText(time);
            }
        };

    }

    private void initComponents() {
        clientNameTV = findViewById(R.id.client_name_tv);
        serviceNameTV = findViewById(R.id.service_name_tv);
        chooseDateTV = findViewById(R.id.choose_date_tv);
        chooseTimeTV = findViewById(R.id.choose_time_tv);
        gobackIB = findViewById(R.id.go_back_ib);
        setReservationBtn = findViewById(R.id.set_reservation_btn);

    }
}
