package com.example.skybox_frontend.ui.comms.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skybox_frontend.R;
import com.example.skybox_frontend.ui.comms.model.Contact;
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
    private RecyclerView contactsList;
    private ParticipantAdapter participantAdapter;
    private boolean isMuted = false;
    private boolean isVideoOn = false;
    private ImageButton btnMute;
    private ImageButton btnVideo;
    private ImageButton btnEndCall;

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
        viewRinging = view.findViewById(R.id.view_call_ringing);
        viewTimeout = view.findViewById(R.id.view_call_timeout);
        viewGallery = view.findViewById(R.id.view_call_gallery);
        participantGrid = viewGallery.findViewById(R.id.participant_grid);
        contactsList = view.findViewById(R.id.contacts_list);

        btnMute = viewGallery.findViewById(R.id.btn_mute);
        btnVideo = viewGallery.findViewById(R.id.btn_video);
        btnEndCall = viewGallery.findViewById(R.id.btn_end_call);

        setupContactsList();

        btnMute.setOnClickListener(v -> {
            isMuted = !isMuted;
            updateMuteButtonUI();
            Log.d("VoiceFragment", "Global mute toggled: " + isMuted);
        });

        btnVideo.setOnClickListener(v -> {
            isVideoOn = !isVideoOn;
            updateVideoButtonUI();
            Log.d("VoiceFragment", "Global video toggled: " + isVideoOn);
        });

        btnEndCall.setOnClickListener(v -> {
            Log.d("VoiceFragment", "End call clicked");
            viewModel.endCall();
        });

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
                resetGalleryButtons();
            }
        });
    }

    private void setupContactsList() {
        List<Contact> mockContacts = List.of(
                new Contact("Alpha", "192.168.0.1", "#3DAEFF"),
                new Contact("Bravo", "192.168.0.2", "#FF5722"),
                new Contact("Charlie", "192.168.0.3", "#4CAF50"),
                new Contact("Delta", "192.168.0.4", "#FFC107")
        );

        ContactAdapter adapter = new ContactAdapter(requireContext(), mockContacts, new ContactAdapter.OnContactClickListener() {
            @Override
            public void onContactClick(Contact contact) {
                inputCallsign.setText(contact.getCallsign());
                inputIp.setText(contact.getIp());
                // viewModel.startCall(contact.getCallsign(), contact.getIp());
                Log.d("VoiceFragment", "Contact clicked: " + contact.getCallsign());
            }

            @Override
            public void onContactLongClick(Contact contact) {
                Log.d("VoiceFragment", "Contact long clicked: " + contact.getCallsign());
            }
        });

        contactsList.setLayoutManager(new LinearLayoutManager(requireContext()));
        contactsList.setAdapter(adapter);
    }

    private void setupParticipants() {
        List<Participant> mockParticipants = List.of(
                new Participant("Alpha", "192.168.0.1", false, false, true),
                new Participant("Bravo", "192.168.0.2", false, false, true),
                new Participant("Charlie", "192.168.0.3", false, true, true),
                new Participant("Delta", "192.168.0.4", false, false, true)
        );

        int participantCount = mockParticipants.size();
        int spanCount = participantCount == 1 ? 1 : (participantCount == 2 ? 2 : 3);

        GridLayoutManager manager = new GridLayoutManager(requireContext(), spanCount);
        participantGrid.setLayoutManager(manager);

        if (participantGrid.getItemDecorationCount() == 0) {
            participantGrid.addItemDecoration(new GridSpacingItemDecoration(spanCount, 16, true));
        }

        participantAdapter = new ParticipantAdapter(requireContext(), mockParticipants);
        participantGrid.setAdapter(participantAdapter);
    }

    private void resetGalleryButtons() {
        isMuted = false;
        isVideoOn = false;
        updateMuteButtonUI();
        updateVideoButtonUI();
        Log.d("VoiceFragment", "Gallery buttons reset");
    }

    private void updateMuteButtonUI() {
        btnMute.setImageResource(isMuted ? R.drawable.ic_mic_off : R.drawable.ic_mic_on);
        btnMute.setColorFilter(ContextCompat.getColor(requireContext(), isMuted ? R.color.status_red : R.color.accent));
    }

    private void updateVideoButtonUI() {
        btnVideo.setImageResource(isVideoOn ? R.drawable.ic_camera_on : R.drawable.ic_camera_off);
        btnVideo.setColorFilter(ContextCompat.getColor(requireContext(), isVideoOn ? R.color.accent : R.color.status_red));
    }
}
