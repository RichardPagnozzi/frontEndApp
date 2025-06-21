package com.example.skybox_frontend.ui.comms.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skybox_frontend.R;
import com.example.skybox_frontend.ui.comms.model.Participant;
import com.example.skybox_frontend.ui.comms.viewmodel.VoiceViewModel;

import java.util.List;

public class VoiceFragment extends Fragment {

    private VoiceViewModel viewModel;
    private EditText inputCallsign;
    private EditText inputIp;
    private View viewInput;
    private View viewRinging;
    private View viewTimeout;
    private View viewGallery;
    private RecyclerView participantGrid;
    private ParticipantAdapter participantAdapter;

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
        viewGallery = view.findViewById(R.id.view_call_gallery);

        participantGrid = viewGallery.findViewById(R.id.participant_grid);

        view.findViewById(R.id.btn_start_call).setOnClickListener(v -> {
            String callsign = inputCallsign.getText().toString().trim();
            String ip = inputIp.getText().toString().trim();
            viewModel.startCall(callsign, ip);
        });

        viewRinging.setOnClickListener(v -> viewModel.mockConnect());
        viewTimeout.setOnClickListener(v -> viewModel.endCall());

        viewModel.getCallState().observe(getViewLifecycleOwner(), callState -> {
            if (callState == null) return;

            viewInput.setVisibility(callState == VoiceViewModel.CallState.IDLE ? View.VISIBLE : View.GONE);
            viewRinging.setVisibility(callState == VoiceViewModel.CallState.RINGING ? View.VISIBLE : View.GONE);
            viewTimeout.setVisibility(callState == VoiceViewModel.CallState.TIMED_OUT ? View.VISIBLE : View.GONE);
            viewGallery.setVisibility(callState == VoiceViewModel.CallState.CONNECTED ? View.VISIBLE : View.GONE);

            if (callState == VoiceViewModel.CallState.CONNECTED) {
                setupParticipants();
            }
        });
    }

    private void setupParticipants() {
        List<Participant> mockParticipants = List.of(
                new Participant("Alpha", "192.168.0.1", false, false),
                new Participant("Bravo", "192.168.0.2", true, false),
                new Participant("Charlie", "192.168.0.3", false, true),
                new Participant("Delta", "192.168.0.4", true, true)
        );

        int participantCount = mockParticipants.size();
        int spanCount;
        if (participantCount == 1) {
            spanCount = 1;
        } else if (participantCount == 2) {
            spanCount = 2;
        } else {
            spanCount = 3; // or 4 if you want them smaller
        }

        GridLayoutManager manager = new GridLayoutManager(requireContext(), spanCount);
        participantGrid.setLayoutManager(manager);

        if (participantGrid.getItemDecorationCount() == 0) {
            participantGrid.addItemDecoration(new GridSpacingItemDecoration(spanCount, 16, true));
        }

        participantAdapter = new ParticipantAdapter(requireContext(), mockParticipants);
        participantGrid.setAdapter(participantAdapter);
    }
}
