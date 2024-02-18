package com.example.class_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class conversation extends AppCompatActivity {

    FirebaseUser user;
    DatabaseReference userRef, messagesRef;

    FirebaseAuth auth;

    EditText message;
    ImageButton send;

    RecyclerView recyclerViewMessages;
    List<Message> messageList;
    MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        if (user != null) {
            userRef.child("status").setValue("online");
        }

        send = findViewById(R.id.buttonSendMessage);
        message = findViewById(R.id.editTextMessageInput);

        auth = FirebaseAuth.getInstance();

        user = auth.getCurrentUser();
        userRef = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        messagesRef = FirebaseDatabase.getInstance().getReference("messages");

        Intent intent = getIntent();
        int conversationId = intent.getIntExtra("conversationId", -1); // -1 is the default value if the key is not found
        String senderUid = intent.getStringExtra("SenderUid");
        String receiverUid = intent.getStringExtra("ReceiverUid");

        Log.d("conversation", "conversationId in chat: " + conversationId);
        Toast.makeText(conversation.this, "conversationId: " + conversationId, Toast.LENGTH_SHORT).show();
        Log.d("conversation", "SenderUid in chat: " + senderUid);
        Log.d("conversation", "ReceiverUid in chat: " + receiverUid);

        // Set the title of the toolbar to the username of the
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // RecyclerView setup
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewMessages.setLayoutManager(layoutManager);
        recyclerViewMessages.setAdapter(messageAdapter);

        // Load messages
        loadMessages(conversationId, senderUid, receiverUid);
    }

    private void loadMessages(int conversationId, String senderUid, String receiverUid) {
        DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference("messages");

        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Message> messageList = new ArrayList<>();

                for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                    Message message = messageSnapshot.getValue(Message.class);
                    if (message != null && message.getConversationId() == conversationId
                            && ((message.getSenderUid().equals(senderUid) && message.getReceiverUid().equals(receiverUid))
                            || (message.getSenderUid().equals(receiverUid) && message.getReceiverUid().equals(senderUid)))) {
                        messageList.add(message);
                    } else {
                        Log.d("Conversation", "Message not added: " + message);
                        if (message != null) {
                            Log.d("Conversation", "Message conversationId: " + message.getConversationId());
                            Log.d("Conversation", "Message senderUid: " + message.getSenderUid());
                            Log.d("Conversation", "Message receiverUid: " + message.getReceiverUid());
                        }
                    }
                }

                messageAdapter.setMessageList(messageList);

                //toast conversationId
                Toast.makeText(conversation.this, "conversationId: " + conversationId, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }
}
