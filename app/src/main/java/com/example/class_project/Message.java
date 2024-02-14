package com.example.class_project;

public class Message {
    private String senderUid;
    private String messageText;

    private String receiverUid;

    // Required default constructor for Firebase
    public Message() {
    }

    // Constructor with parameters
    public Message(String senderUid, String receiverUid,String messageText) {
        this.senderUid = senderUid;
        this.messageText = messageText;
        this.receiverUid = receiverUid;
    }

    // Getters and setters
    public String getSenderUid() {
        return senderUid;
    }

    public String getReceiverUid() {
        return receiverUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public void setReceiverUid(String receiverUid) {
        this.receiverUid = receiverUid;
    }
}
