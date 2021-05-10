package com.chatter.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.chatter.R;
import com.chatter.adapters.ContactsAdapter;
import com.chatter.classes.Contact;
import com.chatter.classes.Conversation;
import com.chatter.classes.Message;
import com.chatter.classes.User;
import com.chatter.dialogs.AddContactDialog;
import com.chatter.dialogs.InsertConversationNameDialog;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ContactListActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        Toolbar toolbar = findViewById(R.id.toolbar_contact_list);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Contacts");
        }

        Query query = FirebaseDatabase.getInstance().getReference("users").child(User.getKey()).child("contacts").limitToLast(100);

        FirebaseRecyclerOptions<Contact> options =
                new FirebaseRecyclerOptions.Builder<Contact>().setQuery(query, Contact.class).build();
        ContactsAdapter adapter = new ContactsAdapter(options);
        adapter.startListening();

        recyclerView = findViewById(R.id.recycle_contact_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        FloatingActionButton buttonStartConversation = findViewById(R.id.button_start_conversation);
        buttonStartConversation.setOnClickListener(v -> {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            ArrayList<Contact> selectedContacts = ContactsAdapter.selectedContacts;

            Conversation newConversation;
            String newConversationName = "";

            if(selectedContacts.size() == 1){
                newConversation = User.getConversations().stream().filter(c -> c.getParticipantsList().contains(selectedContacts.get(0))).findFirst().orElse(null);

                if(newConversation == null) {
                    newConversationName = "private";
                }
            } else {
                //TODO:popup pentru numele conversatiei
                //InsertConversationNameDialog insertConversationNameDialog =new InsertConversationNameDialog(this);
                //insertConversationNameDialog.show();
                newConversationName = "Conversatie noua";
            }

            //regasire lista de contacte cu chei
            ArrayList<Contact> conversationContacts = new ArrayList<>();
            conversationContacts.add(new Contact(User.getKey(),User.getEmail()));
            for (Contact c: selectedContacts) {
                Contact contact = User.getContacts().stream().filter(co -> c.getEmail().equals(co.getEmail())).findFirst().orElse(null);
                conversationContacts.add(contact);
            }

            newConversation = new Conversation(newConversationName, conversationContacts);

            DatabaseReference convRef = database.getReference("conversations").push();
            convRef.setValue(newConversation);
            newConversation.setKey(convRef.getKey());

            //adaugare in lista utilizatorilor
            for (Contact c: conversationContacts) {
                DatabaseReference userConvRef = database.getReference("users").child(c.getKey()).child("user_conversations").push();
                userConvRef.setValue(convRef.getKey());
            }

            DatabaseReference newConfRef = database.getReference("conversations").child(convRef.getKey());
            newConfRef.get().addOnCompleteListener(task -> {
                //adaugas un mesag de sistem
                Message newMessage = new Message("Conversatie creata", User.getKey());
                DatabaseReference messagesRef = database.getReference().child("conversations").child(task.getResult().getKey()).child("messages").push();
                messagesRef.setValue(newMessage);

                Intent data = new Intent();
                Activity activity = ((ContactListActivity)v.getContext());
                data.putExtra("conversation_key",task.getResult().getKey());
                activity.setResult(1,data);
                activity.finish();
            });
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
                AddContactDialog cdd=new AddContactDialog(this);
                cdd.show();
                break;
        }
        return true;
    }

}