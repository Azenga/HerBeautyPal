package com.example.mercie.example.fragments.salonist.salon;


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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mercie.example.R;
import com.example.mercie.example.models.Salon;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class InfoFragment extends Fragment {

    private static final String TAG = "InfoFragment";

    private static final String ARG_SALON_PARAM = "salon";

    private Salon salon = null;

    //Widgets

    //ImageView
    private ImageView coverIV;

    //TextViews
    private TextView nameTV, locationTV, contactTV, websiteTV;

    //FirebaseStorage
    private StorageReference mRef;

    public InfoFragment() {
        // Required empty public constructor
        mRef = FirebaseStorage.getInstance().getReference().child("cover_images");
    }

    public static InfoFragment newInstance(Salon salon) {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_SALON_PARAM, salon);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            salon = (Salon) getArguments().getSerializable(ARG_SALON_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        coverIV = view.findViewById(R.id.cover_iv);

        nameTV = view.findViewById(R.id.name_tv);
        locationTV = view.findViewById(R.id.location_tv);
        contactTV = view.findViewById(R.id.name_tv);
        websiteTV = view.findViewById(R.id.website_tv);

        populateFields();

    }

    private void populateFields() {
        if (salon != null) {
            nameTV.setText(salon.getName());
            locationTV.setText(salon.getLocation());
            contactTV.setText(salon.getContact());
            if (salon.getWebsite() != null) websiteTV.setText(salon.getWebsite());

            //Setting the Image

            if (salon.getCoverImage() != null) {
                StorageReference coverImageRef = mRef.child(salon.getCoverImage());

                final long MB = 1024 * 1024;

                coverImageRef.getBytes(MB)
                        .addOnSuccessListener(
                                bytes -> {
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    coverIV.setImageBitmap(bitmap);
                                }
                        )
                        .addOnFailureListener(e -> Log.e(TAG, "populateFields: Loading IMage", e));
            }

        }
    }
}
