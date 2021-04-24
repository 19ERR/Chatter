package com.chatter.activities;

import android.content.Intent;
import android.os.Bundle;

import com.chatter.R;
import com.chatter.classes.Contact;
import com.chatter.classes.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class ConversationsListActivity extends AppCompatActivity {
    private static final int RC_ADD_CONVERSATION = 9002;
    private Toolbar toolbar;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations_list);

        currentUser = getIntent().getParcelableExtra("currentUser");

        FloatingActionButton button = findViewById(R.id.button_add_conversation);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent newConversationIntent = new Intent(v.getContext(), ContactListActivity.class);
                newConversationIntent.putExtra("currentUser", currentUser);
                startActivityForResult(newConversationIntent, RC_ADD_CONVERSATION);
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar_conversation_list);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Conversatii");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_ADD_CONVERSATION) {
            Contact contact = data.getParcelableExtra("contact");

            Intent conversationIntent = new Intent(this, ConversationActivity.class);
            conversationIntent.putExtra("currentUser", currentUser);
            conversationIntent.putExtra("currentUser", currentUser);///TODO: de pus obiectul conversatiei create
            startActivity(conversationIntent);
            Toast.makeText(this,contact.getEmail(),Toast.LENGTH_LONG).show();
        }
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