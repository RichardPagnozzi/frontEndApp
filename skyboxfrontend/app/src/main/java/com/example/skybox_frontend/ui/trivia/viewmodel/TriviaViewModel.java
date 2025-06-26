package com.example.skybox_frontend.ui.trivia.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.skybox_frontend.ui.trivia.model.TriviaGameModes;
import com.example.skybox_frontend.ui.trivia.model.TriviaGameState;
import com.example.skybox_frontend.ui.trivia.model.TriviaQuestion;
import com.example.skybox_frontend.ui.trivia.repository.TriviaRepository;

import java.util.List;

public class TriviaViewModel extends AndroidViewModel {

    private final TriviaRepository repository;
    private final SharedPreferences prefs;
    private static final String PREFS_NAME = "trivia_high_scores";

    private List<TriviaQuestion> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;

    private final MutableLiveData<TriviaQuestion> currentQuestion = new MutableLiveData<>();
    private final MutableLiveData<Integer> currentScore = new MutableLiveData<>();
    private final MutableLiveData<Integer> highScore = new MutableLiveData<>(0);
    private final MutableLiveData<TriviaGameModes> selectedMode = new MutableLiveData<>();
    private final MutableLiveData<TriviaGameState> gameState = new MutableLiveData<>(TriviaGameState.IN_PROGRESS);

    private CountDownTimer modeTimer;

    public TriviaViewModel(@NonNull Application application) {
        super(application);
        repository = new TriviaRepository(application.getApplicationContext());
        prefs = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        currentScore.setValue(0);
    }

    public void selectMode(TriviaGameModes mode) {
        selectedMode.setValue(mode);
        highScore.setValue(loadHighScore(mode));
    }

    public LiveData<TriviaGameModes> getSelectedMode() {
        return selectedMode;
    }

    public LiveData<TriviaGameState> getGameState() {
        return gameState;
    }

    public LiveData<Integer> getHighScore() {
        return highScore;
    }

    public void startGame() {
        questions = repository.loadQuestions();
        Log.d("TriviaViewModel", "Loaded " + questions.size() + " questions.");

        currentQuestionIndex = 0;
        score = 0;
        currentScore.setValue(score);
        gameState.setValue(TriviaGameState.IN_PROGRESS);

        TriviaGameModes mode = selectedMode.getValue();
        if (mode == TriviaGameModes.TIMED || mode == TriviaGameModes.BLITZ) {
            startModeTimer(mode);
        }

        if (!questions.isEmpty()) {
            currentQuestion.setValue(questions.get(currentQuestionIndex));
        } else {
            Log.e("TriviaViewModel", "No questions loaded!");
            endGame();
        }
    }

    public boolean submitAnswer(String answer) {
        if (questions == null || currentQuestionIndex >= questions.size()) return false;

        TriviaQuestion q = questions.get(currentQuestionIndex);
        boolean isCorrect = q.getCorrectAnswer().equalsIgnoreCase(answer);

        TriviaGameModes mode = selectedMode.getValue();

        if (isCorrect) {
            score++;
            currentScore.setValue(score);
        } else {
            // For BLITZ and STREAK modes, end game on first wrong answer
            if (mode == TriviaGameModes.BLITZ || mode == TriviaGameModes.STREAK) {
                endGame();
            }
        }


        Log.d("TriviaViewModel", "submitAnswer() – Mode: " + mode + ", Correct: " + isCorrect);

        return isCorrect;
    }

    public void moveToNextQuestion() {
        currentQuestionIndex++;
        Log.d("TriviaViewModel", "moveToNextQuestion() called. Index: " + currentQuestionIndex);

        TriviaGameModes mode = selectedMode.getValue();

        // CLASSIC ends after 20 questions
        if (mode == TriviaGameModes.CLASSIC && currentQuestionIndex >= 20) {
            endGame();
            return;
        }

        if (questions != null && currentQuestionIndex < questions.size()) {
            currentQuestion.setValue(questions.get(currentQuestionIndex));
        } else {
            endGame();
        }
    }

    private void endGame() {
        cancelTimerIfRunning();
        updateHighScoreIfNeeded();
        gameState.setValue(TriviaGameState.GAME_OVER);
        Log.d("TriviaViewModel", "Game over triggered.");
    }

    private void updateHighScoreIfNeeded() {
        TriviaGameModes mode = selectedMode.getValue();
        if (mode == null) return;

        int oldHighScore = loadHighScore(mode);
        if (score > oldHighScore) {
            saveHighScore(mode, score);
            highScore.setValue(score); // Notify UI
        }
    }

    private int loadHighScore(TriviaGameModes mode) {
        return prefs.getInt("HIGH_SCORE_" + mode.name(), 0);
    }

    private void saveHighScore(TriviaGameModes mode, int value) {
        prefs.edit().putInt("HIGH_SCORE_" + mode.name(), value).apply();
    }

    private void startModeTimer(TriviaGameModes mode) {
        long durationMillis = (mode == TriviaGameModes.BLITZ) ? 30_000 : 60_000;

        cancelTimerIfRunning(); // In case it's already active
        modeTimer = new CountDownTimer(durationMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Optionally expose time remaining
            }

            @Override
            public void onFinish() {
                endGame();
            }
        };
        modeTimer.start();
    }

    private void cancelTimerIfRunning() {
        if (modeTimer != null) {
            modeTimer.cancel();
            modeTimer = null;
        }
    }

    public LiveData<TriviaQuestion> getCurrentQuestion() {
        return currentQuestion;
    }

    public LiveData<Integer> getCurrentScore() {
        return currentScore;
    }
}
