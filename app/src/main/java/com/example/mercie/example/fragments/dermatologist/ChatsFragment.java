package com.example.mercie.example.fragments.dermatologist;

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

import com.example.mercie.example.R;
import com.example.mercie.example.adapters.ConsultantChatsThreadsAdapter;
import com.example.mercie.example.models.Client;
import com.example.mercie.example.models.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {

    private static final String TAG = "ChatsFragment";

    private List<Client> chattedCLientsList;

    private FirebaseFirestore mDb;
    private FirebaseAuth mAuth;

    private List<String> chattedWithClientUids;
    private RecyclerView chatsRV;

    public ChatsFragment() {
        // Required empty public constructor
        mDb = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        chattedCLientsList = new ArrayList<>();
        chattedWithClientUids = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dermatologist_fragment_chats, container, false);

        chatsRV = view.findViewById(R.id.messagesRV);
        chatsRV.setHasFixedSize(true);
        chatsRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        String currentUser = mAuth.getCurrentUser().getUid();

        mDb.collection("chats")
                .addSnapshotListener(
                        (queryDocumentSnapshots, e) -> {
                            if (e != null) {
                                Log.e(TAG, "onViewCreated: Getting Chats", e);
                                return;
                            }

                            if (queryDocumentSnapshots.isEmpty()) {
                                Toast.makeText(getActivity(), "No chats yet", Toast.LENGTH_SHORT).show();
                            } else {
                                chattedWithClientUids.clear();
                                for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                                    Message msg = snapshot.toObject(Message.class);
                                    if (msg.getSender().equals(currentUser)) {
                                        chattedWithClientUids.add(msg.getReceiver());
                                    }
                                    if (msg.getReceiver().equals(currentUser)) {
                                        chattedWithClientUids.add(msg.getSender());
                                    }
                                }
                            }
                        }
                );

        mDb.collection("clients")
                .addSnapshotListener(
                        (queryDocumentSnapshots, e) -> {
                            if (e != null) {
                                Log.e(TAG, "onViewCreated: Getting clients that chatted with consultant", e);
                                return;
                            }

                            if (queryDocumentSnapshots.isEmpty()) {
                                Toast.makeText(getActivity(), "No clients chats yet", Toast.LENGTH_SHORT).show();
                            } else {
                                chattedCLientsList.clear();
                                for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                                    Client client = snapshot.toObject(Client.class);
                                    client.setUid(snapshot.getId());

                                    for (String uid : chattedWithClientUids) {
                                        if (client.getUid().equals(uid)) {
                                            if (!chattedCLientsList.contains(client))
                                                chattedCLientsList.add(client);
                                        }
                                    }
                                }

                                ConsultantChatsThreadsAdapter adapter = new ConsultantChatsThreadsAdapter(chattedCLientsList);
                                chatsRV.setAdapter(adapter);

                            }
                        }
                );
    }
}
