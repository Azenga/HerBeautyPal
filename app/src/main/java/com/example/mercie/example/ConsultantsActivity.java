package com.example.mercie.example;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.mercie.example.adapters.ConsultantRecyclerViewAdapter;
import com.example.mercie.example.models.Dermatologist;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ConsultantsActivity extends AppCompatActivity {

    private static final String TAG = "ConsultantsActivity";

    private List<Dermatologist> consultants;
    private FirebaseFirestore mDb;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultants);

        mAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();

        consultants = new ArrayList<>();

        ImageButton goBackIB = findViewById(R.id.go_back_btn);
        goBackIB.setOnClickListener(view -> startActivity(new Intent(this, HomeActivity.class)));

        RecyclerView consultantsRV = findViewById(R.id.consultants_rv);

        consultantsRV.setLayoutManager(new LinearLayoutManager(this));
        consultantsRV.setHasFixedSize(true);

        ConsultantRecyclerViewAdapter adapter = new ConsultantRecyclerViewAdapter(this, consultants);

        consultantsRV.setAdapter(adapter);

        mDb.collection("dermatologists")
                .addSnapshotListener(
                        (queryDocumentSnapshots, e) -> {
                            if (e != null) {
                                Log.e(TAG, "onCreate: Getting Consultants", e);
                                return;
                            }

                            if (queryDocumentSnapshots.isEmpty()) {
                                Toast.makeText(this, "No consultants Registered Yet", Toast.LENGTH_SHORT).show();
                            } else {
                                consultants.clear();
                                for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {

                                    Dermatologist dermatologist = snapshot.toObject(Dermatologist.class);
                                    dermatologist.setId(snapshot.getId());
                                    consultants.add(dermatologist);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }
                );

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
