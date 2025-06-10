package com.example.skybox_frontend.ui.comms.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.skybox_frontend.ui.comms.model.Contact;
import com.example.skybox_frontend.ui.comms.repository.CommsRepository;

import java.util.List;

/**
 * ViewModel for the Comms feature.
 * Acts as the bridge between the UI and the repository.
 */
public class CommsViewModel extends ViewModel {

    private final CommsRepository repository = CommsRepository.getInstance();

    // 🧾 Expose the list of contacts to the UI
    public LiveData<List<Contact>> getContacts() {
        return repository.getContacts();
    }

    // ➕ Add a new contact
    public void addContact(Contact contact) {
        repository.addContact(contact);
    }

    // ✏️ Update a contact at a specific index (optional for edit functionality)
    public void updateContact(int index, Contact contact) {
        repository.updateContact(index, contact);
    }

    // 🧼 Clear all contacts (debug/testing)
    public void clearContacts() {
        repository.clearContacts();
    }
}
