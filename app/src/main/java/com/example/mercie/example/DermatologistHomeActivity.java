package com.example.mercie.example;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.mercie.example.fragments.dermatologist.Home;
import com.example.mercie.example.fragments.dermatologist.Notifications;
import com.example.mercie.example.fragments.dermatologist.Profile;
import com.example.mercie.example.fragments.dermatologist.Tips;

public class DermatologistHomeActivity extends AppCompatActivity {

    private ViewPager containerVP;
    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavigationListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch (menuItem.getItemId()) {
                case R.id.bnav_home:
                    changeFragment(0);
                    return true;
                case R.id.bnav_profile:
                    changeFragment(1);
                    return true;
                case R.id.bnav_tips:
                    changeFragment(2);
                    return true;
                case R.id.bnav_notifications:
                    changeFragment(3);
                    return true;
                default:
                    return false;
            }
        }
    };

    private ViewPagerAdapter dermatologistViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dermatologist_home);

        containerVP = findViewById(R.id.dermatologist_vp);

        BottomNavigationView bottomNavigationView = findViewById(R.id.dermatologist_bnv);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavigationListener);

        dermatologistViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        dermatologistViewPagerAdapter.addFragment(new Home(), "Home");
        dermatologistViewPagerAdapter.addFragment(new Profile(), "Profile");
        dermatologistViewPagerAdapter.addFragment(new Tips(), "Tips");
        dermatologistViewPagerAdapter.addFragment(new Notifications(), "Notifications");
    }

    public void changeFragment(int index) {
        containerVP.setCurrentItem(index);
    }
}
