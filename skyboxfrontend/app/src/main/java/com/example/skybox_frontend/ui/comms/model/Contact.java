package com.example.skybox_frontend.ui.comms.model;

import java.io.Serializable;

public class Contact implements Serializable {
    private final MessageThread thread = new MessageThread();
    private String callsign;
    private String ip;
    private String colorHex;
    private boolean isPinned = false;


    public Contact(String callsign, String ip, String colorHex) {
        this.callsign = callsign;
        this.ip = ip;
        this.colorHex = colorHex;
    }

    // Getters
    public String getCallsign() {
        return callsign;
    }

    public String getIp() {
        return ip;
    }

    public String getColorHex() {
        return colorHex;
    }

    public boolean getIsPinned() {
        return isPinned;
    }

    public MessageThread getThread() {return thread;}

    // Setters
    public void setCallsign(String callsign) {
        this.callsign = callsign;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setColorHex(String colorHex) {
        this.colorHex = colorHex;
    }

    public void setIsPinned(boolean pinned) {
        this.isPinned = pinned;
    }
}
