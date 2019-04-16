package com.example.mercie.example.fragments.client;

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
import com.example.mercie.example.adapters.ReservationsRVAdapter;
import com.example.mercie.example.models.Reservation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mercie on 2/27/2019.
 */

public class NotificationFragment extends Fragment {

    private static final String TAG = "NotificationFragment";

    private RecyclerView reservationsRV;
    private ReservationsRVAdapter adapter;
    private ClientNotificationsLIstener mListener;
    private List<Reservation> reservations;

    private FirebaseFirestore mDb;
    private FirebaseAuth mAuth;

    public NotificationFragment() {
        reservations = new ArrayList<>();
        mDb = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_notifications, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            adapter = new ReservationsRVAdapter(reservations, mListener);

            recyclerView.setAdapter(adapter);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Get the notifications Now
        if (mAuth.getCurrentUser() != null) {
            mDb.collection("clientsreservations")
                    .document(mAuth.getCurrentUser().getUid())
                    .collection("Reservations")
                    .whereEqualTo("agreedUpon", false)
                    .addSnapshotListener(
                            (queryDocumentSnapshots, e) -> {
                                if (e != null) {
                                    Log.e(TAG, "onViewCreated: ", e);
                                    return;
                                }

                                if (!queryDocumentSnapshots.isEmpty()) {
                                    reservations.clear();
                                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                                        Reservation reservation = doc.toObject(Reservation.class);
                                        reservation.setId(doc.getId());
                                        reservations.add(reservation);
                                        adapter.notifyDataSetChanged();
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "No notifications", Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof ClientNotificationsLIstener) {
            mListener = (ClientNotificationsLIstener) context;
        }
    }

    public interface ClientNotificationsLIstener {

        void respondToNotification(Reservation reservation);

    }
}
