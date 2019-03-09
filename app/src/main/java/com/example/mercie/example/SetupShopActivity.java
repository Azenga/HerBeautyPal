package com.example.mercie.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SetupShopActivity extends AppCompatActivity {
    Spinner shspinnerDay;
    Spinner shspinnerDayTo;
    Spinner shspinnerTime;
    Spinner shspinnerTimeTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_shop);
        shspinnerDay = findViewById(R.id.shspinnerDay);
        String[] day = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, day);
        shspinnerDay.setAdapter(adapter);

        shspinnerDayTo = findViewById(R.id.shspinnerDayTo);
        ArrayAdapter<String> adapterr = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, day);
        shspinnerDayTo.setAdapter(adapterr);

        shspinnerTime = findViewById(R.id.shspinnerTime);
        String[] time = {"7am", "8am", "9am", "10am", "11am", "12pm", "1pm", "2pm", "3pm", "4pm", "5pm", "6pm", "7pm", "8pm", "9pm", "10pm"};
        ArrayAdapter<String> adapterrr = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, time);
        shspinnerTime.setAdapter(adapterrr);

        shspinnerTimeTo = findViewById(R.id.shspinnerTimeTo);
        ArrayAdapter<String> adapterrrr = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, time);
        shspinnerTimeTo.setAdapter(adapterrrr);


    }
}
