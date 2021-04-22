package com.chatter.activities;

import android.content.Intent;
import android.os.Bundle;

import com.chatter.R;
import com.chatter.activities.ContactListActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

public class ConversationsListActivity extends AppCompatActivity {
    private static final int RC_ADD_CONVERSATION = 9002;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations_list);

        GoogleSignInAccount account = (GoogleSignInAccount) getIntent().getParcelableExtra("account");

        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.button_add_conversation);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent newConversationIntent = new Intent(v.getContext(), ContactListActivity.class);
                startActivityForResult(newConversationIntent, RC_ADD_CONVERSATION);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_ADD_CONVERSATION) {
            //du-te la conversatia creata
        }
    }
}