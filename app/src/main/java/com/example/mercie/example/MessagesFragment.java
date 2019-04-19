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

import com.example.mercie.example.adapters.MessageRecyclerViewAdapter;
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
    private List<Message> messages;


    private FirebaseFirestore mDb;
    private FirebaseAuth mAuth;
    private MessageRecyclerViewAdapter adapter;

    public MessagesFragment() {
        // Required empty public constructor
        mAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();
        messages = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        messagesRV = view.findViewById(R.id.messages_rv);
        messagesRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        messagesRV.setHasFixedSize(true);

        adapter = new MessageRecyclerViewAdapter(messages);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDb.collection("chats")
                .whereEqualTo("fromId", mAuth.getCurrentUser().getUid())
                .addSnapshotListener(
                        (queryDocumentSnapshots, e) -> {
                            if (e != null) {
                                Log.e(TAG, "onViewCreated: ", e);
                                return;
                            }

                            if (queryDocumentSnapshots.isEmpty()) {
                                Toast.makeText(getActivity(), "No chats Yet", Toast.LENGTH_SHORT).show();
                            } else {
                                messages.clear();
                                for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                                    Message msg = snapshot.toObject(Message.class);

                                    messages.add(msg);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }
                );

    }
}
