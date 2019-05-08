package com.example.mercie.example.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mercie.example.MessageActivity;
import com.example.mercie.example.R;
import com.example.mercie.example.models.Dermatologist;
import com.example.mercie.example.models.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsThreadsAdapter extends RecyclerView.Adapter<ChatsThreadsAdapter.ViewHolder> {
    private static final String TAG = "MessageRecyclerViewAdap";
    private Context context;

    private List<Dermatologist> dermatologists;
    private List<Message> messageList;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mDb;
    private StorageReference mRef;

    public ChatsThreadsAdapter(List<Dermatologist> dermatologists) {
        this.dermatologists = dermatologists;

        mAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();
        mRef = FirebaseStorage.getInstance().getReference().child("avatars");

        messageList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.view_message_thread, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String currentUser = mAuth.getCurrentUser().getUid();
        Dermatologist dermatologist = dermatologists.get(i);

        /**
         * Setting username and avatar
         */

        viewHolder.usernameTV.setText(dermatologist.getOfficialName());

        if (dermatologist.getProfilePicName() != null) {
            StorageReference avatarRef = mRef.child(dermatologist.getProfilePicName());

            final long MB = 1024 * 1024;
            avatarRef.getBytes(MB)
                    .addOnSuccessListener(
                            bytes -> {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                viewHolder.avatarCIV.setImageBitmap(bitmap);
                            }
                    )
                    .addOnFailureListener(
                            e -> {
                                Toast.makeText(context, "Failed Loading Image", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "onBindViewHolder: Loading Image Failure", e);
                            }
                    );
        } else {
            Toast.makeText(context, "No user Image", Toast.LENGTH_SHORT).show();
        }

        /**
         * Getting the number of chats between the two users
         */
        mDb.collection("chats")
                .addSnapshotListener(
                        (queryDocumentSnapshots, e) -> {
                            if (e != null) {
                                Log.e(TAG, "onBindViewHolder: Getting Message Count", e);
                                return;
                            }

                            if (queryDocumentSnapshots.isEmpty()) {
                                Toast.makeText(context, "No chats yet", Toast.LENGTH_SHORT).show();
                            } else {
                                messageList.clear();
                                for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                                    Message msg = snapshot.toObject(Message.class);

                                    if ((msg.getSender().equals(currentUser)) && (msg.getReceiver().equals(dermatologist.getId()))) {
                                        messageList.add(msg);
                                    }
                                    if ((msg.getSender().equals(dermatologist.getId())) && (msg.getReceiver().equals(currentUser))) {
                                        messageList.add(msg);
                                    }
                                }

                                viewHolder.msgCountTV.setText(messageList.size() + " messages");
                            }
                        }
                );


        viewHolder.mView.setOnClickListener(view -> {
            if (dermatologist.getId() != null) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra(MessageActivity.RECEIVER_UID, dermatologist.getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dermatologists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        CircleImageView avatarCIV;
        TextView usernameTV, msgCountTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
            avatarCIV = itemView.findViewById(R.id.profile_civ);
            usernameTV = itemView.findViewById(R.id.username_tv);
            msgCountTV = itemView.findViewById(R.id.msg_count_tv);
        }
    }
}
