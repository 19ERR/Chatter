package com.chatter.classes;

import com.google.firebase.database.Exclude;

import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class User {
    @Exclude
    private static final ArrayList<Contact> contacts = new ArrayList<>();
    @Exclude
    private static final ArrayList<Conversation> conversations = new ArrayList<>();
    @Exclude
    private static String key;
    private static String email;

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

    public static void setContacts(Map<String, Contact> contacts) {
        for (String key : contacts.keySet()) {
            Objects.requireNonNull(contacts.get(key)).setKey(key);
            User.contacts.add(contacts.get(key));
        }
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
                User.contacts) {
            contactHashMap.put(c.getKey(), c);
        }

        return contactHashMap;
    }

    public static Conversation getConversation(String conversationKey) {
        Optional<Conversation> result = conversations.stream().filter(c -> c.getKey().equals(conversationKey)).findFirst();
        return result.orElse(null);
    }

    public static void addContact(Contact newContact) {
        User.contacts.add(newContact);
    }

    public static void removeContact(String contactKey) {
        contacts.removeIf(c -> c.getKey().equals(contactKey));
    }

    public static void logOut(){
        ArrayList<Contact> contacts = new ArrayList<>();
        ArrayList<Conversation> conversations = new ArrayList<>();
        key = null;
        email = null;
    }

}
