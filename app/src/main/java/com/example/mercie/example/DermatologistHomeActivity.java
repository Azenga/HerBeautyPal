package com.example.mercie.example;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.mercie.example.adapters.BeatyPalStatePagerAdapter;
import com.example.mercie.example.fragments.dermatologist.Home;
import com.example.mercie.example.fragments.dermatologist.Notifications;
import com.example.mercie.example.fragments.dermatologist.Profile;
import com.example.mercie.example.fragments.dermatologist.Tips;

public class DermatologistHomeActivity extends AppCompatActivity {


    private BottomNavigationView.OnNavigationItemSelectedListener bnvListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {

                case R.id.bnav_home:
                    changeFragment(0);
                    toolbar.setTitle("Timeline");
                    return true;

                case R.id.bnav_notifications:
                    changeFragment(1);
                    toolbar.setTitle("Notifications");

                    return true;

                case R.id.bnav_profile:
                    changeFragment(2);
                    toolbar.setTitle("Profile");

                    return true;

                case R.id.bnav_tips:
                    changeFragment(3);
                    toolbar.setTitle("Tips");

                    return true;

                default:
                    return false;
            }

        }
    };

    private ViewPager viewPager;
    private BeatyPalStatePagerAdapter adapter;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dermatologist_home);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Home");

        viewPager = findViewById(R.id.container);

        adapter = new BeatyPalStatePagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new Home(), "Home");
        adapter.addFragment(new Notifications(), "Notifications");
        adapter.addFragment(new Profile(), "Profile");
        adapter.addFragment(new Tips(), "Tips");

        viewPager.setAdapter(adapter);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bnv);
        bottomNavigationView.setOnNavigationItemSelectedListener(bnvListener);
    }

    public void changeFragment(int position) {
        viewPager.setCurrentItem(position);
    }

}
