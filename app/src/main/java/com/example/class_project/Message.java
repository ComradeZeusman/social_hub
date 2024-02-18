package com.example.class_project;

public class Message {
    private String senderUid;
    private String messageText;

    private String receiverUid;

    private int conversationId;

    private String compositeKey;


    // Required default constructor for Firebase
    public Message() {
    }



    // Constructor with parameters
    public Message(String senderUid, String receiverUid,String messageText, int conversationId) {
        this.senderUid = senderUid;
        this.messageText = messageText;
        this.receiverUid = receiverUid;
        this.conversationId = conversationId;

    }

    // Getters and setters
    public String getSenderUid() {
        return senderUid;
    }

    public String getReceiverUid() {
        return receiverUid;
    }

    public String getCompositeKey() {
        return compositeKey;
    }

    public int getConversationId() {
        return conversationId;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessage() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public void setReceiverUid(String receiverUid) {
        this.receiverUid = receiverUid;
    }

    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }

    public void setCompositeKey(String compositeKey) {
        this.compositeKey = compositeKey;
    }
}
