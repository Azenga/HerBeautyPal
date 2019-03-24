package com.example.mercie.example;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mercie.example.dummy.DummyContent;
import com.example.mercie.example.fragments.salonist.GalleryFragment;
import com.example.mercie.example.fragments.salonist.SalonistAddServiceFragment;
import com.example.mercie.example.fragments.salonist.SalonistEditProfileFragment;
import com.example.mercie.example.fragments.salonist.SalonistHomeFragment;
import com.example.mercie.example.models.Salonist;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class SalonistDashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GalleryFragment.GalleryInteraction {

    //NavigationHeaderWIdgets
    private CircleImageView salonistProfileCIV;
    private TextView salonistUsernameTV;

    //Firebase Variables
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private StorageReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salonist_dashboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialize Firebase Variables
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mRef = FirebaseStorage.getInstance().getReference().child("avatars");


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View navHeaderView = navigationView.getHeaderView(0);

        salonistProfileCIV = navHeaderView.findViewById(R.id.salonist_profile_pic_iv);
        salonistUsernameTV = navHeaderView.findViewById(R.id.salonist_username_tv);

        displayFragment(R.id.nav_home);// Loading the default home fragment
    }

    //Changes the SalonistDashboardActivity Fragment according to the selected Navigation Item
    public void displayFragment(int id) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = null;

        switch (id) {
            case R.id.nav_home:
                getSupportActionBar().setTitle("Home");
                fragment = new SalonistHomeFragment();
                break;

            case R.id.nav_gallery:
                getSupportActionBar().setTitle("Gallery");
                fragment = GalleryFragment.newInstance(3);
                break;

            case R.id.nav_edit_profile:
                getSupportActionBar().setTitle("Edit Profile");
                fragment = new SalonistEditProfileFragment();
                break;

            case R.id.nav_add_service:
                getSupportActionBar().setTitle("Add Service");
                fragment = new SalonistAddServiceFragment();
                break;

            case R.id.nav_appointments:
                getSupportActionBar().setTitle("Appointments");
                // TODO: 3/19/19 Add appointments fragment
                break;

            case R.id.nav_notifications:
                getSupportActionBar().setTitle("Notifications");
                // TODO: 3/19/19 Add salonist notifications fragment
                break;

            case R.id.nav_logout:
                logout();
                break;

            case R.id.nav_share:
                break;
        }

        if (fragment != null) {
            transaction.replace(R.id.container, fragment).commit();
        }
    }

    private void logout() {
        mAuth.signOut();
        startActivity(new Intent(this, SigninAsActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.salonist_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        displayFragment(item.getItemId());

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            startActivity(new Intent(this, SigninAsActivity.class));
            finish();
        } else {
            getSalonist(user.getUid());
        }

    }

    private void getSalonist(String userId) {

        mFirestore.collection("salonists")
                .document(userId)
                .get()
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot salonistSnapshot = task.getResult();

                                if (salonistSnapshot.exists()) {

                                    Salonist salon = salonistSnapshot.toObject(Salonist.class);

                                    updateUI(salon);

                                } else {
                                    Toast.makeText(this, "You should update your profile", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(this, "Error occurred getting salonist details: " + task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                );
    }

    private void updateUI(Salonist salon) {
        salonistUsernameTV.setText(salon.getName());

        if (salon.getProfilePicName() != null) {
            StorageReference profileRef = mRef.child(salon.getProfilePicName());

            final long MB = 1024 * 1024;

            profileRef.getBytes(MB)
                    .addOnSuccessListener(
                            bytes -> {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                salonistProfileCIV.setImageBitmap(bitmap);
                            }
                    ).addOnFailureListener(e -> Toast.makeText(this, "Error getting profile picture: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show());
        } else {
            Toast.makeText(this, "You have not uploaded a profile picture", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onGalleryInteraction(DummyContent.DummyItem item) {

    }
}
