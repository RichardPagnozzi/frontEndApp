package com.example.skybox_frontend.ui.comms.model;

public class Participant {
    private final String callsign;
    private final String ip;
    private final boolean isMuted;
    private final boolean isVideoOn;

    public Participant(String callsign, String ip, boolean isMuted, boolean isVideoOn) {
        this.callsign = callsign;
        this.ip = ip;
        this.isMuted = isMuted;
        this.isVideoOn = isVideoOn;
    }

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
}
