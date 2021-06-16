package com.chatter.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chatter.R;
import com.chatter.adapters.MediaAdapter;
import com.chatter.classes.Conversation;
import com.chatter.classes.Message;
import com.chatter.classes.User;
import com.chatter.viewModels.MediaLinksViewModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class ConversationMediaFragment extends Fragment {

    Conversation conversation;
    MediaAdapter mediaLinksAdapter;
    MediaLinksViewModel mediaLinksViewModel;
    ArrayList<String> mediaLinks = new ArrayList<>();
    RecyclerView recyclerView;
    Observer<ArrayList<String>> mediaLinksListUpdateObserver = new Observer<ArrayList<String>>() {
        @Override
        public void onChanged(ArrayList<String> mediaLinksArrayList) {
            mediaLinksAdapter.notifyDataSetChanged();
        }
    };

    public ConversationMediaFragment() {
        super(R.layout.fragment_conversation_media);
    }

    public ConversationMediaFragment(String conversationKey) {
        super(R.layout.fragment_conversation_media);
        this.conversation = User.getConversation(conversationKey);

        //listener pentru linkuri
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userConvRef = database.getReference().child("messages").child(conversation.getKey()).getRef();
        //cauta utilizatorul in users
        Query query = userConvRef.orderByChild("containsMedia").equalTo(true);
        //cand in gaseste adauga listener pentru a lua valorile initiale
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message message = snapshot.getValue(Message.class);
                assert message != null;
                mediaLinks.add(message.getMediaKey());
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

    public static ConversationMediaFragment newInstance(String conversationKey) {
        ConversationMediaFragment fragment = new ConversationMediaFragment();
        Bundle args = new Bundle();
        args.putString("conversationKey", conversationKey);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.conversation = User.getConversation(savedInstanceState.getString("conversationKey"));
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            this.conversation = User.getConversation(savedInstanceState.getString("conversationKey"));
        }
        recyclerView = view.findViewById(R.id.recycle_conversation_media_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 5));

        //adauga observer pentru lista de conversatii
        mediaLinksViewModel = ViewModelProviders.of(this).get(MediaLinksViewModel.class);
        if (this.conversation != null) {
            mediaLinksViewModel.getMediaLinksLiveData().postValue(mediaLinks);
        }
        mediaLinksAdapter = new MediaAdapter(mediaLinksViewModel.getMediaLinksLiveData().getValue());
        mediaLinksViewModel.getMediaLinksLiveData().observe(getViewLifecycleOwner(), mediaLinksListUpdateObserver);


        recyclerView.setAdapter(mediaLinksAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_conversation_media, container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("conversationKey", this.conversation.getKey());
    }
}