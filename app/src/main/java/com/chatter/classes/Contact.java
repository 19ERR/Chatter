package com.chatter.classes;

import androidx.annotation.NonNull;

import java.util.Iterator;

public class Contact {
    private String email;
    private String id;

    public String getEmail() {
        return email;
    }

    public String getID() {
        return id;
    }

    public Contact(String id, String email){
        this.id = id;
        this.email = email;
    }
}
