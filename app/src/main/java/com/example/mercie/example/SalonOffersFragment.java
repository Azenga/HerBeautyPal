package com.example.mercie.example;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mercie.example.models.Salonist;

public class SalonOffersFragment extends Fragment {

    private Salonist salon = null;

    public SalonOffersFragment() {

    }

    public static SalonOffersFragment getInstance(Salonist salon) {
        SalonOffersFragment salonOffersFragment = new SalonOffersFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("salon", salon);

        salonOffersFragment.setArguments(bundle);

        return salonOffersFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            salon = (Salonist) getArguments().getSerializable("salon");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_two_fragment, container, false);
    }


}
