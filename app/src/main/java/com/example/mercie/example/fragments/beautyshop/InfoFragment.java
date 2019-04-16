package com.example.mercie.example.fragments.beautyshop;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mercie.example.R;
import com.example.mercie.example.ShopActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class InfoFragment extends Fragment {

    private static final String TAG = "InfoFragment";

    private FirebaseFirestore mDb;
    private StorageReference mRef;

    private String salonId = null;

    //Widgets
    private TextView ownerNameTV, locationTV;
    private ImageView coverImageIV;

    public InfoFragment() {
        mDb = FirebaseFirestore.getInstance();
        mRef = FirebaseStorage.getInstance().getReference().child("shop_cover_images");
    }

    public static InfoFragment newInstance(String shopId) {

        Bundle args = new Bundle();
        args.putString(ShopActivity.SHOP_ID_PARAM, shopId);

        InfoFragment fragment = new InfoFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) salonId = getArguments().getString(ShopActivity.SHOP_ID_PARAM);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_info_fragment, container, false);

        ownerNameTV = view.findViewById(R.id.owner_name_tv);
        locationTV = view.findViewById(R.id.location_tv);
        coverImageIV = view.findViewById(R.id.shop_cover_iv);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //shops
        if (salonId != null) {
            mDb.collection("beauty_shops")
                    .document(salonId)
                    .get()
                    .addOnCompleteListener(
                            task -> {
                                if (task.isSuccessful()) {
                                    if (task.getResult().exists()) {
                                        String coverImageName = task.getResult().getString("imageName");

                                        if (coverImageName != null)
                                            populateCoverImage(coverImageName);

                                        String ownerId = task.getResult().getId();

                                        mDb.collection("shops")
                                                .document(ownerId)
                                                .get()
                                                .addOnSuccessListener(documentSnapshot -> {
                                                    if (documentSnapshot.exists()) {
                                                        String ownerName = documentSnapshot.getString("officialName");

                                                        ownerNameTV.setText(ownerName);
                                                    }
                                                })
                                                .addOnFailureListener(
                                                        e -> {
                                                            Toast.makeText(getActivity(), "Failed to get the owner", Toast.LENGTH_SHORT).show();
                                                        }
                                                );

                                        locationTV.setText(task.getResult().getString("location"));

                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Fatal error occurred", Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, "onViewCreated: Getting Shop Error", task.getException());
                                }
                            }
                    );
        }

    }

    private void populateCoverImage(String coverImageName) {
        StorageReference coverImageRef = mRef.child(coverImageName);
        final long MB = 1024 * 1024;
        coverImageRef.getBytes(MB)
                .addOnSuccessListener(
                        bytes -> {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            coverImageIV.setImageBitmap(bitmap);
                        }
                )
                .addOnFailureListener(e -> {
                    Toast.makeText(getActivity(), "A fatal error occurred", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "populateCoverImage: Getting Image Failure", e);
                });
    }
}
