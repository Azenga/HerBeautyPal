package com.example.mercie.example;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

public class SalonActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPagerAdapter adapter;
    private ViewPager viewPager;
    private ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salon);

        Bundle bundle = getIntent().getExtras();

        toolbar = findViewById(R.id.toolbar);
        back = findViewById(R.id.back);
        back.setOnClickListener(view -> startActivity(new Intent(this, SalonsActivity.class)));


        viewPager = findViewById(R.id.viewpager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(SalonInfoFragment.getInstance(bundle), "Info");
        adapter.addFragment(SalonServicesFragment.getInstance(bundle), "Services");
        adapter.addFragment(SalonOffersFragment.getInstance(bundle), "Offers");

        viewPager.setAdapter(adapter);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


    }


}


