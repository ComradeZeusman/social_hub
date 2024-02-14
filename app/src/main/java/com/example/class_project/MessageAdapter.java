package com.example.class_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> messageList;
    private Context context;
    private FirebaseAuth auth;

    public MessageAdapter(List<Message> messageList, Context context) {
        this.messageList = messageList;
        this.context = context;
        this.auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);

        // Check if the current user is either the sender or receiver
        if (auth.getCurrentUser() != null &&
                (message.getSenderUid().equals(auth.getCurrentUser().getUid()) ||
                        message.getReceiverUid().equals(auth.getCurrentUser().getUid()))) {
            holder.bind(message);
        } else {
            // If not authorized, hide the item (optional: you can also display a placeholder or handle it differently)
            holder.itemView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        private TextView senderTextView;
        private TextView messageTextView;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            senderTextView = itemView.findViewById(R.id.senderTextView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
        }

        public void bind(Message message) {
            // Bind the message data to the view
            senderTextView.setText("Sender: " + message.getSenderUid());
            messageTextView.setText(message.getMessageText());
        }
    }
}
