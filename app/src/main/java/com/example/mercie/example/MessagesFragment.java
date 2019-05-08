package com.example.mercie.example;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mercie.example.adapters.ChatsThreadsAdapter;
import com.example.mercie.example.models.Dermatologist;
import com.example.mercie.example.models.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MessagesFragment extends Fragment {

    private static final String TAG = "MessagesFragment";

    //Widgets
    private RecyclerView messagesRV;
    private List<Dermatologist> dermatologists;
    private List<String> uidsList;


    private FirebaseFirestore mDb;
    private FirebaseAuth mAuth;

    public MessagesFragment() {
        // Required empty public constructor
        mAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();
        dermatologists = new ArrayList<>();
        uidsList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        messagesRV = view.findViewById(R.id.messages_rv);
        messagesRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        messagesRV.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String currentUser = mAuth.getCurrentUser().getUid();

        mDb.collection("chats")
                .addSnapshotListener(
                        (queryDocumentSnapshots, e) -> {
                            if (e != null) {
                                Log.e(TAG, "onViewCreated: Getting uids of chats with current user", e);
                                return;
                            }

                            if (queryDocumentSnapshots.isEmpty()) {
                                Toast.makeText(getActivity(), "No chats yet", Toast.LENGTH_SHORT).show();
                            } else {
                                uidsList.clear();
                                for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                                    Message msg = snapshot.toObject(Message.class);
                                    if (msg.getSender().equals(currentUser)) {
                                        uidsList.add(msg.getReceiver());
                                    }
                                    if (msg.getReceiver().equals(currentUser)) {
                                        uidsList.add(msg.getSender());
                                    }
                                }
                            }
                        }
                );

        mDb.collection("dermatologists")
                .addSnapshotListener(
                        (queryDocumentSnapshots, e) -> {
                            if (e != null) {
                                Log.e(TAG, "onViewCreated: Getting dermatologist that chatted with user", e);
                                return;
                            }

                            if (queryDocumentSnapshots.isEmpty()) {
                                Toast.makeText(getActivity(), "No Consultant chats yet", Toast.LENGTH_SHORT).show();
                            } else {
                                dermatologists.clear();
                                for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                                    Dermatologist dermatologist = snapshot.toObject(Dermatologist.class);
                                    dermatologist.setId(snapshot.getId());

                                    for (String uid : uidsList) {
                                        if (dermatologist.getId().equals(uid)) {
                                            if (!dermatologists.contains(dermatologist))
                                                dermatologists.add(dermatologist);
                                        }
                                    }
                                }

                                ChatsThreadsAdapter adapter = new ChatsThreadsAdapter(dermatologists);
                                messagesRV.setAdapter(adapter);

                            }
                        }
                );
    }
}
