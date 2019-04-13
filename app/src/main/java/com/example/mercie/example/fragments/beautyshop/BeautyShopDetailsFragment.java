package com.example.mercie.example.fragments.beautyshop;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mercie.example.EditShopDetailsActivity;
import com.example.mercie.example.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * A simple {@link Fragment} subclass.
 */
public class BeautyShopDetailsFragment extends Fragment {

    private static final String TAG = "BeautyShopDetailsFragme";

    //Widgets
    private ImageView shopCoverImageIV;
    private TextView shopNameTV, locationTV;
    private Button editShopBtn;

    //Firebase Variables
    private FirebaseAuth mAuth;
    private FirebaseFirestore mDb;
    private StorageReference mRef;

    public BeautyShopDetailsFragment() {
        // Required empty public constructor
        mAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();
        mRef = FirebaseStorage.getInstance().getReference().child("shop_cover_images");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beauty_shop_details, container, false);

        shopCoverImageIV = view.findViewById(R.id.shop_cover_iv);
        shopNameTV = view.findViewById(R.id.shop_name_tv);
        locationTV = view.findViewById(R.id.shop_location_tv);
        editShopBtn = view.findViewById(R.id.edit_shop_btn);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        editShopBtn.setOnClickListener(v -> startActivity(new Intent(getActivity(), EditShopDetailsActivity.class)));
    }

    private void updateUI(String uid) {

        mDb.collection("beauty_shops")
                .document(uid)
                .get()
                .addOnSuccessListener(
                        documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                shopNameTV.setText(documentSnapshot.getString("name"));
                                locationTV.setText(documentSnapshot.getString("location"));

                                String imageName = documentSnapshot.getString("imageName");

                                if (imageName != null) {
                                    final long MB = 1024 * 1024;
                                    mRef.child(imageName)
                                            .getBytes(MB)
                                            .addOnSuccessListener(
                                                    bytes -> {
                                                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                                        shopCoverImageIV.setImageBitmap(bitmap);
                                                    }
                                            )
                                            .addOnFailureListener(e -> Log.e(TAG, "updateUI: Getting Image", e));
                                } else {
                                    Log.i(TAG, "updateUI: No Image");
                                }
                            }
                        }
                )
                .addOnFailureListener(e -> Log.e(TAG, "updateUI: Getting Shop", e));

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            updateUI(user.getUid());
        }
    }
}
