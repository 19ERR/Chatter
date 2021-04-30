package com.chatter.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chatter.R;
import com.chatter.adapters.ConversationsAdapter;
import com.chatter.adapters.MessagesAdapter;
import com.chatter.classes.Conversation;
import com.chatter.classes.Message;
import com.chatter.classes.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class ConversationActivity extends AppCompatActivity {
    private Toolbar toolbar;
    User currentUser;
    MessagesAdapter messagesAdapter;
    Conversation currentConversation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        currentUser = getIntent().getParcelableExtra("currentUser");
        String conversationKey = getIntent().getStringExtra("conversation_key");
        currentConversation = currentUser.getConversation(conversationKey);

        Toolbar toolbar = findViewById(R.id.toolbar_conversation_list);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Conversatii");
        }

        this.messagesAdapter =  new MessagesAdapter(currentUser.getConversation(conversationKey).getMessages());
        RecyclerView recyclerView = findViewById(R.id.recycle_message_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(this.messagesAdapter);

        FloatingActionButton button = findViewById(R.id.button_send_message);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText inputEditTextMessage = findViewById(R.id.edit_text_message);
                String messageContent = inputEditTextMessage.getText().toString();

                Message newMessage = new Message(messageContent, currentUser.getKey());
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference convRef = database.getReference().child("conversations").child(currentConversation.getKey()).child("messages").push();
                convRef.setValue(newMessage);
                inputEditTextMessage.setText("");
            }
        });

    }
}