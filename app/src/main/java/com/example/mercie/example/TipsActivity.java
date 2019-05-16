package com.example.mercie.example;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mercie.example.models.Tip;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class TipsActivity extends AppCompatActivity {
    private static final String TAG = "TipsActivity";

    private RecyclerView tipsRV;


    private FirebaseFirestore mDb;
    private StorageReference mRef;

    private FirestoreRecyclerAdapter<Tip, TipHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);

        mDb = FirebaseFirestore.getInstance();
        mRef = FirebaseStorage.getInstance().getReference("tips_images");

        Toolbar toolbar = findViewById(R.id.toolbar);
        tipsRV = findViewById(R.id.tips_rv);
        tipsRV.setLayoutManager(new LinearLayoutManager(this));
        tipsRV.setHasFixedSize(true);

        if (getSupportActionBar() == null) setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Tips");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        readTips();

    }

    private void readTips() {

        Query query = mDb.collection("tips").limit(20);

        FirestoreRecyclerOptions<Tip> options = new FirestoreRecyclerOptions.Builder<Tip>()
                .setQuery(query, Tip.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Tip, TipHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull TipHolder holder, int position, @NonNull Tip model) {

                holder.packageNameTV.setText(model.getTipPackage());
                holder.tipDescTV.setText(model.getDescription());

                String imageName = model.getTipImageName();

                if (imageName != null) {
                    StorageReference tipImageRef = mRef.child(model.getTipImageName());

                    final long MB = 1024 * 1024;

                    tipImageRef.getBytes(MB)
                            .addOnSuccessListener(
                                    bytes -> {
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                        holder.tipImageIV.setImageBitmap(bitmap);
                                    }
                            )
                            .addOnFailureListener(e -> Log.e(TAG, "onBindViewHolder: Getting Tip Image", e));
                }

                holder.readMoreTV.setOnClickListener(view -> {
                    Intent intent = new Intent(TipsActivity.this, TipRemedyActivity.class);
                    intent.putExtra(TipRemedyActivity.CURRENT_TIP_PARAM, model);
                    startActivity(intent);
                });

            }

            @NonNull
            @Override
            public TipHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_tip_user_view, viewGroup, false);
                return new TipHolder(view);
            }
        };

        tipsRV.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();

        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        adapter.startListening();
    }

    public static class TipHolder extends RecyclerView.ViewHolder {
        View mView;
        ImageView tipImageIV;
        TextView packageNameTV, tipDescTV, readMoreTV;
        ImageButton favouriteIB;

        public TipHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
            tipImageIV = itemView.findViewById(R.id.tip_image_iv);
            packageNameTV = itemView.findViewById(R.id.package_name_tv);
            tipDescTV = itemView.findViewById(R.id.tip_desc_tv);
            readMoreTV = itemView.findViewById(R.id.read_more_tv);
            favouriteIB = itemView.findViewById(R.id.favourite_ib);
        }
    }
}
