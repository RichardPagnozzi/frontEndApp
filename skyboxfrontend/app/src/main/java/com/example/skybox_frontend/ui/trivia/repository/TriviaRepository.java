// Directory: ui/trivia/repository/TriviaRepository.java

package com.example.skybox_frontend.ui.trivia.repository;

import android.content.Context;
import android.util.Log;

import com.example.skybox_frontend.R;
import com.example.skybox_frontend.ui.trivia.model.TriviaQuestion;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class TriviaRepository {
    private static final String TAG = "TriviaRepository";

    private final Context context;

    public TriviaRepository(Context context) {
        this.context = context.getApplicationContext();
    }

    public List<TriviaQuestion> loadQuestions() {
        try {
            InputStream inputStream = context.getAssets().open("trivia_questions.json");
            InputStreamReader reader = new InputStreamReader(inputStream);
            Type listType = new TypeToken<List<TriviaQuestion>>() {}.getType();
            return new Gson().fromJson(reader, listType);
        } catch (Exception e) {
            Log.e(TAG, "Failed to load trivia questions: ", e);
            return Collections.emptyList();
        }
    }
}
