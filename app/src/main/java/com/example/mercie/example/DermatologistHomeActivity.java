package com.example.mercie.example;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mercie.example.adapters.BeatyPalStatePagerAdapter;
import com.example.mercie.example.fragments.dermatologist.ChatsFragment;
import com.example.mercie.example.fragments.dermatologist.Notifications;
import com.example.mercie.example.fragments.dermatologist.Profile;
import com.example.mercie.example.fragments.dermatologist.TipsFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DermatologistHomeActivity extends AppCompatActivity {


    private BottomNavigationView.OnNavigationItemSelectedListener bnvListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {

                case R.id.bnav_home:
                    changeFragment(0);
                    toolbar.setTitle("Profile");
                    return true;

                case R.id.bnav_notifications:
                    changeFragment(3);
                    toolbar.setTitle("Notifications");

                    return true;

                case R.id.bnav_chats:
                    changeFragment(2);
                    toolbar.setTitle("Chats");

                    return true;

                case R.id.bnav_tips:
                    changeFragment(1);
                    toolbar.setTitle("TipsFragment");
                    return true;

                default:
                    return false;
            }

        }
    };

    private ViewPager viewPager;
    private BeatyPalStatePagerAdapter adapter;
    Toolbar toolbar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dermatologist_home);

        mAuth = FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Profile");

        viewPager = findViewById(R.id.container);


        adapter = new BeatyPalStatePagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new Profile(), "Profile");
        adapter.addFragment(new TipsFragment(), "TipsFragment");
        adapter.addFragment(new ChatsFragment(), "Chats");
        adapter.addFragment(new Notifications(), "Notifications");

        viewPager.setAdapter(adapter);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bnv);
        bottomNavigationView.setOnNavigationItemSelectedListener(bnvListener);
    }

    public void changeFragment(int position) {
        viewPager.setCurrentItem(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dermatologist_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void signOut() {
        mAuth.signOut();

        startActivity(new Intent(this, SigninAsActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            startActivity(new Intent(this, SigninAsActivity.class));
            finish();
        }
    }
}
