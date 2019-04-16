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
import com.example.mercie.example.adapters.SalonistNotificationRVAdapter;
import com.example.mercie.example.models.Notification;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private static final String SALONIST_ID_PARAM = "salonist-id";
    private static final String TAG = "NotificationsFragment";

    private NotificationsInteractionListener mListener;

    private SalonistNotificationRVAdapter adapter;

    private String ownerId;

    private List<Notification> notifications;

    private FirebaseFirestore mDb;

    public NotificationsFragment() {
        notifications = new ArrayList<>();
        mDb = FirebaseFirestore.getInstance();
    }

    @SuppressWarnings("unused")
    public static NotificationsFragment newInstance(String salonistId) {
        NotificationsFragment fragment = new NotificationsFragment();
        Bundle args = new Bundle();
        args.putString(SALONIST_ID_PARAM, salonistId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ownerId = getArguments().getString(SALONIST_ID_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gallery_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            adapter = new SalonistNotificationRVAdapter(notifications, mListener);
            recyclerView.setAdapter(adapter);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDb
                .collection("salonistnotifications")
                .document(ownerId)
                .collection("Notifications")
                .addSnapshotListener(
                        (queryDocumentSnapshots, e) -> {

                            if (e != null) {
                                Log.e(TAG, "onViewCreated: ", e);
                                return;
                            }

                            if (!queryDocumentSnapshots.isEmpty()) {
                                notifications.clear();
                                for (DocumentSnapshot ds : queryDocumentSnapshots) {
                                    Notification notification = ds.toObject(Notification.class);
                                    notification.setId(ds.getId());
                                    notifications.add(notification);
                                    adapter.notifyDataSetChanged();
                                }
                            } else {
                                Toast.makeText(getActivity(), "No notifications", Toast.LENGTH_SHORT).show();
                            }

                        }
                );
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NotificationsInteractionListener) {
            mListener = (NotificationsInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement NotificationsInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface NotificationsInteractionListener {
        void respondToNotification(Notification notification);
    }
}
