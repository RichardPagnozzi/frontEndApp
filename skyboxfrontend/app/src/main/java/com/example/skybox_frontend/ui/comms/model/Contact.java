package com.example.skybox_frontend.ui.comms.model;

import java.io.Serializable;

public class Contact implements Serializable {
    private String callsign;
    private String ip;
    private String colorHex;

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
}
