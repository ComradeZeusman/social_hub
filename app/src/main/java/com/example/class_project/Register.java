package com.example.class_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    TextInputEditText username, email, password, confirmpassword;
    Button btn_register;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView textview;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_background));
            actionBar.hide();
        }


        mAuth = FirebaseAuth.getInstance();
        username = findViewById(R.id.username_input);
        email = findViewById(R.id.email_input);
        password = findViewById(R.id.userpassword_input);
        btn_register = findViewById(R.id.register_button);
        progressBar = findViewById(R.id.progressBar);
        textview = findViewById(R.id.login_text);
        confirmpassword = findViewById(R.id.password_confirm_input);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("users");

        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // User class definition
        class User {
            private String picture;
            private String username;
            private String email;
            private boolean firstprofile;

            public User(String username, String email, boolean b) {
                // Default constructor required for calls to DataSnapshot.getValue(User.class)
            }

            public User(String picture,String username, String email, boolean firstprofile) {
                this.picture = picture;
                this.username = username;
                this.email = email;
                this.firstprofile = firstprofile;
            }

            public String getPicture() {
                return picture;
            }

            public String getUsername() {
                return username;
            }

            public String getEmail() {
                return email;
            }

            public boolean isFirstprofile() {
                return firstprofile;
            }
        }

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);

                String Username = String.valueOf(username.getText());
                String Email = String.valueOf(email.getText());
                String Password = String.valueOf(password.getText());

                if (Username.isEmpty() || Email.isEmpty() || Password.isEmpty()) {
                    Toast.makeText(Register.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                String emailPattern = "^[a-zA-Z0-9._%+-]+@unilia\\.ac\\.mw$";
                if (!Email.matches(emailPattern)) {
                    Toast.makeText(Register.this, "Please enter a school email", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                String ConfirmPassword = String.valueOf(confirmpassword.getText());
                if (!Password.equals(ConfirmPassword)) {
                    Toast.makeText(Register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                } else {
                    String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";
                    if (!Password.matches(passwordPattern)) {
                        Toast.makeText(Register.this, "Password must contain at least 8 characters, including UPPER/lowercase and numbers", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        return;
                    }
                }

                mAuth.createUserWithEmailAndPassword(Email, Password)
                        .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(Username)
                                            .build();

                                    user.updateProfile(profileUpdates);

                                    String userId = user.getUid();

                                    //save display name and firstprofile to firebase database
                                    User newUser = new User("", Username, Email, false);


                                    userRef.child(userId).setValue(newUser);

                                    Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);

                                    //send email of successful registration
                                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(Register.this, "Verification email sent", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    //show error message
                                    Toast.makeText(Register.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        });
    }
}
