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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Objects;

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
                if(addContact(v)) {
                    Toast.makeText(v.getContext(), "Adaugat cu succes!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(v.getContext(), "Contactul nu a putut fi adaugat!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.button_cancel_add_contact:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }

    private boolean addContact(View v) {
        EditText editTextEmail = findViewById(R.id.edit_text_email_contact_nou);
        String email_contact_nou = editTextEmail.getText().toString();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(v.getContext());
        // adauga in baza de date
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference("users");

        assert account != null;
        dbRef.child(Objects.requireNonNull(account.getId())).child("contacts").push().setValue(email_contact_nou);
        //TODO: VERIFICA DACA EXISTA CONTUL
        return true;
    }
}