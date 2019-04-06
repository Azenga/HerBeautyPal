package com.example.mercie.example;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.mercie.example.adapters.SalonsRecyclerViewAdapter;
import com.example.mercie.example.models.Salon;
import com.example.mercie.example.models.Salonist;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SalonsActivity extends AppCompatActivity {


    private static final String TAG = "SalonsActivity";

    //Widgets
    private RecyclerView salonsRV;
    //Adapter
    private SalonsRecyclerViewAdapter salonsRecyclerViewAdapter;
    //Items list
    private List<Salon> salons;

    //Firebase variables
    private FirebaseFirestore mDb;
    private FirebaseAuth mAuth;


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

        salonsRecyclerViewAdapter = new SalonsRecyclerViewAdapter(this, salons);
        salonsRV.setAdapter(salonsRecyclerViewAdapter);

        mDb.collection("salons")
                .addSnapshotListener(
                        (value, e) -> {

                            if (e != null) {
                                Log.e(TAG, "onCreate: ", e);
                                return;
                            }

                            for (QueryDocumentSnapshot doc : value) {

                                Salon salon = doc.toObject(Salon.class);
                                salon.setId(doc.getId());

                                salons.add(salon);

                                Log.d(TAG, "onCreate: " + salon.toString());

                                salonsRecyclerViewAdapter.notifyDataSetChanged();
                            }

                        }
                );
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