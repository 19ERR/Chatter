package com.chatter.classes;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Iterator;

public class Contact implements Parcelable {

    private String email;
    private String key;
    @Exclude
    private boolean selected = false;

    protected Contact(Parcel in) {
        email = in.readString();
        key = in.readString();
    }

    private Contact(){}

    public Contact(String email, String key){
        this.email = email;
        this.key = key;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(key);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    @Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Exclude
    public void select(){
        this.selected = !this.selected;
    }
    @Exclude
    public boolean isSelected(){return selected;}

}
