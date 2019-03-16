package com.example.mercie.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SetupDermatologistActivity extends AppCompatActivity {

    Spinner dayFromSpinner;
    Spinner dayToSpinner;
    Spinner timeFromSpinner;
    Spinner timeToSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_dermatologist);

        initSpinners();

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
}
