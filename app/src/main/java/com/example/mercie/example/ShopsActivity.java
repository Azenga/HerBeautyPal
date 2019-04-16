package com.example.mercie.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.example.mercie.example.adapters.ClientShopsRVAdapter;
import com.example.mercie.example.models.PShop;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ShopsActivity extends AppCompatActivity {

    private static final String TAG = "ShopsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops);

        FirebaseFirestore mDb = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (getSupportActionBar() == null) setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Beauty Shops");

        RecyclerView shopsRV = findViewById(R.id.shops_rv);
        shopsRV.setLayoutManager(new LinearLayoutManager(this));
        shopsRV.setHasFixedSize(true);
        List<PShop> shopList = new ArrayList<>();
        ClientShopsRVAdapter adapter = new ClientShopsRVAdapter(shopList, this);
        shopsRV.setAdapter(adapter);

        mDb.collection("beauty_shops")
                .addSnapshotListener(
                        (queryDocumentSnapshots, e) -> {
                            if (e != null) {
                                Log.e(TAG, "onCreate: Getting SHops Failure", e);
                                return;
                            }

                            if (!queryDocumentSnapshots.isEmpty()) {

                                for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                                    PShop shop = new PShop(
                                            snapshot.getId(),
                                            snapshot.getString("imageName"),
                                            snapshot.getString("name"),
                                            snapshot.getString("location")
                                    );

                                    shopList.add(shop);

                                    adapter.notifyDataSetChanged();
                                }

                            } else {
                                Toast.makeText(this, "No shops Yet", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
    }
}
