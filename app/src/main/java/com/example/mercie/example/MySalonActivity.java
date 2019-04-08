package com.example.mercie.example;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.mercie.example.fragments.salonist.SalonistAddServiceFragment;
import com.example.mercie.example.fragments.salonist.salon.InfoFragment;
import com.example.mercie.example.fragments.salonist.salon.ServicesFragment;
import com.example.mercie.example.models.Salon;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 */
public class MySalonActivity extends AppCompatActivity implements
        ServicesFragment.OnServiceFragInteraction {

    public static final String SALON_PARAM = "salon";
    private BottomNavigationView.OnNavigationItemSelectedListener bnvlistener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {

                case R.id.bnv_nav_info:
                    toolbar.setTitle("Salon Info");
                    changeFragment(InfoFragment.newInstance(mSalon));
                    return true;

                case R.id.bnv_nav_services:
                    toolbar.setTitle("Salon Services");
                    changeFragment(ServicesFragment.newInstance(mSalon.getId()));
                    return true;

                case R.id.bnv_nav_offers:
                    toolbar.setTitle("Salon Offers");
                    // TODO: 4/5/19 Create Offers Fragment and Switch to It
                    return true;

                default:
                    return false;
            }
        }
    };

    private Salon mSalon = null;

    private FirebaseFirestore mDb;
    private FirebaseAuth mAuth;

    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_salon);

        if (getIntent() != null)
            mSalon = (Salon) getIntent().getSerializableExtra(SALON_PARAM);

        mDb = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        BottomNavigationView bvn = findViewById(R.id.my_salon_bnv);

        bvn.setOnNavigationItemSelectedListener(bnvlistener);

        toolbar = findViewById(R.id.toolbar);

        if (getSupportActionBar() == null) setSupportActionBar(toolbar);

        changeFragment(InfoFragment.newInstance(mSalon));

    }

    public void changeFragment(Fragment frag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (frag != null) transaction.replace(R.id.frag_container, frag).commit();
    }


    @Override
    public void loadAddFragmentService(String salonId) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("Add Service");
        changeFragment(SalonistAddServiceFragment.newInstance(salonId));
    }

}
