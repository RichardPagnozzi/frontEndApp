package com.example.skybox_frontend.ui.comms.model;

import java.util.ArrayList;
import java.util.List;

public class MessageThread {
    private final List<Message> messages = new ArrayList<>();

    public List<Message> getMessages() {
        return messages;
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public void clearMessages() {
        messages.clear();
    }

    public int getMessageCount() {
        return messages.size();
    }
}
