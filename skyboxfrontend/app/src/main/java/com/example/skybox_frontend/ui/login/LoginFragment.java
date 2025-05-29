package com.example.skybox_frontend.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.skybox_frontend.MainActivity;
import com.example.skybox_frontend.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginFragment extends Fragment {

    // <editor-fold desc="Members">
    private LoginViewModel viewModel;
    private TextView titleTextView;
    private TextInputEditText callsignInput;
    private TextInputEditText passwordInput;
    private MaterialButton loginButton;
    // </editor-fold>

    // <editor-fold desc="Lifecycle">
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);

        initializeViews(root);
        initializeViewModel();
        observeViewModel();
        setupListeners();

        return root;
    }
    // </editor-fold>

    // <editor-fold desc="Helper Methods">
    private void initializeViews(View root) {
        titleTextView = root.findViewById(R.id.login_title);
        callsignInput = root.findViewById(R.id.callsign_input);
        passwordInput = root.findViewById(R.id.password_input);
        loginButton = root.findViewById(R.id.login_button);
    }

    private void initializeViewModel() {
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    }

    private void observeViewModel() {
        viewModel.getLoginTitle().observe(getViewLifecycleOwner(), title -> {
            if (title != null) titleTextView.setText(title);
        });

        viewModel.getLoginSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success != null && success) {
                ((MainActivity) requireActivity()).onLoginSuccess();
            } else if (success != null) {
                Toast.makeText(requireContext(), "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupListeners() {
        loginButton.setOnClickListener(v -> {
            String callsign = callsignInput.getText() != null ? callsignInput.getText().toString() : "";
            String password = passwordInput.getText() != null ? passwordInput.getText().toString() : "";
            String chatIP = ""; // Optional input, not yet used

            viewModel.attemptLogin(callsign, password, chatIP);
        });
    }
    // </editor-fold>
}

