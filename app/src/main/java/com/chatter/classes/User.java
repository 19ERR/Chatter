package com.chatter.classes;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class User implements Parcelable {
    private String email;
    private String key;
    private List<Contact> contacts = new ArrayList<>();
    private List<Conversation> conversations = new ArrayList<>();

    protected User(Parcel in) {
        email = in.readString();
        key = in.readString();
        contacts = in.createTypedArrayList(Contact.CREATOR);
        conversations = in.createTypedArrayList(Conversation.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(key);
        dest.writeTypedList(contacts);
        dest.writeTypedList(conversations);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(HashMap<String,Contact> contacts) {
        for (String key : contacts.keySet()) {
            contacts.get(key).setKey(key);
            this.contacts.add(contacts.get(key));
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private User(){
    }

    public User(String email){
        this.email = email;
    }

    public List<Conversation> getConversations() {
        return conversations;
    }

    public void setConversations(HashMap<String,Conversation> conversations) {
        for (String key : conversations.keySet()) {
            conversations.get(key).setKey(key);
            this.conversations.add(conversations.get(key));
        }
    }

    public Map<String, Object> getContactsHashMap(){
        Map<String,Object> contactHashMap = new HashMap<>();
        for (Contact c:
             this.contacts) {
            contactHashMap.put(c.getKey(), c);
        }

        return contactHashMap;
    }
}
