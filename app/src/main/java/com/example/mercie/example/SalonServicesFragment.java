package com.example.mercie.example;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mercie.example.models.Salon;
import com.google.firebase.firestore.FirebaseFirestore;

public class SalonServicesFragment extends Fragment {

    private String salonId = null;

    private FirebaseFirestore mDb;

    public SalonServicesFragment() {
        mDb = FirebaseFirestore.getInstance();
    }

    public static SalonServicesFragment getInstance(String salon) {
        SalonServicesFragment salonServicesFragment = new SalonServicesFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("salonId", salon);
        salonServicesFragment.setArguments(bundle);

        return salonServicesFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            salonId = getArguments().getString("salonId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_salonservices, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}