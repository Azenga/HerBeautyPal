package com.example.mercie.example;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mercie.example.models.Salonist;

public class SalonActivity extends AppCompatActivity {

    private Salonist salon = null;

    private final String INFO = "Info";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salon);

        //OK SO far
        salon = (Salonist) getIntent().getSerializableExtra("salon");

        Toast.makeText(this, "" + salon, Toast.LENGTH_SHORT).show();

        ImageView goBackIV = findViewById(R.id.go_back_iv);
        goBackIV.setOnClickListener(view -> {
            startActivity(new Intent(this, SalonsActivity.class));
            finish();
        });

        Toolbar salonToolbar = findViewById(R.id.salon_toolbar);
        TabLayout fragmentTabLayout = findViewById(R.id.fragment_tablayout);

        //OK SO far

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(SalonInfoFragment.getInstance(salon), "Info");
        adapter.addFragment(SalonServicesFragment.getInstance(salon), "Services");
        adapter.addFragment(SalonOffersFragment.getInstance(salon), "Offers");

        //fragContainer.setAdapter(adapter);
        // TODO: 3/26/19 Make the fragment show => Ithink I will end using the default way to swap fragments

        fragmentTabLayout.addTab(fragmentTabLayout.newTab().setText("Info"));
        fragmentTabLayout.addTab(fragmentTabLayout.newTab().setText("Services"));
        fragmentTabLayout.addTab(fragmentTabLayout.newTab().setText("Offers"));

        fragmentTabLayout.setOnTabSelectedListener(
                new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {

                        if (tab.getText() != null) {
                            switch (tab.getText().toString()) {
                                case "Info":
                                    displayFragment(SalonInfoFragment.getInstance(salon));
                                    break;
                                case "Services":
                                    displayFragment(SalonServicesFragment.getInstance(salon));
                                    break;
                                case "Offers":
                                    displayFragment(SalonOffersFragment.getInstance(salon));
                                    break;
                                default:
                                    displayFragment(SalonInfoFragment.getInstance(salon));
                            }
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                }
        );


    }

    public void displayFragment(Fragment frag) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, frag);

        transaction.commit();

    }


}


