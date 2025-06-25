package com.example.skybox_frontend.ui.trivia.view;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.skybox_frontend.R;
import com.example.skybox_frontend.ui.trivia.model.TriviaGameModes;
import com.example.skybox_frontend.ui.trivia.viewmodel.TriviaViewModel;

public class ModeSelectFragment extends Fragment {

    private TriviaViewModel triviaViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trivia_mode_select, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        triviaViewModel = new ViewModelProvider(requireActivity()).get(TriviaViewModel.class);

        Button btnClassic = view.findViewById(R.id.btnClassic);
        Button btnTimed = view.findViewById(R.id.btnTimed);
        Button btnBlitz = view.findViewById(R.id.btnBlitz);
        Button btnStreak = view.findViewById(R.id.btnStreak);

        btnClassic.setOnClickListener(v -> onModeSelected(TriviaGameModes.CLASSIC));
        btnTimed.setOnClickListener(v -> onModeSelected(TriviaGameModes.TIMED));
        btnBlitz.setOnClickListener(v -> onModeSelected(TriviaGameModes.BLITZ));
        btnStreak.setOnClickListener(v -> onModeSelected(TriviaGameModes.STREAK));
    }

    private void onModeSelected(TriviaGameModes mode) {
        triviaViewModel.selectMode(mode);

        ModeDescriptionFragment fragment = ModeDescriptionFragment.newInstance(mode);
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.trivia_fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
