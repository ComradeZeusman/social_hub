package com.example.class_project;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Forgotpassword extends AppCompatActivity {

    EditText email;
    Button send;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        Toolbar toolbar = findViewById(R.id.toolbar);

        // Display toolbar title
        toolbar.setTitle("Unilia Social hub");

        // Sets the Toolbar to act as the ActionBar for this Activity window
        setSupportActionBar(toolbar);


        email = findViewById(R.id.editTextEmail);
        send = findViewById(R.id.btnForgotPassword);

        // Initialize FirebaseAuth instance
        firebaseAuth = FirebaseAuth.getInstance();

        // Check if the email exists in the database
        send.setOnClickListener(v -> {
            firebaseAuth.sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(Forgotpassword.this, "Password sent to your email", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Forgotpassword.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Forgotpassword.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.back_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Assuming you have defined R.id.newchat and R.id.Logout in your menu resource file
        MenuItem newChatMenuItem = menu.findItem(R.id.back);


        if (newChatMenuItem != null) {
            // Load intent to new chat activity
            newChatMenuItem.setOnMenuItemClickListener(item -> {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                return true;
            });
        }


        return super.onPrepareOptionsMenu(menu);
    }
}


