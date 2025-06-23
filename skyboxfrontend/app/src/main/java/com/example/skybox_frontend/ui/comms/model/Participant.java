package com.example.skybox_frontend.ui.comms.model;

public class Participant {
    private String callsign;
    private String ip;
    private boolean isMuted;
    private boolean isVideoOn;
    private boolean isConnected;

    public Participant(String callsign, String ip, boolean isMuted, boolean isVideoOn, boolean isConnected) {
        this.callsign = callsign;
        this.ip = ip;
        this.isMuted = isMuted;
        this.isVideoOn = isVideoOn;
        this.isConnected = isConnected;
    }

    // Getters
    public String getCallsign() {
        return callsign;
    }

    public String getIp() {
        return ip;
    }

    public boolean isMuted() {
        return isMuted;
    }

    public boolean isVideoOn() {
        return isVideoOn;
    }

    public boolean isConnected() {
        return isConnected;
    }

    // Setters
    public void setMuted(boolean muted) {
        this.isMuted = muted;
    }

    public void setVideoOn(boolean videoOn) {
        this.isVideoOn = videoOn;
    }

    public void setConnected(boolean connected) {
        this.isConnected = connected;
    }
}
