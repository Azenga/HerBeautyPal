package com.example.mercie.example.adapters;

import android.content.Context;
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

import com.example.mercie.example.R;
import com.example.mercie.example.models.Dermatologist;
import com.example.mercie.example.models.Message;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<MessageRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "MessageRecyclerViewAdap";
    private Context context;

    private List<Message> messageList;

    private FirebaseFirestore mDb;
    private StorageReference mRef;

    public MessageRecyclerViewAdapter(List<Message> messageList) {
        this.messageList = messageList;
        mDb = FirebaseFirestore.getInstance();
        mRef = FirebaseStorage.getInstance().getReference().child("avatars");
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
        Message message = messageList.get(i);

        viewHolder.msgCountTV.setText(messageList.size() + " messages");

        //Setting username and avatar

        String from = message.getFromId();

        mDb.collection("dermatologists")
                .document(from)
                .get()
                .addOnSuccessListener(
                        documentSnapshot -> {

                            if (documentSnapshot.exists()) {

                                Dermatologist dermatologist = documentSnapshot.toObject(Dermatologist.class);

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
                                    Toast.makeText(context, "No user IMage", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(context, "Bunch of Jebrish", Toast.LENGTH_SHORT).show();
                            }
                        }
                )
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "An error occurred", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onBindViewHolder: Failed Getting Dermatologist", e);
                });

        viewHolder.mView.setOnClickListener(view -> {

        });

    }

    @Override
    public int getItemCount() {
        return messageList.size();
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
