package com.example.mercie.example;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;


public class TipsActivity extends AppCompatActivity {
    private Toolbar bar;
    private TabLayout tabLayout;
    private ViewPagerAdapter adapter;
    private ViewPager viewPager;
    private ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);



        bar =findViewById(R.id.bar);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TipsActivity.this, HomeActivity.class));
            }
        });




        viewPager=(ViewPager)findViewById(R.id.viewpager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HairFragment(), "Hair");
        adapter.addFragment(new FaceFragment(), "Face");
        adapter.addFragment(new EyesFragment(), "Eyes");
        adapter.addFragment(new SkinFragment(), "Skin");
        adapter.addFragment(new HandsFragment(), "Hands");
        adapter.addFragment(new FeetFragment(), "Feet");
        viewPager.setAdapter(adapter);

        tabLayout=(TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);




    }


}
