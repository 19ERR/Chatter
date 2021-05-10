package com.chatter.classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.Date;

public class Message implements Parcelable {
    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };
    @Exclude
    private String key;
    private String content;
    private String senderEmail;
    private Date timestamp;

    protected Message(Parcel in) {
        key = in.readString();
        content = in.readString();
        senderEmail = in.readString();
    }

    public Message(String content, String senderEmail) {
        this.content = content;
        this.senderEmail = senderEmail;
        this.timestamp = new Date();
    }

    private Message() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(content);
        dest.writeString(senderEmail);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

}
