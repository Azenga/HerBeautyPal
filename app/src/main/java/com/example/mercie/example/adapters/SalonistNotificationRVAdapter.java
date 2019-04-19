package com.example.mercie.example.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mercie.example.R;
import com.example.mercie.example.fragments.salonist.NotificationsFragment;
import com.example.mercie.example.models.SalonistNotification;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;


public class SalonistNotificationRVAdapter extends RecyclerView.Adapter<SalonistNotificationRVAdapter.ViewHolder> {

    private static final String TAG = "SalonistNotificationRVA";

    private final List<SalonistNotification> notifications;
    private final NotificationsFragment.NotificationsInteractionListener mListener;

    private FirebaseFirestore mDb;

    public SalonistNotificationRVAdapter(List<SalonistNotification> notifications, NotificationsFragment.NotificationsInteractionListener listener) {
        this.notifications = notifications;
        mListener = listener;

        mDb = FirebaseFirestore.getInstance();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_single_salonist_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        SalonistNotification notification = notifications.get(position);

        holder.mNotification = notification;

        mDb.collection("clients")
                .document(notification.getClientId())
                .get()
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {

                                DocumentSnapshot doc = task.getResult();

                                if (doc.exists()) {
                                    holder.clientIdTV.setText(doc.getString("name"));

                                } else {
                                    Log.d(TAG, "onBindViewHolder: The document does not exist");
                                }

                            } else {
                                Log.e(TAG, "onBindViewHolder: Getting Client", task.getException());
                            }
                        }
                );

        holder.serviceNameTV.setText(notification.getServiceName());

        holder.mView.setOnClickListener((View v) -> {
            if (null != mListener) {
                mListener.respondToNotification(notification);
            }

        });
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView clientIdTV;
        public final TextView serviceNameTV;
        public SalonistNotification mNotification;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            clientIdTV = view.findViewById(R.id.client_id_tv);
            serviceNameTV = view.findViewById(R.id.service_name_tv);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + serviceNameTV.getText() + "'";
        }
    }
}
