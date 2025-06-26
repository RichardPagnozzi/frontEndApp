package com.example.skybox_frontend.ui.trivia.view;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.skybox_frontend.R;
import com.example.skybox_frontend.ui.trivia.model.TriviaGameModes;
import com.example.skybox_frontend.ui.trivia.viewmodel.TriviaViewModel;

public class EndGameFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trivia_game_over, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TriviaViewModel viewModel = new ViewModelProvider(requireActivity()).get(TriviaViewModel.class);

        TextView scoreText = view.findViewById(R.id.currentScoreText);
        TextView highScoreText = view.findViewById(R.id.highScoreText);
        Button btnReplay = view.findViewById(R.id.btnReplay);
        Button btnEnd = view.findViewById(R.id.btnEndGame);

        scoreText.setText("Score: " + viewModel.getCurrentScore().getValue());
        highScoreText.setText("High Score: " + viewModel.getHighScore().getValue());

        btnReplay.setOnClickListener(v -> {
            viewModel.startGame(); // Restart the game using the same selected mode
            Fragment gameplayFragment = new GameplayFragment();
            FragmentTransaction transaction = requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction();
            transaction.replace(R.id.trivia_fragment_container, gameplayFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        btnEnd.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        });
    }
}
