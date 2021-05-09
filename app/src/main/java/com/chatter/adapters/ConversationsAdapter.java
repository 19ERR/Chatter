package com.chatter.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chatter.R;
import com.chatter.activities.ContactListActivity;
import com.chatter.activities.ConversationActivity;
import com.chatter.classes.Contact;
import com.chatter.classes.Conversation;
import com.chatter.classes.User;

import java.util.ArrayList;


public class ConversationsAdapter extends RecyclerView.Adapter<ConversationsAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View view) {
            super(view);

            textView = view.findViewById(R.id.conversationName);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    public ConversationsAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.conversation_view, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        Conversation conversation = User.getConversations().get(position);
        if(conversation.getParticipantsList().size() == 2){
            for(Contact contact: conversation.getParticipantsList()){
                if(!contact.getEmail().equals(User.getEmail())){
                    viewHolder.getTextView().setText(contact.getEmail());
                    break;
                }
            }
        } else {
            viewHolder.getTextView().setText(conversation.getName());
        }

        viewHolder.getTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),User.getConversations().get(position).getName(),Toast.LENGTH_SHORT).show();
                Intent openConversationIntent = new Intent(v.getContext(), ConversationActivity.class);
                openConversationIntent.putExtra("conversation_key", User.getConversations().get(position).getKey());
                v.getContext().startActivity(openConversationIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return User.getConversations().size();
    }
}