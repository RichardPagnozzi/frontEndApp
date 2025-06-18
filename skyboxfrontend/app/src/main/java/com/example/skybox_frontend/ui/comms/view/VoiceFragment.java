package com.example.skybox_frontend.ui.comms.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.skybox_frontend.R;
import com.example.skybox_frontend.ui.comms.viewmodel.VoiceViewModel;

public class VoiceFragment extends Fragment {

    private VoiceViewModel viewModel;
    private EditText inputCallsign;
    private EditText inputIp;
    private View viewInput;
    private View viewRinging;
    private View viewTimeout;
    private View participantsGrid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_voice, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(VoiceViewModel.class);

        inputCallsign = view.findViewById(R.id.input_callsign);
        inputIp = view.findViewById(R.id.input_ip);
        viewInput = view.findViewById(R.id.view_input);
        viewRinging = view.findViewById(R.id.view_ringing);
        viewTimeout = view.findViewById(R.id.view_timeout);
        participantsGrid = view.findViewById(R.id.participants_grid);

        view.findViewById(R.id.btn_start_call).setOnClickListener(v -> {
            String callsign = inputCallsign.getText().toString().trim();
            String ip = inputIp.getText().toString().trim();
            viewModel.startCall(callsign, ip);
        });

        viewModel.getCallState().observe(getViewLifecycleOwner(), callState -> {
            if (callState == null) return;

            viewInput.setVisibility(callState == VoiceViewModel.CallState.IDLE ? View.VISIBLE : View.GONE);
            viewRinging.setVisibility(callState == VoiceViewModel.CallState.RINGING ? View.VISIBLE : View.GONE);
            viewTimeout.setVisibility(callState == VoiceViewModel.CallState.TIMED_OUT ? View.VISIBLE : View.GONE);
            participantsGrid.setVisibility(callState == VoiceViewModel.CallState.CONNECTED ? View.VISIBLE : View.GONE);
        });
    }
}
