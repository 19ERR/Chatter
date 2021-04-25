package com.chatter.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.chatter.R;
import com.chatter.adapters.ContactsAdapter;
import com.chatter.classes.Contact;
import com.chatter.classes.Conversation;
import com.chatter.classes.User;
import com.chatter.dialogs.AddContactDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.stream.Collectors;

public class ContactListActivity extends AppCompatActivity {
    public User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        currentUser = getIntent().getParcelableExtra("currentUser");

        Toolbar toolbar = findViewById(R.id.toolbar_contact_list);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Contacts");
        }

        RecyclerView recyclerView = findViewById(R.id.recycle_contact_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ContactsAdapter(currentUser.getContacts(), this));

        FloatingActionButton buttonStartConversation = findViewById(R.id.button_start_conversation);
        buttonStartConversation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                List<Contact> selectedContacts;
                selectedContacts = currentUser.getContacts().stream().filter(Contact::isSelected).collect(Collectors.toList());
                Conversation newConversation;
                String newConversationName = "";

                if(selectedContacts.size() == 1){
                    newConversation = currentUser.getConversations().stream().filter(c -> c.getParticipants().contains(selectedContacts.get(0))).findFirst().orElse(null);

                    if(newConversation == null) {
                        newConversationName = selectedContacts.get(0).getEmail();
                    }
                } else {
                    //popup pentru numele conversatiei
                    newConversationName = "Conversatie noua";
                }
                newConversation = new Conversation(newConversationName, selectedContacts);
                currentUser.getConversations().add(newConversation);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference userConvRef = database.getReference("users").child(currentUser.getKey()).child("conversations").push();
                newConversation.setKey(userConvRef.getKey());
                userConvRef.child("name").setValue(newConversation.getName());
                userConvRef.child("participants").setValue(newConversation.getParticipants());
                userConvRef.child("key").setValue(newConversation.getKey());

                DatabaseReference convRef = database.getReference("conversations").push();
                convRef.child("name").setValue(newConversation.getName());
                convRef.child("participants").setValue(newConversation.getParticipants());
                convRef.child("key").setValue(newConversation.getKey());
                convRef.child("messages").setValue(newConversation.getMessages());
                newConversation.setKey(convRef.getKey());
                convRef.setValue(newConversation);

                Intent data = new Intent();
                Activity activity = ((ContactListActivity)v.getContext());
                data.putExtra("conversation",newConversation);
                activity.setResult(1,data);
                activity.finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_contact_list, menu);
        return true;
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.menuAddContact:
                AddContactDialog cdd=new AddContactDialog(this, this.currentUser);
                cdd.show();
                break;
        }
        return true;
    }

}