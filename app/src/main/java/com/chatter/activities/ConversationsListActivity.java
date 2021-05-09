package com.chatter.activities;

import android.content.Intent;
import android.os.Bundle;

import com.chatter.R;
import com.chatter.adapters.ConversationsAdapter;
import com.chatter.classes.Conversation;
import com.chatter.classes.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class ConversationsListActivity extends AppCompatActivity {
    private static final int RC_ADD_CONVERSATION = 9002;

    private ConversationsAdapter conversationsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations_list);

        FloatingActionButton button = findViewById(R.id.button_add_conversation);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent newConversationIntent = new Intent(v.getContext(), ContactListActivity.class);
                startActivityForResult(newConversationIntent, RC_ADD_CONVERSATION);
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar_conversation_list);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Conversatii");
        }

        this.conversationsAdapter =  new ConversationsAdapter();
        RecyclerView recyclerView = findViewById(R.id.recycle_conversation_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(this.conversationsAdapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userConvRef = database.getReference().child("users").child(User.getKey()).child("user_conversations").getRef();
        userConvRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot userConversationSnapshot, @Nullable String previousChildName) {
                String value = userConversationSnapshot.getValue(String.class);

                DatabaseReference conversationsRef = database.getReference().child("conversations").child(value).getRef();
                conversationsRef.keepSynced(true);
                conversationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot conversationSnapshot) {
                        Conversation c = conversationSnapshot.getValue(Conversation.class);
                        c.setKey(conversationSnapshot.getKey());
                        User.getConversations().add(c);
                        conversationsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                conversationsAdapter.notifyDataSetChanged();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_ADD_CONVERSATION) {
            String conversation_key = data.getStringExtra("conversation_key");
            openConversation(conversation_key);
        }
    }

    private void openConversation(String conversation_key){
        Intent conversationIntent = new Intent(this, ConversationActivity.class);
        conversationIntent.putExtra("conversation_key", conversation_key);
        startActivity(conversationIntent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_conversation_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.menuAbout:
                Toast.makeText(this, "You clicked about", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menuSettings:
                Toast.makeText(this, "You clicked settings", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menuLogout:
                Toast.makeText(this, "You clicked logout", Toast.LENGTH_SHORT).show();
                break;

        }
        return true;
    }

}