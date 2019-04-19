package com.example.mercie.example.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mercie.example.R;
import com.example.mercie.example.fragments.client.NotificationFragment;
import com.example.mercie.example.models.Reservation;
import com.example.mercie.example.models.Salon;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ReservationsRVAdapter extends RecyclerView.Adapter<ReservationsRVAdapter.ViewHolder> {
    private static final String TAG = "ReservationsRVAdapter";
    private List<Reservation> reservations;
    private NotificationFragment.ClientNotificationsLIstener mListener;
    private Context context;

    private FirebaseFirestore mDb;

    public ReservationsRVAdapter(List<Reservation> reservations, NotificationFragment.ClientNotificationsLIstener mListener) {
        this.reservations = reservations;
        this.mListener = mListener;

        mDb = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.view_single_client_reservation, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        Reservation reservation = reservations.get(position);

        viewHolder.typeTV.setText(reservation.getType());
        viewHolder.itemTV.setText(reservation.getItem());
        viewHolder.timeTV.setText(reservation.getTime() + ' ' + reservation.getDate());
        viewHolder.salonTV.setText(reservation.getSalonName());

        mDb.collection("salons")
                .document(reservation.getSalonistId())
                .get()
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                Salon salon = task.getResult().toObject(Salon.class);
                                viewHolder.locationTV.setText(salon.getLocation());
                            } else {
                                Log.e(TAG, "onBindViewHolder: Getting Location", task.getException());
                            }
                        }
                );

        viewHolder.mView.setOnClickListener(view -> {
            if (mListener != null)
                mListener.respondToNotification(reservation);
        });

    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView typeTV, itemTV, timeTV, salonTV, locationTV;
        View mView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            typeTV = itemView.findViewById(R.id.type_tv);
            itemTV = itemView.findViewById(R.id.item_tv);
            timeTV = itemView.findViewById(R.id.time_tv);
            salonTV = itemView.findViewById(R.id.salon_tv);
            locationTV = itemView.findViewById(R.id.location_tv);
        }
    }
}
