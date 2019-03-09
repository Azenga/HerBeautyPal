package com.example.mercie.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SetupDermatologistActivity extends AppCompatActivity {
    Spinner spinnerDay;
    Spinner spinnerDayTo;
    Spinner spinnerTime;
    Spinner spinnerTimeTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_dermatologist);

        spinnerDay = findViewById(R.id.spinnerDay);
        String[] day = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, day);
        spinnerDay.setAdapter(adapter);

        spinnerDayTo = findViewById(R.id.spinnerDayTo);
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        ArrayAdapter<String> adapterr = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, days);
        spinnerDayTo.setAdapter(adapterr);

        spinnerTime = findViewById(R.id.spinnerTime);
        String[] time = {"7am", "8am", "9am", "10am", "11am", "12pm", "1pm", "2pm", "3pm", "4pm", "5pm", "6pm", "7pm", "8pm", "9pm", "10pm"};
        ArrayAdapter<String> adapterrr = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, time);
        spinnerTime.setAdapter(adapterrr);

        spinnerTimeTo = findViewById(R.id.spinnerTimeTo);
        String[] times = {"7am", "8am", "9am", "10am", "11am", "12pm", "1pm", "2pm", "3pm", "4pm", "5pm", "6pm", "7pm", "8pm", "9pm", "10pm"};
        ArrayAdapter<String> adapterrrr = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, times);
        spinnerTimeTo.setAdapter(adapterrrr);
    }
}
