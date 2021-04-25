package com.chatter.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.chatter.BuildConfig;
import com.chatter.R;
import com.chatter.activities.ContactListActivity;
import com.chatter.classes.Contact;
import com.chatter.classes.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;


public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private final List<Contact> contacts;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View view) {
            super(view);

            textView = view.findViewById(R.id.contactEmail);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    public ContactsAdapter(List<Contact> dataSet, ContactListActivity contactListActivity) {
        contacts = dataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.contact_view, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getTextView().setText(contacts.get(position).getEmail());
        viewHolder.getTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contacts.get(position).select();
                if(contacts.get(position).isSelected()){
                    Toast.makeText(v.getContext(),"Selectat",Toast.LENGTH_SHORT).show();
                    v.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.black));
                } else {
                    Toast.makeText(v.getContext(),"Deselectat",Toast.LENGTH_SHORT).show();
                    v.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.white));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }
}