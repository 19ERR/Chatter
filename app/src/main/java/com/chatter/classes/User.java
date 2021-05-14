package com.chatter.classes;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.Exclude;

import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class User {
    @Exclude
    private static final MutableLiveData<ArrayList<Contact>> contacts = new MutableLiveData<>(new ArrayList<>());
    @Exclude
    private static final ArrayList<Conversation> conversations = new ArrayList<>();

    private static String email;

    private User() {
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        User.email = email;
    }

    public static MutableLiveData<ArrayList<Contact>> getContacts() {
        return contacts;
    }

    public static void setContacts(Map<String, Contact> contactsMap) {
        ArrayList<Contact> contactsAux = new ArrayList<>();
        for (String key : contactsMap.keySet()) {
            Objects.requireNonNull(contactsMap.get(key)).setKey(key);
            contactsAux.add(contactsMap.get(key));
        }
        contacts.postValue(contactsAux);
    }

    public static ArrayList<Conversation> getConversations() {
        return User.conversations;
    }

    public static void setConversations(Map<String, Conversation> conversations) {
        for (String key : conversations.keySet()) {
            Objects.requireNonNull(conversations.get(key)).setKey(key);
            User.conversations.add(conversations.get(key));
        }
    }

    public static Map<String, Object> getContactsHashMap() {
        Map<String, Object> contactHashMap = new HashMap<>();
        for (Contact c :
                User.contacts.getValue()) {
            contactHashMap.put(c.getKey(), c);
        }

        return contactHashMap;
    }

    public static Conversation getConversation(String conversationKey) {
        Optional<Conversation> result = conversations.stream().filter(c -> c.getKey().equals(conversationKey)).findFirst();
        return result.orElse(null);
    }

    public static void addContact(Contact newContact) {
        ArrayList<Contact> newContactList;
        newContactList = contacts.getValue();
        newContactList.add(newContact);
        contacts.postValue(newContactList);
    }

    public static void removeContact(String contactKey) {
        ArrayList<Contact> newContactList;
        newContactList = contacts.getValue();
        newContactList.removeIf(c -> c.getKey().equals(contactKey));
        contacts.postValue(newContactList);
    }

    public static void logOut(){
        FirebaseAuth.getInstance().signOut();
        ArrayList<Contact> contacts = new ArrayList<>();
        ArrayList<Conversation> conversations = new ArrayList<>();
        email = null;
    }

}
