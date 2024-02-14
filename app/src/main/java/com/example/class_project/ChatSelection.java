package com.example.class_project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.class_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ChatSelection extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private RecyclerView recyclerView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_selection);


        // Find the toolbar view inside the activity layout
        Toolbar toolbar = findViewById(R.id.toolbar);

        //display toolbar title
        toolbar.setTitle("Unilia Social hub");

        // Sets the Toolbar to act as the ActionBar for this Activity window
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user == null) {
            // Load the main activity
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Load users into RecyclerView
            loadUsers();
        }
    }

    private void loadUsers() {
        UserDetails.getAllUsers(new UserDetails.UserCallback() {
            @Override
            public void onCallback(List<UserDetails> userList) {
                // Initialize and set the adapter for the RecyclerView
                UserAdapter userAdapter = new UserAdapter(userList);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onError(String errorMessage) {
                // Handle error
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.select_menu, menu);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Assuming you have defined R.id.newchat and R.id.Logout in your menu resource file

        MenuItem newChatMenuItem = menu.findItem(R.id.back);
        MenuItem refreshMenuItem = menu.findItem(R.id.refresh);

        if (newChatMenuItem != null) {
            //load intent to new chat activity
            newChatMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Intent intent = new Intent(getApplicationContext(), chatinterface.class);
                    startActivity(intent);
                    return true;
                }
            });
        }

        if (refreshMenuItem != null) {
            //logout
            refreshMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    loadUsers();
                    return true;
                }
            });
        }

        return super.onPrepareOptionsMenu(menu);
    }


}
