package com.example.mercie.example;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mercie.example.models.Salon;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SalonActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPagerAdapter adapter;
    private ViewPager viewPager;

    private Salon salon = null;

    //Firebase Variables
    private StorageReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salon);

        //OK SO far
        salon = (Salon) getIntent().getSerializableExtra("salon");

        mRef = FirebaseStorage.getInstance().getReference().child("cover_images");

        ImageView goBackIV = findViewById(R.id.go_back_iv);
        goBackIV.setOnClickListener(view -> {
            startActivity(new Intent(this, SalonsActivity.class));
            finish();
        });

        ImageView coverImageIV = findViewById(R.id.cover_image_iv);

        final long MB = 1024 * 1024;
        //Populate the cover image
        mRef.child(salon.getCoverImage())
                .getBytes(MB)
                .addOnSuccessListener(
                        bytes -> {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            coverImageIV.setImageBitmap(bitmap);
                        }
                )
                .addOnFailureListener(e -> Toast.makeText(this, "Cover Image Error: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());

        toolbar = findViewById(R.id.salon_toolbar);
        viewPager = findViewById(R.id.viewpager);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(SalonInfoFragment.getInstance(salon), "Info");
        adapter.addFragment(SalonServicesFragment.getInstance(salon.getId()), "Services");
        adapter.addFragment(new SalonOffersFragment(), "Offers");
        viewPager.setAdapter(adapter);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }
}



