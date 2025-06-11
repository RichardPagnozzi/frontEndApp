package com.example.skybox_frontend.ui.comms.model;

public class Message {
    private final String content;
    private final boolean isOutgoing;
    private final long timestamp;

    public Message(String content, boolean isOutgoing, long timestamp) {
        this.content = content;
        this.isOutgoing = isOutgoing;
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public boolean isOutgoing() {
        return isOutgoing;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
