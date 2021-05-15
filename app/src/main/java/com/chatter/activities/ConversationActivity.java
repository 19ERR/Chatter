package com.chatter.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.chatter.R;
import com.chatter.classes.Conversation;
import com.chatter.classes.User;
import com.chatter.fragments.MessagesFragment;
import com.google.android.material.navigation.NavigationView;

public class ConversationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Conversation conversation;
    DrawerLayout drawerLayout;

    Fragment messagesFragment;
    Fragment conversationParticipantsFragment;
    Fragment conversationMediaFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        this.conversation = User.getConversation(getIntent().getStringExtra("conversation_key"));

        Toolbar toolbar = findViewById(R.id.toolbar_conversation);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Conversatii");
        }
        this.drawerLayout = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle actionBarDrawerToggle =
                new ActionBarDrawerToggle(this,drawerLayout, toolbar,
                        R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //creeare fragmente
        this.messagesFragment = new MessagesFragment(conversation.getKey());
        if (savedInstanceState == null) {
            showMessages();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void showMessages(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.conversation_fragment, this.messagesFragment)
                .setReorderingAllowed(true)
                .addToBackStack("name") // name can be null
                .commit();

    }

    private void showParticipants(){
    }
    private void showMedia(){
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.nav_item_messages:
                showMessages();
                break;
            case R.id.nav_item_participants:
                showParticipants();
                break;
            case R.id.nav_item_media:
                showMedia();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}