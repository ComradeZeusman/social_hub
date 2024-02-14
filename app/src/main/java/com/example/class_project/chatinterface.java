package com.example.class_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class chatinterface extends AppCompatActivity implements ChatAdapter.OnChatItemClickListener {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference userRef;
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatinterface);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = findViewById(R.id.toolbar);

        // Display toolbar title
        toolbar.setTitle("Unilia Social hub");

        // Sets the Toolbar to act as the ActionBar for this Activity window
        setSupportActionBar(toolbar);

        // Initialize variables
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());

        if (user == null) {
            // Load the main activity
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        loadChats();
    }

    private void loadChats() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<User> userList = new ArrayList<>();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    User userDetails = userSnapshot.getValue(User.class);
                    if (userDetails != null) {
                        userList.add(userDetails);
                    }
                }
                updateChatList(userList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(chatinterface.this, "Failed to load chats", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateChatList(List<User> userList) {
        chatAdapter = new ChatAdapter(userList, this);
        recyclerView.setAdapter(chatAdapter);
    }

    @Override
    public void onChatItemClick(User chat) {
        Intent intent = new Intent(getApplicationContext(), chatting.class);
        intent.putExtra("username", chat.getUsername());
        intent.putExtra("email", chat.getEmail());
        intent.putExtra("yearOfStudy", chat.getYearOfStudy());
        startActivity(intent);
    }

    // Create the options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Assuming you have defined R.id.newchat and R.id.Logout in your menu resource file
        MenuItem newChatMenuItem = menu.findItem(R.id.newchat);
        MenuItem logoutMenuItem = menu.findItem(R.id.Logout);

        if (newChatMenuItem != null) {
            // Load intent to new chat activity
            newChatMenuItem.setOnMenuItemClickListener(item -> {
                Intent intent = new Intent(getApplicationContext(), ChatSelection.class);
                startActivity(intent);
                return true;
            });
        }

        if (logoutMenuItem != null) {
            // Logout
            logoutMenuItem.setOnMenuItemClickListener(item -> {
                auth.signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            });
        }

        return super.onPrepareOptionsMenu(menu);
    }
}
