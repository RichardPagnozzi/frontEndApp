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

    // 🔧 Singleton instance
    private static CommsRepository instance;

    // 🗂 Internal backing list for contacts
    private final MutableLiveData<List<Contact>> contactsLiveData = new MutableLiveData<>(new ArrayList<>());

    // 🛠 Private constructor
    private CommsRepository() {}

    // 🎯 Global access point
    public static CommsRepository getInstance() {
        if (instance == null) {
            instance = new CommsRepository();
        }
        return instance;
    }

    // 🧠 Add a new contact
    public void addContact(Contact contact) {
        List<Contact> current = contactsLiveData.getValue();
        if (current == null) current = new ArrayList<>();

        current.add(contact);
        contactsLiveData.setValue(current);
    }

    // 🔁 Update existing contact by index (optional for edit feature)
    public void updateContact(int index, Contact updatedContact) {
        List<Contact> current = contactsLiveData.getValue();
        if (current != null && index >= 0 && index < current.size()) {
            current.set(index, updatedContact);
            contactsLiveData.setValue(current);
        }
    }

    // 🧾 Expose immutable contact list
    public LiveData<List<Contact>> getContacts() {
        return contactsLiveData;
    }

    // 🚫 Clear contacts (for debug/testing)
    public void clearContacts() {
        contactsLiveData.setValue(new ArrayList<>());
    }

    // 🔮 TODO: Add message handling, voice state, etc. in future
}
