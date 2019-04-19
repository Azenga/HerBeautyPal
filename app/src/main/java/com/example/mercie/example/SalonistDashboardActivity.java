package com.example.mercie.example;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mercie.example.fragments.salonist.AppointmentsFragment;
import com.example.mercie.example.fragments.salonist.NotificationsFragment;
import com.example.mercie.example.fragments.salonist.SalonistEditProfileFragment;
import com.example.mercie.example.fragments.salonist.SalonistHomeFragment;
import com.example.mercie.example.models.Reservation;
import com.example.mercie.example.models.SalonistNotification;
import com.example.mercie.example.models.Salon;
import com.example.mercie.example.models.Salonist;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class SalonistDashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        SalonistHomeFragment.ProfileFragListener,
        NotificationsFragment.NotificationsInteractionListener {

    private static final String TAG = "SalonistDashboardActivi";
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

        Toolbar toolbar = findViewById(R.id.shop_toolbar);
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

        salonistProfileCIV = navHeaderView.findViewById(R.id.profile_civ);
        salonistUsernameTV = navHeaderView.findViewById(R.id.username_tv);
    }

    //Changes the SalonistDashboardActivity Fragment according to the selected Navigation Item
    public void displayFragment(Fragment fragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (fragment != null) {
            transaction.replace(R.id.container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.

        switch (item.getItemId()) {
            case R.id.nav_home:
                if (getSupportActionBar() != null)
                    getSupportActionBar().setTitle("Home");
                if (mAuth.getCurrentUser() != null)
                    getSalonist(mAuth.getCurrentUser().getUid());
                break;

            case R.id.nav_my_salon:
                getTheSalonAndSwitch();
                break;

            case R.id.nav_appointments:
                if (getSupportActionBar() != null)
                    getSupportActionBar().setTitle("Appointments");
                    displayFragment(new AppointmentsFragment());
                break;

            case R.id.nav_notifications:
                getSupportActionBar().setTitle("Notifications");
                displayFragment(NotificationsFragment.newInstance(mAuth.getCurrentUser().getUid()));
                break;

            case R.id.nav_feedback:
                getSupportActionBar().setTitle("Feedback");
                // TODO: 3/19/19 Add salonist Feedback
                break;

            case R.id.nav_logout:
                logout();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getTheSalonAndSwitch() {

        if (mAuth.getCurrentUser() != null) {

            mFirestore.collection("salons")
                    .document(mAuth.getCurrentUser().getUid())
                    .get()
                    .addOnSuccessListener(
                            documentSnapshot -> {
                                if (documentSnapshot.exists()) {

                                    Salon salon = documentSnapshot.toObject(Salon.class);
                                    salon.setId(documentSnapshot.getId());

                                    Intent intent = new Intent(this, MySalonActivity.class);
                                    intent.putExtra(MySalonActivity.SALON_PARAM, salon);
                                    startActivity(intent);

                                } else {
                                    View view = findViewById(android.R.id.content);
                                    Snackbar.make(view, "Register A Salon", Snackbar.LENGTH_LONG)
                                            .setAction("RegisterSalon", (View v) -> {
                                                startActivity(new Intent(this, RegisterSalonActivity.class));
                                            })
                                            .show();
                                }
                            }
                    )
                    .addOnFailureListener(e -> Log.e(TAG, "getTheSalonAndSwitch: Failed", e));
        }
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

    public void getSalonist(String userId) {

        mFirestore.collection("salonists")
                .document(userId)
                .get()
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot salonistSnapshot = task.getResult();

                                if (salonistSnapshot.exists()) {

                                    Salonist salon = salonistSnapshot.toObject(Salonist.class);

                                    displayFragment(SalonistHomeFragment.newInstance(salon));
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
    public void openEditFragment(Salonist salonist) {
        displayFragment(new SalonistEditProfileFragment());
    }


    @Override
    public void respondToNotification(SalonistNotification notification) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Request Action");
        builder.setMessage("Accept the request and set Reservation?");

        builder.setPositiveButton(
                "Accept", (dialog, which) -> {

                    //Get Time
                    String date = notification.getRequestedDate();
                    String time = notification.getRequestedTime();

                    Reservation reservation = new Reservation("Service", notification.getServiceName(), time, date,notification.getSalonName(),  mAuth.getCurrentUser().getUid());
                    reservation.setAgreedUpon(false);

                    mFirestore.collection("clientsreservations")
                            .document(notification.getClientId())
                            .collection("Reservations")
                            .add(reservation)
                            .addOnCompleteListener(
                                    task -> {
                                        if (task.isSuccessful()) {


                                            mFirestore
                                                    .collection("salonistnotifications")
                                                    .document(mAuth.getCurrentUser().getUid())
                                                    .collection("Notifications")
                                                    .document(notification.getId())
                                                    .delete();

                                            Toast.makeText(this, "Reservation Send", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(this, SalonistDashboardActivity.class));
                                            finish();
                                        } else {

                                            Toast.makeText(this, "Error: " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                                        }
                                    }
                            );


                }
        ).setNegativeButton(
                "Reject", (dialog, which) -> {

                    if (mAuth.getCurrentUser() != null) {
                        mFirestore
                                .collection("salonistnotifications")
                                .document(mAuth.getCurrentUser().getUid())
                                .collection("Notifications")
                                .document(notification.getId())
                                .delete();

                        Toast.makeText(SalonistDashboardActivity.this, "Request Dropped", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        builder.show();
    }
}
