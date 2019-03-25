package com.example.mercie.example;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SalonOffersFragment extends Fragment {
    public SalonOffersFragment() {

    }

    public static SalonOffersFragment getInstance(Bundle bundle) {
        SalonOffersFragment salonOffersFragment = new SalonOffersFragment();
        salonOffersFragment.setArguments(bundle);

        return salonOffersFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_two_fragment, container, false);
    }


}
