package com.chatter.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.chatter.classes.Contact;
import com.chatter.classes.User;

import java.util.ArrayList;

public class ContactsViewModel extends ViewModel {
    MutableLiveData<ArrayList<Contact>> contactsLiveData;

    public ContactsViewModel() {
        contactsLiveData = new MutableLiveData<>();
        contactsLiveData = User.getContacts();
    }

    public MutableLiveData<ArrayList<Contact>> getContactsLiveData() {
        return contactsLiveData;
    }
}