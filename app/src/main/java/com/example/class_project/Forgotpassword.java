package com.example.class_project;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_background));
        }


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
}
