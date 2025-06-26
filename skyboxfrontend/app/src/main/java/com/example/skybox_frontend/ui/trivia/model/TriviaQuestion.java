package com.example.skybox_frontend.ui.trivia.model;

import java.util.Map;

public class TriviaQuestion {
    private String question;
    private Map<String, String> options;
    private String correctAnswer;
    private String category;
    private String difficulty;

    public String getQuestion() {
        return question;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getCategory() {
        return category;
    }

    public String getDifficulty() {
        return difficulty;
    }
}
