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

    private final User currentUser;
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

    public ConversationsAdapter(User currentUser) {
        this.currentUser = currentUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.conversation_view, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Conversation conversation = currentUser.getConversations().get(position);
        if(conversation.getParticipantsList().size() == 2){
            for(Contact contact: conversation.getParticipantsList()){
                if(!contact.getEmail().equals(currentUser.getEmail())){
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
                Toast.makeText(v.getContext(),currentUser.getConversations().get(position).getName(),Toast.LENGTH_SHORT).show();
                Intent openConversationIntent = new Intent(v.getContext(), ConversationActivity.class);
                openConversationIntent.putExtra("currentUser", currentUser);
                openConversationIntent.putExtra("conversation_key", currentUser.getConversations().get(position).getKey());
                v.getContext().startActivity(openConversationIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return currentUser.getConversations().size();
    }
}