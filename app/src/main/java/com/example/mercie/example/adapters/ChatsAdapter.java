package com.example.mercie.example.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mercie.example.R;
import com.example.mercie.example.models.Message;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ChatHolder> {

    private static final int MSG_TYPE_RIGHT = 0;
    private static final int MSG_TYPE_LEFT = 1;

    private Context context;
    private List<Message> messageList;

    private FirebaseAuth mAuth;

    public ChatsAdapter(Context context, List<Message> messageList) {
        this.context = context;
        this.messageList = messageList;

        mAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = null;

        if (viewType == MSG_TYPE_LEFT) {
            view = LayoutInflater.from(context).inflate(R.layout.left_chat_item, viewGroup, false);
        } else if (viewType == MSG_TYPE_RIGHT) {
            view = LayoutInflater.from(context).inflate(R.layout.right_chat_item, viewGroup, false);
        }

        return new ChatHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHolder chatHolder, int position) {
        Message msg = messageList.get(position);
        chatHolder.chatMsgTV.setText(msg.getContent());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class ChatHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView chatMsgTV;

        public ChatHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
            chatMsgTV = itemView.findViewById(R.id.chat_msg_tv);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Message msg = messageList.get(position);

        String currentUserUid = mAuth.getCurrentUser().getUid();

        if (msg.getSender().equals(currentUserUid)) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}
