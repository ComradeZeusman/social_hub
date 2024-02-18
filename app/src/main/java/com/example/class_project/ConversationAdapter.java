package com.example.class_project;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;


public class ConversationAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> messageList;
    private Context context;
    private FirebaseAuth auth;

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
        Log.d("MessageAdapter", "Updated with " + messageList.size() + " messages"); // Add this line
        notifyDataSetChanged();
    }

    public ConversationAdapter(List<Message> messageList, Context context) {
        this.messageList = messageList;
        this.context = context;
        this.auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false);
        return new MessageAdapter.MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MessageViewHolder holder, int position) {
        Message message = messageList.get(position);

        // Check if the current user is either the sender or receiver
        // Check if the message is not null and if the current user is either the sender or receiver
        if (message != null && auth.getCurrentUser() != null &&
                (message.getSenderUid().equals(auth.getCurrentUser().getUid()) ||
                        message.getReceiverUid().equals(auth.getCurrentUser().getUid()))) {
            holder.bind(message);
        } else {
            // If not authorized or message is null, hide the item (optional: you can also display a placeholder or handle it differently)
            holder.itemView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        Log.d("MessageAdapter", "Item count requested: " + messageList.size()); // Add this line
        return messageList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        private TextView messageText;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messageTextView);
        }

        public void bind(Message message) {
            messageText.setText(message.getMessage());
        }
    }
}
