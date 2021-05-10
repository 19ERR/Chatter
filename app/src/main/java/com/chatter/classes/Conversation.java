package com.chatter.classes;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

public class Conversation implements Parcelable {
    @Exclude
    private String key;
    private String name;

    private ArrayList<Contact> participants = new ArrayList<>();
    @Exclude
    private ArrayList<Message> messages = new ArrayList<>();

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setParticipants(HashMap<String,Contact> contacts) {
        for (String key : contacts.keySet()) {
            contacts.get(key).setKey(key);
            this.participants.add(contacts.get(key));
        }
    }

    public Map<String, Object> getParticipants() {
        Map<String,Object> contactHashMap = new HashMap<>();
        for (Contact c:
                this.participants) {
            contactHashMap.put(c.getKey(), c);
        }

        return contactHashMap;
    }
    @Exclude
    public ArrayList<Contact> getParticipantsList() {
        return this.participants;
    }

    public void setMessages(HashMap<String,Message> messages) {
        for (String key : messages.keySet()) {
            messages.get(key).setKey(key);
            this.messages.add(messages.get(key));
        }
    }
    @Exclude
    public Map<String, Object> getMessages() {
        Map<String,Object> messagesHashMap = new HashMap<>();
        for (Message m:
                this.messages) {
            messagesHashMap.put(m.getKey(), m);
        }

        return messagesHashMap;
    }
    @Exclude
    public ArrayList<Message> getMessagesList() {
        return messages;
    }

    public Conversation(String name, ArrayList<Contact> participants){
        this.name = name;
        this.participants = participants;
    }

    private Conversation(){
    }

    protected Conversation(Parcel in) {
        key = in.readString();
        name = in.readString();
        participants = in.createTypedArrayList(Contact.CREATOR);
        messages = in.createTypedArrayList(Message.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(name);
        dest.writeTypedList(participants);
        dest.writeTypedList(messages);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Conversation> CREATOR = new Creator<Conversation>() {
        @Override
        public Conversation createFromParcel(Parcel in) {
            return new Conversation(in);
        }

        @Override
        public Conversation[] newArray(int size) {
            return new Conversation[size];
        }
    };


}
