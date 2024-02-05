package com.example.class_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.Manifest;
import android.net.Uri;
import androidx.annotation.Nullable;




public class Add_details extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private static final int REQUEST_PICK_IMAGE = 102;

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference userRef;

    ImageView imageView;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_details);

        // Initialize variables
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());


        imageView = findViewById(R.id.clickableImage);
        imageView.setOnClickListener(v -> openImagePicker());
        saveButton = findViewById(R.id.update);


        if (user == null) {
            // Load the main activity
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }


        // Inside the onCreate method of Add_details activity
        EditText regEditText = findViewById(R.id.editTextRegistrationNumber);
        EditText yearEditText = findViewById(R.id.editTextYearOfStudy);
        EditText semesterEditText = findViewById(R.id.editTextSemester);

// Save the user details to the Firebase Realtime Database
        saveButton.setOnClickListener(v -> {
            // Retrieve the values inside the OnClickListener
            String reg = regEditText.getText().toString().trim();
            String year = yearEditText.getText().toString().trim();
            String semester = semesterEditText.getText().toString().trim();

            // Check if the values are not empty
            if (reg.isEmpty() || year.isEmpty() || semester.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please fill in all details", Toast.LENGTH_SHORT).show();
            } else {
                //retrieve the user's email
                String email = user.getEmail();
                // Update firstprofile to true
                userRef.child("firstprofile").setValue(true);

                // Save user details
                userRef.child("email").setValue(email);
                userRef.child("registrationNumber").setValue(reg);
                userRef.child("yearOfStudy").setValue(year);
                userRef.child("semester").setValue(semester);
                // Save the image URI to the database


                // Load the home activity
                Toast.makeText(getApplicationContext(), "Details saved", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void openImagePicker() {
        // Check runtime permissions for camera access
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        } else {
            // Open the image picker dialog
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_PICK_IMAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission granted, open the camera
            openImagePicker();
        } else {
            // Permission denied, show a message or handle accordingly
            Toast.makeText(this, "Camera permission required", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PICK_IMAGE && data != null) {
                // Handle the selected image URI, you may want to upload it to Firebase storage here
                Uri selectedImageUri = data.getData();
                // Now you can do something with the selected image URI
                //save the image URI to the database
                userRef.child("imageUri").setValue(selectedImageUri.toString());
            }
        }
    }
}

