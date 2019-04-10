package com.example.mercie.example;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mercie.example.fragments.client.AboutFragment;
import com.example.mercie.example.fragments.client.EditProfileFragment;
import com.example.mercie.example.fragments.client.CheckReservationDetailsFragment;
import com.example.mercie.example.fragments.client.HomePageFragment;
import com.example.mercie.example.fragments.client.NotificationFragment;
import com.example.mercie.example.models.Appointment;
import com.example.mercie.example.models.Client;
import com.example.mercie.example.models.Reservation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        NotificationFragment.ClientNotificationsLIstener {

    //Firebase Stuff
    private FirebaseAuth mAuth;
    private StorageReference mStoreRef;
    private FirebaseFirestore mFirestore;

    //Widgets
    private ImageView profileIV;
    private TextView usernameTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        mStoreRef = FirebaseStorage.getInstance().getReference().child("avatars");
        mFirestore = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.shop_toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View navHeaderView = navigationView.getHeaderView(0);

        profileIV = navHeaderView.findViewById(R.id.profile_civ);
        usernameTV = navHeaderView.findViewById(R.id.username_tv);

        displayFrag(R.id.nav_homepage);

    }

    public void displayFrag(int id) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment frag = null;

        switch (id) {
            case R.id.nav_edit_profile:
                getSupportActionBar().setTitle("Edit Profile");
                frag = new EditProfileFragment();
                break;
            case R.id.nav_homepage:
                getSupportActionBar().setTitle("Home");
                frag = new HomePageFragment();
                break;
            case R.id.nav_notifications:
                getSupportActionBar().setTitle("Notifications");
                frag = new NotificationFragment();
                break;
            case R.id.nav_about:
                getSupportActionBar().setTitle("About");
                frag = new AboutFragment();
                break;
            case R.id.nav_reservations:
                getSupportActionBar().setTitle("Reservations");
                frag = new CheckReservationDetailsFragment();
                break;
            default:
                logout();
        }

        if (frag != null) {
            ft.replace(R.id.container, frag);
            ft.addToBackStack(null);
            ft.commit();
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
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_rateUs) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        displayFrag(item.getItemId());

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            startActivity(new Intent(this, SigninAsActivity.class));
            finish();
        } else {
            updateUI(user.getUid());
        }
    }

    private void updateUI(String userUid) {

        DocumentReference clientRef = mFirestore.collection("clients").document(userUid);
        clientRef.get()
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot clientSnapshot = task.getResult();

                                if (clientSnapshot.exists()) {
                                    Client client = clientSnapshot.toObject(Client.class);
                                    updateHeaderWidgets(client);
                                } else {
                                    Toast.makeText(this, "You haven't updated your profile", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(this, "Error getting client data: " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                );

    }

    private void updateHeaderWidgets(Client client) {
        usernameTV.setText(client.getName());

        if (client.getImageUrl() != null) {
            StorageReference profileRef = mStoreRef.child(client.getImageUrl());

            final long MB = 1024 * 1024;

            profileRef.getBytes(MB)
                    .addOnSuccessListener(
                            bytes -> {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                profileIV.setImageBitmap(bitmap);
                            }
                    ).addOnFailureListener(e -> Toast.makeText(this, "Error getting image: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show());
        } else {
            Toast.makeText(this, "You have not uploaded a profile picture", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void respondToNotification(Reservation reservation) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Checkout Reservation");
        builder.setMessage("Would you like to accept this reservation?");

        if (mAuth.getCurrentUser() != null) {

            builder.setPositiveButton(
                    "Accept", (dialog, which) -> {
                        mFirestore.collection("clientsreservations")
                                .document(mAuth.getCurrentUser().getUid())
                                .collection("Reservations")
                                .document(reservation.getId())
                                .update("agreedUpon", true);

                        //Set Appointment With Admin
                        mFirestore.collection("salonistappointments")
                                .document(reservation.getSalonistId())
                                .collection("Appointments")
                                .add(new Appointment(mAuth.getCurrentUser().getUid(), reservation.getItem(), reservation.getDate(), reservation.getTime()));


                        Toast.makeText(HomeActivity.this, "Reservation Accepted", Toast.LENGTH_SHORT).show();

                    }
            )
                    .setNegativeButton("Reject", (dialog, which) -> {

                                mFirestore.collection("clientsreservations")
                                        .document(mAuth.getCurrentUser().getUid())
                                        .collection("Reservations")
                                        .document(reservation.getId())
                                        .delete();

                                Toast.makeText(HomeActivity.this, "Reservation Rejected", Toast.LENGTH_SHORT).show();


                            }
                    );

            builder.show();
        }

    }
}
