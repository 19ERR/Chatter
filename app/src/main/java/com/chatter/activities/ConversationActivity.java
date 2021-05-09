package com.chatter.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chatter.R;
import com.chatter.adapters.MessagesAdapter;
import com.chatter.classes.Conversation;
import com.chatter.classes.Message;
import com.chatter.classes.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ConversationActivity extends AppCompatActivity {
    MessagesAdapter messagesAdapter;
    Conversation currentConversation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        String conversationKey = getIntent().getStringExtra("conversation_key");

        Toolbar toolbar = findViewById(R.id.toolbar_conversation_list);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Conversatii");
        }

        this.messagesAdapter =  new MessagesAdapter(User.getConversation(conversationKey).getMessagesList());
        RecyclerView recyclerView = findViewById(R.id.recycle_message_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(this.messagesAdapter);

        FloatingActionButton button = findViewById(R.id.button_send_message);
        button.setOnClickListener(v -> {
            EditText inputEditTextMessage = findViewById(R.id.edit_text_message);
            String messageContent = inputEditTextMessage.getText().toString();

            Message newMessage = new Message(messageContent, User.getKey());
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference convRef = database.getReference().child("conversations").child(User.getConversation(conversationKey).getKey()).child("messages").push();
            convRef.setValue(newMessage);
            inputEditTextMessage.setText("");
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference messagesRef = database.getReference().child("conversations").child(conversationKey).child("messages").getRef();

        //adauga listener pentru trigger notifyDataSetChanged pentru actualizarea mesajelor
        messagesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                messagesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}