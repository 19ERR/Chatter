package com.chatter.activities;

import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chatter.R;
import com.chatter.adapters.MessagesAdapter;
import com.chatter.classes.Message;
import com.chatter.classes.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ConversationActivity extends AppCompatActivity {
    MessagesAdapter messagesAdapter;

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

        FloatingActionButton button = findViewById(R.id.button_send_message);
        button.setOnClickListener(v -> {
            EditText inputEditTextMessage = findViewById(R.id.edit_text_message);
            String messageContent = inputEditTextMessage.getText().toString();

            Message newMessage = new Message(messageContent, User.getEmail());
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference convRef = database.getReference().child("messages").child(conversationKey).push();
            convRef.setValue(newMessage);
            inputEditTextMessage.setText("");
        });

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("messages").child(conversationKey)
                .limitToLast(50);
        FirebaseRecyclerOptions<Message> options =
                new FirebaseRecyclerOptions.Builder<Message>()
                        .setQuery(query, Message.class)
                        .build();
        MessagesAdapter adapter = new MessagesAdapter(options,this);
        adapter.startListening();
        RecyclerView recyclerView = findViewById(R.id.recycle_message_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        //listener care sa notifice adapterul pentru mesaje noi
        /*FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference messagesRef = database.getReference().child("conversations").child(conversationKey).child("messages").getRef();
        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}