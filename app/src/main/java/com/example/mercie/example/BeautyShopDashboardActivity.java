package com.example.mercie.example;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mercie.example.fragments.beautyshop.AddProductFragment;
import com.example.mercie.example.fragments.beautyshop.BeautyShopDetailsFragment;
import com.example.mercie.example.fragments.beautyshop.BeautyShopHomeFragment;
import com.example.mercie.example.fragments.beautyshop.BeautyShopProductsFragment;
import com.example.mercie.example.fragments.beautyshop.BeautyShopReservationFragment;
import com.example.mercie.example.models.Shop;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class BeautyShopDashboardActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, BeautyShopProductsFragment.BeautyProductsListener {

    private CircleImageView profileIV;
    private TextView usernameTV;

    //Firebase Variables
    private FirebaseAuth mAuth;
    private FirebaseFirestore mDb;
    private StorageReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beauty_shop_dashboard);

        //Init FirebaseVariables
        mAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();
        mRef = FirebaseStorage.getInstance().getReference().child("avatars");


        Toolbar toolbar = findViewById(R.id.shop_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("Beauty Shop");

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

        replaceFragment(new BeautyShopHomeFragment());
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (fragment != null) transaction.replace(R.id.container, fragment).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                replaceFragment(new BeautyShopHomeFragment());

                if (getSupportActionBar() != null)
                    getSupportActionBar().setTitle("Profile");
                break;
            case R.id.nav_shop_details:
                replaceFragment(new BeautyShopDetailsFragment());

                if (getSupportActionBar() != null)
                    getSupportActionBar().setTitle("Shop Details");
                break;

            case R.id.nav_products:
                replaceFragment(new BeautyShopProductsFragment());

                if (getSupportActionBar() != null)
                    getSupportActionBar().setTitle("Products");
                break;

            case R.id.nav_notifications:
                replaceFragment(new BeautyShopReservationFragment());

                if (getSupportActionBar() != null)
                    getSupportActionBar().setTitle("Notifications");
                break;

            case R.id.nav_reservations:
                replaceFragment(new BeautyShopReservationFragment());

                if (getSupportActionBar() != null)
                    getSupportActionBar().setTitle("Reservations");
                break;

            case R.id.nav_logout:
                logout();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void logout() {
        if (mAuth.getCurrentUser() != null) mAuth.signOut();
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
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            startActivity(new Intent(this, SigninAsActivity.class));
            finish();
        } else {
            getUserDetails(user.getUid());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getUserDetails(String uid) {

        //Getting the shop owner details

        mDb.collection("shops")
                .document(uid)
                .get()
                .addOnCompleteListener(
                        task -> {

                            if (task.isSuccessful()) {

                                if (task.getResult().exists()) {
                                    Shop shop = task.getResult().toObject(Shop.class);

                                    updateUI(shop);

                                } else {
                                    Toast.makeText(this, "You have not finished setting up you profile please updated it", Toast.LENGTH_SHORT).show();
                                }

                            } else {

                                if (task.getException().getLocalizedMessage() != null)
                                    Toast.makeText(this, "Error: " + task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }

                        }
                );

    }

    private void updateUI(Shop shop) {
        usernameTV.setText(shop.getOfficialName());

        if (shop.getProfileImageName() != null) {

            StorageReference profileImageRef = mRef.child(shop.getProfileImageName());

            final long MB = 1024 * 1024;

            profileImageRef.getBytes(MB)
                    .addOnSuccessListener(
                            bytes -> {

                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                profileIV.setImageBitmap(bitmap);

                            }
                    ).addOnFailureListener(e -> Toast.makeText(this, "Failed to retrieve the image: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show());

        } else {
            Toast.makeText(this, "No image avatar uploaded", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void startAddProduct() {

        replaceFragment(new AddProductFragment());

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("Add Product");
    }
}
