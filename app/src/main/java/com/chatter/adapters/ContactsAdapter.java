package com.chatter.adapters;

import android.app.Activity;
import android.content.Context;
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
import com.chatter.classes.Contact;
import com.chatter.classes.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private final List<Contact> contacts;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View view) {
            super(view);

            textView = (TextView) view.findViewById(R.id.contactEmail);
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
                Activity activity = ((ContactListActivity)v.getContext());

                // TODO: de creat conversatia
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference usersRef = database.getReference("conversations");

                /*Query query = usersRef.orderByChild("email").equalTo("stanciuandreicristian@gmail.com").limitToFirst(1);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            DataSnapshot userSnapshot = snapshot.getChildren().iterator().next();
                            currentUser = userSnapshot.getValue(User.class);
                            assert currentUser != null;
                            currentUser.setKey(userSnapshot.getKey());
                        }
                        else {
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference dbRef = database.getReference("users").push();
                            dbRef.child("email").setValue(account.getEmail());

                            currentUser = new User(account.getEmail());
                            currentUser.setKey(dbRef.getKey());
                        }

                        intent.putExtra("currentUser", currentUser);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });*/

                Intent data = new Intent();
                data.putExtra("contact",contacts.get(position));
                activity.setResult(1,data);
                activity.finish();
            }
        });

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }
}