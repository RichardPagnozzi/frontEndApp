package com.example.skybox_frontend.ui.trivia.view;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.skybox_frontend.R;
import com.example.skybox_frontend.ui.trivia.model.TriviaGameModes;
import com.example.skybox_frontend.ui.trivia.model.TriviaGameState;
import com.example.skybox_frontend.ui.trivia.model.TriviaQuestion;
import com.example.skybox_frontend.ui.trivia.viewmodel.TriviaViewModel;

import java.util.Map;

public class GameplayFragment extends Fragment {

    private TriviaViewModel viewModel;

    private TextView questionText, gameModeTitle, scoreCounter, timerText;
    private Button btnNext;
    private Button[] optionButtons;
    private String selectedAnswer = null;
    private CountDownTimer countDownTimer;
    private TriviaQuestion currentQuestion;

    private static final long TIMER_DURATION = 30_000;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trivia_game, container, false);

        // Observe Game Over state and transition to EndGameFragment
        TriviaViewModel viewModel = new ViewModelProvider(requireActivity()).get(TriviaViewModel.class);
        viewModel.getGameState().observe(getViewLifecycleOwner(), state -> {
            if (state == TriviaGameState.GAME_OVER) {
                Fragment endGameFragment = new EndGameFragment();
                FragmentTransaction transaction = requireActivity()
                        .getSupportFragmentManager()
                        .beginTransaction();
                transaction.replace(R.id.trivia_fragment_container, endGameFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(TriviaViewModel.class);

        // UI references
        gameModeTitle = view.findViewById(R.id.gameModeTitle);
        scoreCounter = view.findViewById(R.id.scoreCounter);
        questionText = view.findViewById(R.id.questionText);
        timerText = view.findViewById(R.id.timerText);
        btnNext = view.findViewById(R.id.btnNext);

        optionButtons = new Button[]{
                view.findViewById(R.id.btnOptionA),
                view.findViewById(R.id.btnOptionB),
                view.findViewById(R.id.btnOptionC),
                view.findViewById(R.id.btnOptionD)
        };

        viewModel.getSelectedMode().observe(getViewLifecycleOwner(), mode -> {
            if (mode != null) gameModeTitle.setText(mode.getTitle());
        });

        viewModel.getCurrentScore().observe(getViewLifecycleOwner(), score ->
                scoreCounter.setText("Score: " + score));

        viewModel.getCurrentQuestion().observe(getViewLifecycleOwner(), this::displayQuestion);

        btnNext.setText("Next");
        btnNext.setOnClickListener(v -> {
            if (selectedAnswer == null) {
                showToast("Please select an answer.");
                return;
            }

            // Submit + feedback
            String answerKey = selectedAnswer.trim().substring(0, 1);
            boolean correct = viewModel.submitAnswer(answerKey);
            highlightResults(answerKey, correct);
            showToast(correct ? "Correct!" : "Incorrect");

            // Disable buttons during feedback
            for (Button btn : optionButtons) btn.setEnabled(false);
            btnNext.setEnabled(false);

            // Delay → next question
            btnNext.postDelayed(() -> {
                selectedAnswer = null;
                resetButtonColors();
                for (Button btn : optionButtons) btn.setEnabled(true);
                btnNext.setEnabled(true);
                viewModel.moveToNextQuestion();
            }, 1500);
        });

        for (Button btn : optionButtons) {
            btn.setOnClickListener(v -> {
                selectedAnswer = btn.getText().toString();
                highlightSelection(btn);
            });
        }

        viewModel.startGame();
    }

    private void displayQuestion(TriviaQuestion question) {
        if (question == null) return;
        this.currentQuestion = question;

        questionText.setText(question.getQuestion());
        Map<String, String> options = question.getOptions();
        optionButtons[0].setText("A. " + options.get("A"));
        optionButtons[1].setText("B. " + options.get("B"));
        optionButtons[2].setText("C. " + options.get("C"));
        optionButtons[3].setText("D. " + options.get("D"));

        resetButtonColors();
        selectedAnswer = null;

        for (Button btn : optionButtons) btn.setEnabled(true);

        if (requiresTimer()) startTimer();
        else timerText.setVisibility(View.GONE);
    }

    private void highlightSelection(Button selectedBtn) {
        resetButtonColors();
        selectedBtn.setBackgroundTintList(getResources().getColorStateList(R.color.secondary, null));
    }

    private void resetButtonColors() {
        if (!isAdded() || getContext() == null) return;

        for (Button btn : optionButtons) {
            btn.setBackgroundTintList(getResources().getColorStateList(R.color.accent, null));
        }
    }


    private void highlightResults(String selectedKey, boolean correct) {
        String correctKey = currentQuestion.getCorrectAnswer().toUpperCase();

        for (Button btn : optionButtons) {
            String key = btn.getText().toString().substring(0, 1).toUpperCase();
            if (key.equals(correctKey)) {
                btn.setBackgroundTintList(getResources().getColorStateList(R.color.status_green, null));
            } else if (key.equals(selectedKey) && !correct) {
                btn.setBackgroundTintList(getResources().getColorStateList(R.color.status_red, null));
            } else {
                btn.setBackgroundTintList(getResources().getColorStateList(R.color.accent, null));
            }
        }
    }

    private boolean requiresTimer() {
        TriviaGameModes mode = viewModel.getSelectedMode().getValue();
        return mode == TriviaGameModes.TIMED || mode == TriviaGameModes.BLITZ;
    }

    private void startTimer() {
        if (countDownTimer != null) countDownTimer.cancel();

        timerText.setVisibility(View.VISIBLE);
        timerText.setText("30s");

        countDownTimer = new CountDownTimer(TIMER_DURATION, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerText.setText((millisUntilFinished / 1000) + "s");
            }

            @Override
            public void onFinish() {
                timerText.setText("0s");

                if (selectedAnswer == null) {
                    viewModel.submitAnswer(""); // no answer
                    highlightResults("", false);
                    showToast("Time's up!");

                    for (Button btn : optionButtons) btn.setEnabled(false);
                    btnNext.setEnabled(false);

                    btnNext.postDelayed(() -> {
                        selectedAnswer = null;
                        resetButtonColors();
                        for (Button btn : optionButtons) btn.setEnabled(true);
                        btnNext.setEnabled(true);
                        viewModel.moveToNextQuestion();
                    }, 1500);
                }
            }
        };
        countDownTimer.start();
    }

    private Toast currentToast;

    private void showToast(String message) {
        if (currentToast != null) currentToast.cancel();

        currentToast = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT);
        currentToast.show();

        // Remove after 1.5 seconds to match our delay time
        new Handler().postDelayed(() -> {
            if (currentToast != null) {
                currentToast.cancel();
            }
        }, 1500);
    }


    @Override
    public void onDestroyView() {
        if (countDownTimer != null) countDownTimer.cancel();
        super.onDestroyView();
    }
}
