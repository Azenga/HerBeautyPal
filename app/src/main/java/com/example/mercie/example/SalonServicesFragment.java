package com.example.mercie.example;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mercie.example.adapters.SalonServicesRecyclerViewAdapter;
import com.example.mercie.example.models.SalonService;
import com.example.mercie.example.models.Salonist;

import java.util.List;

public class SalonServicesFragment extends Fragment {

    private List<SalonService> services;
    private SalonServicesRecyclerViewAdapter salonServicesRecyclerViewAdapter;

    private Salonist salon = null;

    public SalonServicesFragment() {

    }

    public static SalonServicesFragment getInstance(Salonist salon) {
        SalonServicesFragment salonServicesFragment = new SalonServicesFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("salon", salon);
        salonServicesFragment.setArguments(bundle);

        return salonServicesFragment;
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
        View view = inflater.inflate(R.layout.fragment_salonservices, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            salonServicesRecyclerViewAdapter = new SalonServicesRecyclerViewAdapter(services);

            recyclerView.setAdapter(salonServicesRecyclerViewAdapter);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TODO: 3/25/19 Get salons from the firebase firestore and add them to the services list
        // TODO: 3/25/19 Remember to notify the adapter

    }
}