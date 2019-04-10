package com.example.mercie.example.fragments.salonist;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mercie.example.R;
import com.example.mercie.example.adapters.AppointmentsRecylcerViewAdapter;
import com.example.mercie.example.models.Appointment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AppointmentsFragment extends Fragment {

    private static final String TAG = "NotificationsFragment";

    private AppointmentsRecylcerViewAdapter adapter;

    private List<Appointment> appointments;

    private FirebaseFirestore mDb;
    private FirebaseAuth mAuth;

    public AppointmentsFragment() {
        appointments = new ArrayList<>();
        mDb = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_salonist_appointment_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            adapter = new AppointmentsRecylcerViewAdapter(appointments, getActivity());
            recyclerView.setAdapter(adapter);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mAuth.getCurrentUser() != null) {
            mDb
                    .collection("salonistappointments")
                    .document(mAuth.getCurrentUser().getUid())
                    .collection("Appointments")
                    .addSnapshotListener(
                            (queryDocumentSnapshots, e) -> {

                                if (e != null) {
                                    Log.e(TAG, "onViewCreated: ", e);
                                    return;
                                }

                                if (!queryDocumentSnapshots.isEmpty()) {

                                    for (DocumentSnapshot ds : queryDocumentSnapshots) {
                                        Appointment appointment = ds.toObject(Appointment.class);
                                        appointment.setId(ds.getId());
                                        appointments.add(appointment);

                                        adapter.notifyDataSetChanged();
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "No Appointments set", Toast.LENGTH_SHORT).show();
                                }

                            }
                    );
        }
    }
}
