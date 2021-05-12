package com.chatter.classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.common.data.DataBufferObserver;
import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Conversation implements Parcelable {
    @Exclude
    private String key;
    private String name;
    private Message lastMessage;
    private ArrayList<Contact> participants = new ArrayList<>();

    public Conversation(String name, ArrayList<Contact> participants, Message lastMessage) {
        this.name = name;
        this.participants = participants;
        this.lastMessage = lastMessage;
    }

    private Conversation() {
    }

    protected Conversation(Parcel in) {
        key = in.readString();
        name = in.readString();
        participants = in.createTypedArrayList(Contact.CREATOR);
        lastMessage = in.readParcelable(Message.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(name);
        dest.writeTypedList(participants);
        dest.writeParcelable(lastMessage, flags);
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

    public Map<String, Object> getParticipants() {
        Map<String, Object> contactHashMap = new HashMap<>();
        for (Contact c :
                this.participants) {
            contactHashMap.put(c.getKey(), c);
        }

        return contactHashMap;
    }

    public void setParticipants(HashMap<String, Contact> contacts) {
        for (String key : contacts.keySet()) {
            contacts.get(key).setKey(key);
            this.participants.add(contacts.get(key));
        }
    }

    public void setParticipantsList(ArrayList<Contact> participants) {
        this.participants = participants;
    }
    @Exclude
    public ArrayList<Contact> getParticipantsList() {
        return this.participants;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }

}
