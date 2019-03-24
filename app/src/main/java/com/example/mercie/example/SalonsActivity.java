package com.example.mercie.example;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.mercie.example.adapters.SalonsRecyclerViewAdapter;
import com.example.mercie.example.models.Salonist;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SalonsActivity extends AppCompatActivity {

    //Widgets
    private RecyclerView salonsRV;

    //Adapter
    private SalonsRecyclerViewAdapter salonsRecyclerViewAdapter;

    //Firebase Variables
    private FirebaseFirestore mDb;
    private FirebaseAuth mAuth;

    //Data
    private List<Salonist> salons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salons);

        //Setting title for the saloon
        Toolbar salonsToolbar = findViewById(R.id.salons_toolbar);
        setSupportActionBar(salonsToolbar);

        getSupportActionBar().setTitle("Salons");

        //Init Variables
        salonsRV = findViewById(R.id.salons_rv);
        mDb = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //Setting LayoutManager for RecyclerView
        salonsRV.setLayoutManager(new LinearLayoutManager(this));

        salons = new ArrayList<>();

        mDb.collection("salonists")
                .get()
                .addOnCompleteListener(
                        task -> {

                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    Salonist salon = document.toObject(Salonist.class);

                                    salons.add(salon);

                                    Toast.makeText(this, salon.toString(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(this, "Error getting the salons: " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                );


        salonsRecyclerViewAdapter = new SalonsRecyclerViewAdapter(this, salons);
        salonsRV.setAdapter(salonsRecyclerViewAdapter);
    }

    //Check whether the current user is authenticated
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