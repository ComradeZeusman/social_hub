package com.example.class_project;

public class User {
    private String username;
    private String email;
    private boolean firstprofile;
    private String imageUri; // Add this field
    private String registrationNumber; // Add this field
    private String semester; // Add this field
    private String yearOfStudy; // Add this field

    private String SenderUid;

    private String ReceiverUid;

    private int conversationId;

    private String uid;

    // Default constructor required for Firebase
    public User() {
    }

    public User(String picture, String username, String email, boolean firstprofile, String imageUri, String registrationNumber, String semester, String yearOfStudy, int conversationId, String SenderUid, String ReceiverUid) {
        this.username = username;
        this.email = email;
        this.firstprofile = firstprofile;
        this.imageUri = imageUri;
        this.registrationNumber = registrationNumber;
        this.semester = semester;
        this.yearOfStudy = yearOfStudy;
        this.conversationId = conversationId;
        this.SenderUid = SenderUid;
        this.ReceiverUid = ReceiverUid;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getUid() {
        return uid;
    }

    public boolean isFirstprofile() {
        return firstprofile;
    }

    public String getImageUri() {
        return imageUri;
    }

    public int getConversationId() {
        return conversationId;
    }

    public String getSenderUid() {
        return SenderUid;
    }

    public String getReceiverUid() {
        return ReceiverUid;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }
    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getYearOfStudy() {
        return yearOfStudy;
    }

    public void setYearOfStudy(String yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    public void setSenderUid(String SenderUid) {
        this.SenderUid = SenderUid;
    }

    public void setReceiverUid(String ReceiverUid) {
        this.ReceiverUid = ReceiverUid;
    }



}
