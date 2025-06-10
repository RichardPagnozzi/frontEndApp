package com.example.skybox_frontend.ui.comms.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.skybox_frontend.ui.comms.model.Contact;
import com.example.skybox_frontend.ui.comms.repository.CommsRepository;

import java.util.Arrays;
import java.util.List;

public class CommsViewModel extends ViewModel {

    private final CommsRepository repository = CommsRepository.getInstance();

    public CommsViewModel() {
        List<Contact> mock = Arrays.asList(
                new Contact("A", "192.168.0.1", "#3DAEFF"),
                new Contact("B", "192.168.0.2", "#5E748D"),
                new Contact("C", "192.168.0.3", "#FFA500"),
                new Contact("D", "192.168.0.4", "#9C27B0")
        );
        for (Contact c : mock) {
            addContact(c);
        }
    }

    public LiveData<List<Contact>> getContacts() {
        return repository.getContacts();
    }

    // Add new contact
    public void addContact(Contact contact) {
        repository.addContact(contact);
    }

    // Update a contact at a specific index (optional for edit functionality)
    public void updateContact(int index, Contact contact) {
        repository.updateContact(index, contact);
    }

    // Clear all contacts
    public void clearContacts() {
        repository.clearContacts();
    }

    // Forwards the pin toggle command to the repository
    public void togglePin(Contact contact) {
        repository.togglePin(contact);
    }

}
