package com.example.skybox_frontend.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {

    // Observable which can be watched by UI for changes
    // Locally mutable, but globally immutable to outside world (only GetLoginTitle() is exposed)
    private final MutableLiveData<String> loginTitle = new MutableLiveData<>("SKYBOX Authentication");
    private final MutableLiveData<Boolean> loginSuccess = new MutableLiveData<>();

    // Exposed as read-only LiveData to the view
    public LiveData<String> getLoginTitle() {return loginTitle;}
    public LiveData<Boolean> getLoginSuccess() {return loginSuccess;}

    // Login model instance
    private final LoginModel model = new LoginModel();





    // Called by the Fragment when login is attempted
    public void attemptLogin(String callsign, String password, String chatIP) {

        // Right now this is mock logic — replace with real backend call later
        boolean result = model.login(callsign, password); // chatIP not used yet
        loginSuccess.setValue(result); // Triggers UI observers
    }
}
