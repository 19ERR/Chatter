package com.chatter.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chatter.R;
import com.chatter.adapters.ConversationsAdapter;
import com.chatter.classes.Conversation;
import com.chatter.classes.User;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ConversationsListActivity extends AppCompatActivity {
    private static final int RC_ADD_CONVERSATION = 9002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations_list);

        FloatingActionButton button = findViewById(R.id.button_add_conversation);
        button.setOnClickListener(v -> {
            Intent newConversationIntent = new Intent(v.getContext(), ContactListActivity.class);
            startActivityForResult(newConversationIntent, RC_ADD_CONVERSATION);
        });

        Toolbar toolbar = findViewById(R.id.toolbar_conversation_list);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Conversatii");
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference convRef = database.getReference("conversations");

        /*Query query = convRef.orderByKey();

        FirebaseRecyclerOptions<Conversation> options =
                new FirebaseRecyclerOptions.Builder<Conversation>().setQuery(query, Conversation.class).build();
        ConversationsAdapter conversationsAdapter = new ConversationsAdapter(options);
        conversationsAdapter.startListening();

        RecyclerView recyclerView;
        recyclerView = findViewById(R.id.recycle_conversation_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(conversationsAdapter);*/
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_ADD_CONVERSATION && resultCode == 1) {
            String conversation_key = data.getStringExtra("conversation_key");
            openConversation(conversation_key);
        }
    }

    private void openConversation(String conversation_key) {
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

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuAbout:
                Toast.makeText(this, "You clicked about", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menuSettings:
                Toast.makeText(this, "You clicked settings", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menuLogout:
                User.logOut();
                finish();
                break;

        }
        return true;
    }

}