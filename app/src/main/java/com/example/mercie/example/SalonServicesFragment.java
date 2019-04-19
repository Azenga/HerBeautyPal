package com.example.mercie.example;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mercie.example.adapters.SalonServicesRecyclerViewAdapter;
import com.example.mercie.example.models.SalonService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SalonServicesFragment extends Fragment {

    private String salonId = null;

    private static final String TAG = "SalonServicesFragment";

    private FirebaseFirestore mDb;

    private SalonServiceFragmentListener mListener;

    private RecyclerView servicesRV;
    private List<SalonService> services;
    private SalonServicesRecyclerViewAdapter adapter;

    private FirebaseAuth mAuth;

    public SalonServicesFragment() {
        mAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();
        services = new ArrayList<>();
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

        servicesRV = view.findViewById(R.id.services_rv);
        servicesRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new SalonServicesRecyclerViewAdapter(services, mListener);
        servicesRV.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mAuth.getCurrentUser() != null) {
            mDb.collection("services").document(salonId).collection("Services")
                    .addSnapshotListener((queryDocumentSnapshots, e) -> {

                        if (e != null) {
                            Log.e(TAG, "onViewCreated: Getting services", e);
                            return;
                        }

                        if (!queryDocumentSnapshots.isEmpty()) {

                            for (DocumentSnapshot dc : queryDocumentSnapshots.getDocuments()) {
                                SalonService service = dc.toObject(SalonService.class);
                                services.add(service);

                                adapter.notifyDataSetChanged();
                            }

                        } else {
                            Toast.makeText(getActivity(), "No Services Yet", Toast.LENGTH_SHORT).show();
                        }

                    });
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof SalonServiceFragmentListener)
            mListener = (SalonServiceFragmentListener) context;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface SalonServiceFragmentListener {
        void RequestSalonService(SalonService service);
    }
}