package com.example.class_project;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//import user class
import com.example.class_project.User;
public class Home extends AppCompatActivity {


    FirebaseAuth auth;
    Button button;
    TextView textView;
    FirebaseUser user;
    DatabaseReference userRef;

    ImageView imageView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_background));
            actionBar.hide();
        }

        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.Logout);
        textView = findViewById(R.id.User_details);
        imageView = findViewById(R.id.profileIcon);

        user = auth.getCurrentUser();


        if (user == null) {
            // Load the main activity
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        } else {


            // Check for firstprofile in the Firebase Realtime Database
            userRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Use the User class directly
                        User userData = dataSnapshot.getValue(User.class);
                        if (userData != null && !userData.isFirstprofile()) {
                            // Show a toast message if firstprofile is false
                            Intent intent = new Intent(getApplicationContext(), Add_details.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Continue to Home.java
                            //display the registered user details
                            assert userData != null;
                            String picture = userData.getImageUri();
                            String name = user.getDisplayName();
                            textView.setText("Welcome " + name);

                            // Load the profile picture
                            //convert the image uri to a URL
                            Uri imageUri = Uri.parse(picture);
                            //load the image using Glide
                            if (imageUri != null) {
                                Glide.with(Home.this).load(imageUri).into(imageView);
                                    Toast.makeText(Home.this, "Image found", Toast.LENGTH_SHORT).show();
                            } else {
                                //show error message
                                Toast.makeText(Home.this, "Error: Image not found", Toast.LENGTH_SHORT).show();
                            }



                            //wait for 5 seconds then start new intent
                            new CountDownTimer(5000, 1000) {
                                public void onTick(long millisUntilFinished) {
                                    // Do nothing on tick
                                }

                                public void onFinish() {
                                    // Load the MainActivity2 after 5 seconds
                                    Intent intent = new Intent(getApplicationContext(), chatinterface.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }.start();


                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle error
                    Toast.makeText(Home.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        button.setOnClickListener(v -> {
           // Logout the user
            auth.signOut();
            // Load the main activity
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();

            Toast.makeText(Home.this, "Logged out", Toast.LENGTH_SHORT).show();
        });
    }

   //create the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    //handle the menu item click

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.newchat){
            // Load the profile activity
            Toast.makeText(this, "New chat", Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId() == R.id.Logout){
            // Load the about activity
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    public void chat(View view) {
    }
}