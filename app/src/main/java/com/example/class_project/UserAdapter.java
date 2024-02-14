package com.example.class_project;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.class_project.UserDetails;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<UserDetails> userList;

    public UserAdapter(List<UserDetails> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_selecting, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserDetails user = userList.get(position);
        holder.bind(user);

        // Add onClickListeners for the user items
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDetails userDetails = userList.get(position);

                Intent intent = new Intent(v.getContext(), chatting.class);
                intent.putExtra("username", userDetails.getUsername());
                intent.putExtra("email", userDetails.getEmail());
                intent.putExtra("yearOfStudy", userDetails.getYearOfStudy());
               intent.putExtra("uid", userDetails.getUid());

                v.getContext().startActivity(intent);
            }

        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView usernameTextView;
        private TextView emailTextView;
        private TextView yearOfStudyTextView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            yearOfStudyTextView = itemView.findViewById(R.id.yearOfStudyTextView);
        }

        public void bind(UserDetails user) {
            // Bind the user data to the view
            usernameTextView.setText(user.getUsername());
            emailTextView.setText(user.getEmail());
            yearOfStudyTextView.setText(user.getYearOfStudy());
            // Add other bindings as needed
        }
        //setOnClickListeners for the user items


    }
}
