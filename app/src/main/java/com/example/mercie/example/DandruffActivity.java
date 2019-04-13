package com.example.mercie.example;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

public class DandruffActivity extends AppCompatActivity {
    private Toolbar tool;
    private TabLayout tabLayout;
    private ViewPagerAdapter adapter;
    private ViewPager viewPager;
    private ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dandruff);
        tool = findViewById(R.id.tool);

        back = findViewById(R.id.back);
        back.setOnClickListener(view -> startActivity(new Intent(DandruffActivity.this, TipsActivity.class)));


        viewPager = findViewById(R.id.container);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new RemmedyFragment(), "Remmedy");
        adapter.addFragment(new DietFragment(), "Diet");

        viewPager.setAdapter(adapter);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


    }


}
