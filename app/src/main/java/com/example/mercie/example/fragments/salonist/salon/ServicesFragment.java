package com.example.mercie.example.fragments.salonist.salon;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mercie.example.R;
import com.example.mercie.example.adapters.SalonistServicesRecyclerViewAdapter;
import com.example.mercie.example.models.SalonService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ServicesFragment extends Fragment {

    private static final String SALON_ID_PARAM = "salon-id";
    public static final String TAG = "ServicesFragment";

    private String salonId = null;
    private List<SalonService> services;

    //Widgets
    private SalonistServicesRecyclerViewAdapter adapter;

    private OnServiceFragInteraction mListener;
    private FirebaseFirestore mDb;
    private FirebaseAuth mAth;

    public ServicesFragment() {
        services = new ArrayList<>();
        mDb = FirebaseFirestore.getInstance();
        mAth = FirebaseAuth.getInstance();
    }

    public static ServicesFragment newInstance(String id) {
        ServicesFragment fragment = new ServicesFragment();
        Bundle args = new Bundle();
        args.putString(SALON_ID_PARAM, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) salonId = getArguments().getString(SALON_ID_PARAM);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.salon_fragment_services, container, false);

        RecyclerView servicesRV = view.findViewById(R.id.services_rv);
        adapter = new SalonistServicesRecyclerViewAdapter(services);
        servicesRV.setAdapter(adapter);

        FloatingActionButton addServiceBtn = view.findViewById(R.id.add_service_fab);
        addServiceBtn.setOnClickListener(v -> {
            if (mListener != null) mListener.loadAddFragmentService(salonId);
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (mAth.getCurrentUser() != null) {

            mDb.collection("services")
                    .document(salonId)
                    .collection("Services")
                    .addSnapshotListener((queryDocumentSnapshots, e) -> {

                        if (e != null) {
                            Log.e(TAG, "onViewCreated: Getting services", e);
                            return;
                        }

                        if (!queryDocumentSnapshots.isEmpty()) {
                            services.clear();
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
        if (context instanceof OnServiceFragInteraction)
            mListener = (OnServiceFragInteraction) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnServiceFragInteraction {
        void loadAddFragmentService(String salonId);
    }
}
