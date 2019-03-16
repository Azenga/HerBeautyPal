package com.example.mercie.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SetupShopActivity extends AppCompatActivity {

    private Spinner spinnerDay;
    private Spinner spinnerDayTo;
    private Spinner spinnerTime;
    private Spinner spinnerTimeTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_shop);

        initSpinners();

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
}
