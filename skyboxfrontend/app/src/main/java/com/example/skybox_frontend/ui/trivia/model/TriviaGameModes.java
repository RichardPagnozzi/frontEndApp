// Directory: model/TriviaGameModes.java

package com.example.skybox_frontend.ui.trivia.model;

// Holds enum for game mode state
public enum TriviaGameModes {
    CLASSIC("Classic", "Answer 20 questions at your own pace.", 0),
    TIMED("Timed", "Answer as many questions as you can within 60 seconds.", 0),
    BLITZ("Blitz", "Speed round with increasing difficulty and no room for error.", 0),
    STREAK("Streak", "Build the longest correct answer streak possible.", 0);

    private final String title;
    private final String description;
    private int highScore;

    TriviaGameModes(String title, String description, int highScore) {
        this.title = title;
        this.description = description;
        this.highScore = highScore;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }
}
