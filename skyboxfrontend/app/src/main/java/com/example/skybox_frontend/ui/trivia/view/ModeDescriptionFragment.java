package com.example.skybox_frontend.ui.trivia.view;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.skybox_frontend.R;
import com.example.skybox_frontend.ui.trivia.model.TriviaGameModes;

public class ModeDescriptionFragment extends Fragment {

    private static final String ARG_GAME_MODE = "arg_game_mode";
    private TriviaGameModes gameMode;

    public static ModeDescriptionFragment newInstance(TriviaGameModes mode) {
        ModeDescriptionFragment fragment = new ModeDescriptionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_GAME_MODE, mode.name());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String modeName = getArguments().getString(ARG_GAME_MODE);
            gameMode = TriviaGameModes.valueOf(modeName);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trivia_mode_summary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView title = view.findViewById(R.id.summaryTitle);
        TextView description = view.findViewById(R.id.summaryDescription);
        TextView highScore = view.findViewById(R.id.highScoreText);

        if (gameMode != null) {
            title.setText(gameMode.getTitle());
            description.setText(gameMode.getDescription());
            highScore.setText("High Score: " + gameMode.getHighScore());
        }

        setupButtons(view);
    }

    private void setupButtons(@NonNull View view) {
        Button btnBack = view.findViewById(R.id.btnBack);
        Button btnPlay = view.findViewById(R.id.btnPlay);

        btnBack.setOnClickListener(v -> requireActivity().onBackPressed());

        btnPlay.setOnClickListener(v -> {
            Fragment gameplayFragment = new GameplayFragment();
            FragmentTransaction transaction = requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction();
            transaction.replace(R.id.trivia_fragment_container, gameplayFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
    }
}
