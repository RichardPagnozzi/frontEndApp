package com.example.skybox_frontend.ui.trivia.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.skybox_frontend.ui.trivia.model.TriviaGameModes;

// Tracks selected live data game mode
public class TriviaViewModel extends ViewModel {

    // Holds current selected game mode
    private final MutableLiveData<TriviaGameModes> selectedMode = new MutableLiveData<>();

    // Read-only access to observers
    public LiveData<TriviaGameModes> getSelectedMode() {
        return selectedMode;
    }

    // Setter that changes the selected game mode
    public void selectMode(TriviaGameModes mode) {
        selectedMode.setValue(mode);
    }
}
