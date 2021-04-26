package com.chatter.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ContactListActivity extends AppCompatActivity {
    public User currentUser;
    RecyclerView recyclerView;
    public ContactsAdapter contactsAdapter;
    public static String REFRESH_LIST = "com.domain.action.REFRESH_LIST";

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(REFRESH_LIST)) {
                contactsAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(REFRESH_LIST);
        this.registerReceiver(broadcastReceiver, filter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        this.currentUser = getIntent().getParcelableExtra("currentUser");

        Toolbar toolbar = findViewById(R.id.toolbar_contact_list);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Contacts");
        }

        this.contactsAdapter =  new ContactsAdapter(this.currentUser.getContacts());
        this.recyclerView = findViewById(R.id.recycle_contact_list);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setAdapter(this.contactsAdapter);

        FloatingActionButton buttonStartConversation = findViewById(R.id.button_start_conversation);
        buttonStartConversation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                ArrayList<Contact> selectedContacts;
                selectedContacts = (ArrayList<Contact>)currentUser.getContacts().stream().filter(Contact::isSelected).collect(Collectors.toList());
                Conversation newConversation;
                String newConversationName = "";

                if(selectedContacts.size() == 1){
                    newConversation = currentUser.getConversations().stream().filter(c -> c.getParticipantsList().contains(selectedContacts.get(0))).findFirst().orElse(null);

                    if(newConversation == null) {
                        newConversationName = selectedContacts.get(0).getEmail();
                    }
                } else {
                    //TODO:popup pentru numele conversatiei
                    newConversationName = "Conversatie noua";
                }
                newConversation = new Conversation(newConversationName, selectedContacts);
                currentUser.getConversations().add(newConversation);
                newConversation.getParticipantsList().add(new Contact(currentUser.getEmail(),currentUser.getKey()));

                //adaugare in lista de conversatii
                DatabaseReference convRef = database.getReference("conversations").push();
                convRef.setValue(newConversation);
                newConversation.setKey(convRef.getKey());

                //adaugare in lista utilizatorului
                for (Contact c: selectedContacts) {
                    DatabaseReference userConvRef = database.getReference("users").child(c.getKey()).child("user_conversations").push();
                    userConvRef.setValue(convRef.getKey());
                }

                Intent data = new Intent();
                Activity activity = ((ContactListActivity)v.getContext());
                data.putExtra("conversation_key",newConversation.getKey());
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