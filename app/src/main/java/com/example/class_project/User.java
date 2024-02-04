package com.example.class_project;

public class User {
    private String picture;
    private String username;
    private String email;
    private boolean firstprofile;

    // Default constructor required for Firebase
    public User() {
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
