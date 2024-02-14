package com.example.class_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    TextInputEditText email, password;
    Button btn_login;
    FirebaseAuth mAuth;

    ProgressBar progressBar;

    FirebaseUser user;

    TextView textview;




    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

// Check if the user is already logged in
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            //check if email is verified
            if (user.isEmailVerified()) {
            Intent intent = new Intent(getApplicationContext(), Home.class);
            startActivity(intent);
            finish();
} else {
                Toast.makeText(MainActivity.this, "Please verify your email address", Toast.LENGTH_SHORT).show();
            }
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_background));
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.hide();
        }


        mAuth = FirebaseAuth.getInstance();
        // initialize the variables
        email = findViewById(R.id.Email_name_input);
        password = findViewById(R.id.UseRpassword_input);
        btn_login = findViewById(R.id.login_button);
        progressBar = findViewById(R.id.progressBar);
        textview = findViewById(R.id.register_text);
        // set onclick listener for the login text
        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
                finish();
            }
        });

        // set onclick listener for the login button
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                progressBar.setVisibility(View.VISIBLE);

                String Email, Password;
                Email = String.valueOf(email.getText());
                Password = String.valueOf(password.getText());

                // check if the user has entered the username, email, and password
                if (Email.isEmpty() || Password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                mAuth.signInWithEmailAndPassword(Email, Password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                       //check if email is verified
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    assert user != null;
                                    if (user.isEmailVerified()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Intent intent = new Intent(getApplicationContext(), Home.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(MainActivity.this, "Please verify your email address", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    // If sign in fails, display a message to the user.

                                    //show error message
                                    Toast.makeText(MainActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });

    }

    public void onForgotPasswordClick(View view) {
Intent intent = new Intent(getApplicationContext(), Forgotpassword.class);
        startActivity(intent);
        finish();

    }
}