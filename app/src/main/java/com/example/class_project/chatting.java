package com.example.class_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class chatting extends AppCompatActivity {

    FirebaseUser user;
    DatabaseReference userRef, messagesRef;

    FirebaseAuth auth;

    EditText message;
    ImageButton send;

    RecyclerView recyclerViewMessages;
    List<Message> messageList;
    MessageAdapter messageAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        send = findViewById(R.id.buttonSendMessage);
        message = findViewById(R.id.editTextMessageInput);

        auth = FirebaseAuth.getInstance();

        user = auth.getCurrentUser();
        userRef = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        messagesRef = FirebaseDatabase.getInstance().getReference("messages");



        if (user != null) {
            userRef.child("status").setValue("online");
        }


        //set the title of the toolbar


        // Get the intent that started this activity and extract the string
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String email = intent.getStringExtra("email");
        String yearOfStudy = intent.getStringExtra("yearOfStudy");
        String receiverUid = intent.getStringExtra("uid");

        // Set the title of the toolbar to the username of the
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(username);

        // RecyclerView setup
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewMessages.setLayoutManager(layoutManager);
        recyclerViewMessages.setAdapter(messageAdapter);

        // Set an onClickListener for the send button
        send.setOnClickListener(v -> {
            String messageText = message.getText().toString();
            // randomly generated conversation id
            int conversationId = (int) (Math.random() * 1000000);
            if (!messageText.isEmpty()) {
                // Create a new message object with senderUid, receiverUid, and messageText
                Message newMessage = new Message(user.getUid(), receiverUid, messageText, conversationId);
                newMessage.setConversationId(conversationId);

                // Create a composite key by concatenating senderUid and receiverUid
                String compositeKey = user.getUid() + "_" + receiverUid;
                newMessage.setCompositeKey(compositeKey);

                // Push the new message to the database
                DatabaseReference newMessageRef = messagesRef.push();
                newMessageRef.setValue(newMessage);

                // Clear the message input
                message.setText("");
            }
        });
        // Listen for changes in the database and update the RecyclerView accordingly
        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);
                    if (message != null) {
                        messageList.add(message);
                    }
                }
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors if necessary
            }
        });
    }
}
