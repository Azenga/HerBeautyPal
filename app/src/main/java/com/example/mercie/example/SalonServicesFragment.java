package com.example.mercie.example;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SalonServicesFragment extends Fragment {
    public SalonServicesFragment() {

    }

    public static SalonServicesFragment getInstance(Bundle bundle) {
        SalonServicesFragment salonServicesFragment = new SalonServicesFragment();
        salonServicesFragment.setArguments(bundle);

        return salonServicesFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_salonservices, container, false);
    }


}