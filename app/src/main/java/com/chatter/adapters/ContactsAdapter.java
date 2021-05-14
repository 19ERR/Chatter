package com.chatter.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.chatter.R;
import com.chatter.classes.Contact;
import com.chatter.classes.User;

import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactHolder> {

    public static ArrayList<Contact> selectedContacts = new ArrayList<>();

    public ContactsAdapter() {
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_view, parent, false);

        return new ContactHolder(view);
    }

    @Override
    public int getItemCount() {
        return User.getContacts().getValue().size();
    }

    @Override
    public void onBindViewHolder(ContactHolder viewHolder, int position) {
        viewHolder.getTextViewContactEmail().setText(User.getContacts().getValue().get(position).getEmail());
        viewHolder.getTextViewContactEmail().setOnClickListener(v -> {
            User.getContacts().getValue().get(position).select();
            if (User.getContacts().getValue().get(position).isSelected()) {
                selectedContacts.add(User.getContacts().getValue().get(position));
                Toast.makeText(v.getContext(), "Selectat", Toast.LENGTH_SHORT).show();
                v.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.black));
            } else {
                selectedContacts.remove(User.getContacts().getValue().get(position));
                Toast.makeText(v.getContext(), "Deselectat", Toast.LENGTH_SHORT).show();
                v.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.white));
            }
        });
    }

    public static class ContactHolder extends RecyclerView.ViewHolder {
        private final TextView textViewContactEmail;

        public ContactHolder(View view) {
            super(view);
            textViewContactEmail = view.findViewById(R.id.contactEmail);
        }

        public TextView getTextViewContactEmail() {
            return textViewContactEmail;
        }
    }
    
}
