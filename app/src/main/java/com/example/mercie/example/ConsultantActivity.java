package com.example.mercie.example;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class ConsultantActivity extends AppCompatActivity {

    public final static String DERMATOLOGIST_ID = "dermatologist-id";

    private String dermatologistId = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultant);

        if (getIntent() != null)
            dermatologistId = getIntent().getStringExtra(DERMATOLOGIST_ID);

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(view -> startActivity(new Intent(this, ConsultantsActivity.class)));

        TabLayout tabLayout = findViewById(R.id.tabs);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new ConsultantInfoFragment(), "Info");
        adapter.addFragment(new MessagesFragment(), "Messages");
        adapter.addFragment(new AppointmentFragment(), "Appointments");

        ViewPager viewPager = findViewById(R.id.container);

        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
    }
}
