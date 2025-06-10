package com.example.skybox_frontend.ui.comms.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.skybox_frontend.ui.comms.model.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Central repository for the entire Comms feature.
 * Owns and manages all communication-related data (contacts, messages, voice state, etc).
 */
public class CommsRepository {

    // Singleton instance
    private static CommsRepository instance;

    // Internal list for contacts
    private final MutableLiveData<List<Contact>> contactsLiveData = new MutableLiveData<>(new ArrayList<>());

    private CommsRepository() {}

    // Access point
    public static CommsRepository getInstance() {
        if (instance == null) {
            instance = new CommsRepository();
        }
        return instance;
    }

    // Public Methods

    public void addContact(Contact contact) {
        List<Contact> current = contactsLiveData.getValue();
        if (current == null) current = new ArrayList<>();

        current.add(contact);
        contactsLiveData.setValue(current);
    }

    public void updateContact(int index, Contact updatedContact) {
        List<Contact> current = contactsLiveData.getValue();
        if (current != null && index >= 0 && index < current.size()) {
            current.set(index, updatedContact);
            contactsLiveData.setValue(current);
        }
    }

    public LiveData<List<Contact>> getContacts() {
        return contactsLiveData;
    }

    public void clearContacts() {
        contactsLiveData.setValue(new ArrayList<>());
    }
}
