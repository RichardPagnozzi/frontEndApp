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

    // Start a call with provided callsign and IP. If valid, move to RINGING state.
    public void startCall(String callsign, String ip) {
        callState.setValue(CallState.RINGING);
    }

    // Simulate a successful connection (mock backend connect)
    public void mockConnect() {
        callState.setValue(CallState.CONNECTED);
    }

    // Simulate a timeout (mock backend no-answer)

    public void mockTimeout() {
        callState.setValue(CallState.TIMED_OUT);
    }

    // End the current call and reset state
    public void endCall() {
        callState.setValue(CallState.IDLE);
    }

    // Basic validation: non-empty callsign + IP
    private boolean isValidInput(String callsign, String ip) {
        return callsign != null && !callsign.isEmpty()
                && ip != null && !ip.isEmpty();
    }
}
