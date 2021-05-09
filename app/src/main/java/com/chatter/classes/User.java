package com.chatter.classes;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;

public class User {
    @Exclude
    private static String key;

    private static String email;

    @Exclude
    private static ArrayList<Contact> contacts = new ArrayList<>();

    @Exclude
    private static ArrayList<Conversation> conversations = new ArrayList<>();

    private User() {
    }

    public static String getKey() {
        return key;
    }

    public static void setKey(String key) {
        User.key = key;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        User.email = email;
    }

    public static ArrayList<Contact> getContacts() {
        return contacts;
    }

    public static void setContacts(Map<String,Contact> contacts) {
        for (String key : contacts.keySet()) {
            contacts.get(key).setKey(key);
            User.contacts.add(contacts.get(key));
        }
    }

    public static ArrayList<Conversation> getConversations() {
        return User.conversations;
    }

    public static void setConversations(Map<String,Conversation> conversations) {
        for (String key : conversations.keySet()) {
            conversations.get(key).setKey(key);
            User.conversations.add(conversations.get(key));
        }
    }

    public static Map<String, Object> getContactsHashMap(){
        Map<String,Object> contactHashMap = new HashMap<>();
        for (Contact c:
             User.contacts) {
            contactHashMap.put(c.getKey(), c);
        }

        return contactHashMap;
    }

    public static Conversation getConversation(String conversationKey){
        Optional<Conversation> result = conversations.stream().filter(c -> c.getKey().equals(conversationKey)).findFirst();
        return result.orElse(null);
    }

    public static void addContact(Contact newContact){
        User.contacts.add(newContact);
    }

    public static void removeContact(String contactKey){
        contacts.removeIf(c -> c.getKey().equals(contactKey));
    }
}
