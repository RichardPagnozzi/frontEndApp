package com.example.skybox_frontend.ui.comms.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class VoiceViewModel extends ViewModel {

    public enum CallState {
        IDLE,
        RINGING,
        CONNECTED,
        TIMED_OUT
    }

    private final MutableLiveData<CallState> callState = new MutableLiveData<>(CallState.IDLE);

    public LiveData<CallState> getCallState() {
        return callState;
    }

    public void startCall(String callsign, String ip) {
        if (callsign == null || callsign.isEmpty() || ip == null || ip.isEmpty()) {
            // Invalid input, stay in IDLE (you could expose an error LiveData if needed)
            return;
        }
        callState.setValue(CallState.RINGING);
        // You can later add logic to move to CONNECTED, TIMED_OUT, etc.
    }

    public void endCall() {
        callState.setValue(CallState.IDLE);
    }

    public void timeoutCall() {
        callState.setValue(CallState.TIMED_OUT);
    }

    public void connectCall() {
        callState.setValue(CallState.CONNECTED);
    }
}
