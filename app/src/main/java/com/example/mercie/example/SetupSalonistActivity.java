package com.example.mercie.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SetupSalonistActivity extends AppCompatActivity {
    Spinner sspinnerDay;
    Spinner sspinnerDayTo;
    Spinner sspinnerTime;
    Spinner sspinnerTimeTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_salonist);

        sspinnerDay = findViewById(R.id.sspinnerDay);
        String[] day = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, day);
        sspinnerDay.setAdapter(adapter);

        sspinnerDayTo = findViewById(R.id.sspinnerDayTo);
        ArrayAdapter<String> adapterr = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, day);
        sspinnerDayTo.setAdapter(adapterr);

        sspinnerTime = findViewById(R.id.sspinnerTime);
        String[] time = {"7am", "8am", "9am", "10am", "11am", "12pm", "1pm", "2pm", "3pm", "4pm", "5pm", "6pm", "7pm", "8pm", "9pm", "10pm"};
        ArrayAdapter<String> adapterrr = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, time);
        sspinnerTime.setAdapter(adapterrr);

        sspinnerTimeTo = findViewById(R.id.sspinnerTimeTo);
        ArrayAdapter<String> adapterrrr = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, time);
        sspinnerTimeTo.setAdapter(adapterrrr);
    }
}
