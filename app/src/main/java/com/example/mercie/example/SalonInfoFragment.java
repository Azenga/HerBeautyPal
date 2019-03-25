package com.example.mercie.example;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mercie.example.models.Salonist;

public class SalonInfoFragment extends Fragment {
    private Salonist salon = null;

    private TextView salonOwnerTV, salonLocationTV, openFronTV, openToTV, salonWebsiteTV;

    public SalonInfoFragment() {

    }

    public static SalonInfoFragment getInstance(Bundle bundle) {

        SalonInfoFragment salonInfoFragment = new SalonInfoFragment();
        salonInfoFragment.setArguments(bundle);

        return salonInfoFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            salon = savedInstanceState.getParcelable("salon");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_saloninfo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //Init widgets

        salonOwnerTV = view.findViewById(R.id.salon_name_tv);
        salonLocationTV = view.findViewById(R.id.salon_location_tv);
        openFronTV = view.findViewById(R.id.salon_openfrom_tv);
        openToTV = view.findViewById(R.id.salon_opento_tv);
        salonWebsiteTV = view.findViewById(R.id.salon_website_tv);

        //Populate widgets

        salonOwnerTV.setText(salon.getName());
        salonLocationTV.setText(salon.getLocation());
        openFronTV.setText(salon.getOpenFrom());
        openToTV.setText(salon.getOpenTo());
        salonWebsiteTV.setText(salon.getWebsite());
    }
}
