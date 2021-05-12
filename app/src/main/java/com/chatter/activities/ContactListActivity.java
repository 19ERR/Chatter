package com.chatter.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chatter.R;
import com.chatter.adapters.ContactsAdapter;
import com.chatter.classes.Contact;
import com.chatter.classes.Conversation;
import com.chatter.classes.Message;
import com.chatter.classes.User;
import com.chatter.dialogs.AddContactDialog;
import com.chatter.dialogs.InsertConversationTitleDialog;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class ContactListActivity extends AppCompatActivity implements  InsertConversationTitleDialog.finishTitleInsertionDialogListener{
    RecyclerView recyclerView;
    ContactsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        Toolbar toolbar = findViewById(R.id.toolbar_contact_list);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Contacts");
        }

        Query query = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getUid()).child("contacts").limitToLast(100);

        FirebaseRecyclerOptions<Contact> options =
                new FirebaseRecyclerOptions.Builder<Contact>().setQuery(query, Contact.class).build();
        ContactsAdapter adapter = new ContactsAdapter(options);
        adapter.startListening();

        recyclerView = findViewById(R.id.recycle_contact_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        FloatingActionButton buttonStartConversation = findViewById(R.id.button_start_conversation);
        buttonStartConversation.setOnClickListener(v -> startNewConversation());
    }

    public void startNewConversation(){
        String newConversationName = "";
        ArrayList<Contact> selectedContacts = ContactsAdapter.selectedContacts;

        if (selectedContacts.size() == 1) {
            Conversation newConversation = User.getConversations().stream().filter(c -> c.getParticipantsList().contains(selectedContacts.get(0))).findFirst().orElse(null);

            //daca nu exista conversatie existenta
            if (newConversation == null) {
                newConversationName = "private";
                createConversation(newConversationName);
            } else {
                //daca exista du-l la ea
                Intent data = new Intent();
                data.putExtra("conversation_key", newConversation.getKey());
                this.setResult(1, data);
                this.finish();
            }
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            InsertConversationTitleDialog insertConversationTitleDialog = InsertConversationTitleDialog.newInstance();
            insertConversationTitleDialog.show(fragmentManager, "conversation_title");
        }
    }

    public void createConversation(String newConversationName){
        ArrayList<Contact> selectedContacts = ContactsAdapter.selectedContacts;
        //regasire lista de contacte cu chei
        ArrayList<Contact> conversationContacts = new ArrayList<>();
        conversationContacts.add(new Contact(FirebaseAuth.getInstance().getUid(), User.getEmail()));
        for (Contact c : selectedContacts) {
            Contact contact = User.getContacts().stream().filter(co -> c.getEmail().equals(co.getEmail())).findFirst().orElse(null);
            conversationContacts.add(contact);
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Conversation newConversation;
        Message newMessage = new Message("Conversatie creata", "", User.getEmail());
        newConversation = new Conversation(newConversationName, conversationContacts, newMessage);

        DatabaseReference convRef = database.getReference("conversations").push();
        convRef.setValue(newConversation);
        newConversation.setKey(convRef.getKey());

        //adaugare in lista utilizatorilor
        for (Contact c : conversationContacts) {
            DatabaseReference userConvRef = database.getReference("users").child(c.getKey()).child("user_conversations").push();
            userConvRef.setValue(convRef.getKey());
        }

        DatabaseReference newConvRef = database.getReference("conversations").child(convRef.getKey());
        newConvRef.get().addOnCompleteListener(task -> {
            //adaugas un mesag de sistem pentru a evita erori la initierea cu valori null ale adapterului din
            //conversation activity

            DatabaseReference messagesRef = database.getReference().child("messages").child(task.getResult().getKey()).push();
            messagesRef.setValue(newMessage);

            DatabaseReference lastMessageConversationRef = database.getReference().child("conversations").child(task.getResult().getKey()).child("lastMessage");
            lastMessageConversationRef.setValue(newMessage);

            Intent data = new Intent();
            data.putExtra("conversation_key", task.getResult().getKey());
            this.setResult(1, data);
            this.finish();
        });
    }
    @Override
    public void onFinishTitleInsertionDialog(String newConversationTitle) {
        createConversation(newConversationTitle);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void returnNull() {
        Intent data = new Intent();
        data.putExtra("conversation_key", "null");
        this.setResult(0, data);
        this.finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_contact_list, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //your logic
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //your logic
                return true;
            }
        });

        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuAddContact:
                AddContactDialog cdd = new AddContactDialog(this);
                cdd.show();
                break;
        }
        return true;
    }
}