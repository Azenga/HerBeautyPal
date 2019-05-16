package com.example.mercie.example;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mercie.example.adapters.ChatsAdapter;
import com.example.mercie.example.models.Client;
import com.example.mercie.example.models.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConsultantMessageActivity extends AppCompatActivity {

    public static final String RECEIVER_UID = "receiver-uid";
    private static final String TAG = "MessageActivity";

    private CircleImageView profileCIV;
    private RecyclerView chatsRV;

    private EditText msgET;
    private TextView nameTV;
    private ImageButton sendBtn;

    private Toolbar toolbar;

    private String mReceiver = null;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mDb;
    private StorageReference mRef;

    private List<Message> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        mAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();
        mRef = FirebaseStorage.getInstance().getReference("avatars");

        mReceiver = getIntent().getStringExtra(RECEIVER_UID);

        initComponents();

        if (getSupportActionBar() == null) setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        sendBtn.setOnClickListener(view -> sendMessage());
    }

    private void sendMessage() {
        String msg = String.valueOf(msgET.getText());

        if (msg == null) {
            msgET.setError("You can't send an empty message");
            msgET.requestFocus();
            return;
        }

        Map<String, Object> chatMap = new HashMap<>();
        chatMap.put("sender", mAuth.getCurrentUser().getUid());
        chatMap.put("receiver", mReceiver);
        chatMap.put("content", msg);
        chatMap.put("timestamp", FieldValue.serverTimestamp());

        mDb.collection("chats")
                .add(chatMap)
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show();
                                msgET.setText("");
                            } else {
                                Toast.makeText(this, "Message not sent", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
    }

    private void initComponents() {
        profileCIV = findViewById(R.id.profile_civ);

        chatsRV = findViewById(R.id.chats_rv);
        chatsRV.setLayoutManager(new LinearLayoutManager(this));
        chatsRV.setHasFixedSize(true);

        msgET = findViewById(R.id.msg_et);
        nameTV = findViewById(R.id.name_tv);
        sendBtn = findViewById(R.id.send_btn);
        toolbar = findViewById(R.id.toolbar);

        messageList = new ArrayList<>();
    }


    private void updateUI() {


        mDb.document("clients/" + mReceiver)
                .addSnapshotListener(
                        (documentSnapshot, e) -> {

                            if (e != null) {
                                Log.e(TAG, "updateUI: Getting Username", e);
                                return;
                            }

                            if (documentSnapshot.exists()) {
                                Client client = documentSnapshot.toObject(Client.class);

                                nameTV.setText(client.getName());

                                if (client.getImageUrl() != null) {

                                    StorageReference profilePicRef = mRef.child(client.getImageUrl());
                                    final long MB = 1024 * 1024;

                                    profilePicRef.getBytes(MB)
                                            .addOnSuccessListener(
                                                    bytes -> {
                                                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                                        profileCIV.setImageBitmap(bitmap);
                                                    }
                                            )
                                            .addOnFailureListener(
                                                    error -> {
                                                        Log.e(TAG, "updateUI: Getting Image", error);
                                                        Toast.makeText(this, "Error getting the  Image", Toast.LENGTH_SHORT).show();
                                                    }
                                            );
                                }
                            }

                        }
                );

        readMessages(mAuth.getCurrentUser().getUid(), mReceiver);
    }

    private void readMessages(String currentUser, String otherUser) {

        mDb.collection("chats")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener(
                        (queryDocumentSnapshots, e) -> {
                            if (e != null) {
                                Log.e(TAG, "readMessages: Error", e);
                                return;
                            }

                            if (queryDocumentSnapshots.isEmpty()) {
                                Toast.makeText(this, "No chats yet", Toast.LENGTH_SHORT).show();
                            } else {
                                messageList.clear();
                                for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                                    Message msg = snapshot.toObject(Message.class);

                                    if ((msg.getSender().equals(currentUser)) && (msg.getReceiver().equals(otherUser))) {
                                        messageList.add(msg);
                                    }
                                    if ((msg.getSender().equals(otherUser)) && (msg.getReceiver().equals(currentUser))) {
                                        messageList.add(msg);
                                    }
                                }

                                ChatsAdapter adapter = new ChatsAdapter(this, messageList);
                                chatsRV.setAdapter(adapter);
                            }
                        }
                );
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            startActivity(new Intent(this, SigninAsActivity.class));
        } else {
            updateUI();
        }
    }
}
