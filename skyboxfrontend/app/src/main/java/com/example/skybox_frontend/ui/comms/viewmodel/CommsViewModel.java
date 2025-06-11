package com.example.skybox_frontend.ui.comms.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.skybox_frontend.ui.comms.model.Contact;
import com.example.skybox_frontend.ui.comms.model.Message;
import com.example.skybox_frontend.ui.comms.repository.CommsRepository;

import java.util.Arrays;
import java.util.List;

public class CommsViewModel extends ViewModel {

    private final CommsRepository repository = CommsRepository.getInstance();
    private final MutableLiveData<Contact> selectedContact = new MutableLiveData<>();


    public CommsViewModel() {
        generateMockContactsWithMessages();
    }

    // Getter/Setter
    public void setSelectedContact(Contact contact) {
        selectedContact.setValue(contact);
    }

    public LiveData<Contact> getSelectedContact() {
        return selectedContact;
    }

    // Get Contacts
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


    // Mock
    private void populateMockMessages(Contact contact, int messageCount) {
        long now = System.currentTimeMillis();
        for (int i = 0; i < messageCount; i++) {
            boolean isOutgoing = i % 2 == 0;
            String content = "Msg " + (i + 1);
            long timestamp = now - (messageCount - i) * 60_000;
            contact.getThread().addMessage(new Message(content, isOutgoing, timestamp));
        }
    }

    private void generateMockContactsWithMessages() {
        Contact c1 = new Contact("Alpha", "192.168.0.1", "#3DAEFF");
        Contact c2 = new Contact("Bravo", "192.168.0.2", "#5E748D");
        Contact c3 = new Contact("Charlie", "192.168.0.3", "#FFA500");
        Contact c4 = new Contact("Delta", "192.168.0.4", "#9C27B0");

        populateMockMessages(c1, 5);
        populateMockMessages(c2, 3);
        populateMockMessages(c3, 6);

        addContact(c1);
        addContact(c2);
        addContact(c3);
        addContact(c4);
    }


}
