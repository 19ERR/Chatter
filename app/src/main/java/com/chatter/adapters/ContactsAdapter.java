package com.chatter.adapters;

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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;

public class ContactsAdapter extends FirebaseRecyclerAdapter<Contact, ContactsAdapter.ContactHolder> {

    public static ArrayList<Contact> selectedContacts = new ArrayList<>();

    public static class ContactHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ContactHolder(View view) {
            super(view);

            textView = view.findViewById(R.id.contactEmail);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    public ContactsAdapter(@NonNull FirebaseRecyclerOptions<Contact> options) {
        super(options);
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_view, parent, false);

        return new ContactHolder(view);
    }

    @Override
    protected void onBindViewHolder(ContactHolder viewHolder, int position, @NonNull Contact contact) {
        viewHolder.getTextView().setText(contact.getEmail());
        viewHolder.getTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contact.select();
                if(contact.isSelected()){
                    selectedContacts.add(contact);
                    Toast.makeText(v.getContext(),"Selectat",Toast.LENGTH_SHORT).show();
                    v.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.black));
                } else {
                    selectedContacts.remove(contact);
                    Toast.makeText(v.getContext(),"Deselectat",Toast.LENGTH_SHORT).show();
                    v.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.white));
                }
            }
        });
    }
}
