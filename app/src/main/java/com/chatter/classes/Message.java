package com.chatter.classes;

import android.net.Uri;
import android.widget.ImageView;

import com.google.firebase.database.Exclude;

import java.util.Date;

public class Message {
    @Exclude
    private String key;
    private String textContent ="";
    private String senderEmail;
    private Date timestamp;
    private String mediaKey;

    public String getMediaKey() {
        return mediaKey;
    }

    public void setMediaKey(String mediaKey) {
        this.mediaKey = mediaKey;
    }


    public Message(String textContent, String mediaKey, String senderEmail) {
        this.textContent = textContent;
        this.mediaKey = mediaKey;
        this.senderEmail = senderEmail;
        this.timestamp = new Date();
    }

    private Message() {
    }

    @Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
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
