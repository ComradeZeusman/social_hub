// ChatAdapter.java
package com.example.class_project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<User> chatList;
    private OnChatItemClickListener listener;

    public ChatAdapter(List<User> chatList, OnChatItemClickListener listener) {
        this.chatList = chatList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        User chat = chatList.get(position);
        holder.bind(chat);
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public interface OnChatItemClickListener {
        void onChatItemClick(User chat);
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        private TextView chatTitleTextView;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            chatTitleTextView = itemView.findViewById(R.id.chatTitleTextView);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onChatItemClick(chatList.get(position));
                    }
                }
            });
        }

        public void bind(User chat) {
            chatTitleTextView.setText(chat.getUsername());
        }
    }
}
