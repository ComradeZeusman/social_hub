package com.example.class_project;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserDetails {

    FirebaseAuth auth;
    private String uid; // Firebase Authentication UID
    private String username;
    private String email;
    private String yearOfStudy;

    // Constructors, getters, and setters...

    static void getAllUsers(UserCallback userCallback) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<UserDetails> userList = new ArrayList<>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    UserDetails userDetails = userSnapshot.getValue(UserDetails.class);
                    if (userDetails != null) {
                        userDetails.setUid(userSnapshot.getKey()); // Set Firebase Authentication UID
                        userList.add(userDetails);
                    }
                }
                // Retrieve usernames from Realtime Database
                retrieveUsernamesFromRealtimeDatabase(userList, userCallback);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                userCallback.onError(databaseError.getMessage());
            }
        });
    }


    private static void retrieveUsernamesFromRealtimeDatabase(List<UserDetails> userList, UserCallback userCallback) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        for (UserDetails userDetails : userList) {
            String uid = userDetails.getUid();

            // Retrieve the user from Realtime Database based on UID
            usersRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Assuming "username" is the field in your Realtime Database storing the username
                        String username = dataSnapshot.child("username").getValue(String.class);
                        userDetails.setUsername(username != null ? username : "Unknown User");
                    } else {
                        // Handle the case where the user data does not exist in Realtime Database
                        userDetails.setUsername("Unknown User");
                    }

                    // Check if this is the last user to process
                    if (userList.indexOf(userDetails) == userList.size() - 1) {
                        userCallback.onCallback(userList);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle the case where retrieving user data from Realtime Database fails
                    userDetails.setUsername("Unknown User");

                    // Check if this is the last user to process
                    if (userList.indexOf(userDetails) == userList.size() - 1) {
                        userCallback.onCallback(userList);
                    }
                }
            });
        }
    }

    private static String getUsernameFromFirebaseAuthentication(String uid) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        FirebaseUser user = auth.getCurrentUser();

        if (user != null && user.getUid().equals(uid)) {
            // Return the username from Firebase Authentication
            return user.getDisplayName();
        } else {
            // Handle the case where the UID does not match the current user
            return "Unknown User";
        }
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getYearOfStudy() {
        return yearOfStudy;
    }

    public void setYearOfStudy(String yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    public interface UserCallback {
        void onCallback(List<UserDetails> userList);
        void onError(String errorMessage);
    }
}
