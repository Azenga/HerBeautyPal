package com.example.mercie.example.fragments.salonist;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mercie.example.R;
import com.example.mercie.example.fragments.salonist.salon.InfoFragment;
import com.example.mercie.example.fragments.salonist.salon.ServicesFragment;
import com.example.mercie.example.models.Salon;

/**
 * A simple {@link Fragment} subclass.
 */
public class MySalonFragment extends Fragment implements ServicesFragment.OnServiceFragInteraction {

    private static final String SALON_PARAM = "salon";
    private BottomNavigationView.OnNavigationItemSelectedListener bnvlistener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            // TODO: 4/3/19 Switch to correct Fragment
            switch (menuItem.getItemId()) {

                case R.id.bnv_nav_info:
                    changeFragment(InfoFragment.newInstance(mSalon));
                    return true;

                case R.id.bnv_nav_services:
                    changeFragment(ServicesFragment.newInstance(mSalon.getId()));
                    return true;

                case R.id.bnv_nav_offers:

                    return true;

                default:
                    return false;
            }
        }
    };
    private Salon mSalon = null;


    public MySalonFragment() {
        // Required empty public constructor
    }

    public static MySalonFragment getInstance(Salon salon) {
        MySalonFragment fragment = new MySalonFragment();
        Bundle args = new Bundle();
        args.putSerializable(SALON_PARAM, salon);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mSalon = (Salon) getArguments().getSerializable(SALON_PARAM);
        }
    }

    public void changeFragment(Fragment frag) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (frag != null) transaction.replace(R.id.frag_container, frag).commit();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_salon, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        BottomNavigationView bvn = view.findViewById(R.id.my_salon_bnv);

        bvn.setOnNavigationItemSelectedListener(bnvlistener);
        changeFragment(InfoFragment.newInstance(mSalon));
    }

    @Override
    public void loadAddFragmentService() {
        Toast.makeText(getActivity(), "Was called", Toast.LENGTH_SHORT).show();
        changeFragment(new SalonistAddServiceFragment());
    }
}
