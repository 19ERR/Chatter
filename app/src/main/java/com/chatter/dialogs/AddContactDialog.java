package com.chatter.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chatter.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddContactDialog extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button buttonAddContact, buttonCancel;

    public AddContactDialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_contact);

        buttonAddContact = findViewById(R.id.button_add_contact);
        buttonCancel= findViewById(R.id.button_cancel_add_contact);
        buttonAddContact.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_add_contact:
                addContact(v);
                c.finish();
                break;
            case R.id.button_cancel_add_contact:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }

    private void addContact(View v) {
        EditText editTextEmail = findViewById(R.id.edit_text_email_contact_nou);
        String email = editTextEmail.getText().toString();

        // adauga in baza de date
        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference dbRef = database.getReference("users");

        //dbRef.child(account.getId()).child("email").setValue(account.getEmail());

        Toast.makeText(v.getContext(), email, Toast.LENGTH_SHORT).show();
    }
}