package com.example.skybox_frontend.ui.comms.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skybox_frontend.R;
import com.example.skybox_frontend.ui.comms.model.Contact;
import com.example.skybox_frontend.ui.comms.model.Message;
import com.example.skybox_frontend.ui.comms.viewmodel.CommsViewModel;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment implements ContactAdapter.OnContactClickListener {
    private static final String TAG = "ChatFragment";
    private ImageButton btnAddContact;
    private ImageButton btnAddGroup;
    private ImageButton btnVoice;
    private ImageButton btnSend;
    private RecyclerView sidebarList;
    private RecyclerView chatRecyclerView;
    private TextView emptyMessageText;
    private MessageAdapter messageAdapter;
    private CommsViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(CommsViewModel.class);

        // Button references
        btnAddContact = view.findViewById(R.id.btn_add_contact);
        btnAddGroup = view.findViewById(R.id.btn_add_group);
        btnVoice = view.findViewById(R.id.btn_voice);
        btnSend = view.findViewById(R.id.sendButton);

        // Button click subscriptions
        btnAddContact.setOnClickListener(this::onAddContactClicked);
        btnAddGroup.setOnClickListener(this::onAddGroupClicked);
        btnVoice.setOnClickListener(this::onVoiceClicked);
        btnSend.setOnClickListener(this::onSendClicked);

        // Setup Contacts
        sidebarList = view.findViewById(R.id.sidebar_list);
        sidebarList.setLayoutManager(new LinearLayoutManager(requireContext()));
        ContactAdapter contactAdapter = new ContactAdapter(requireContext(), new ArrayList<>(), this);
        sidebarList.setAdapter(contactAdapter);

        // Setup Messages
        chatRecyclerView = view.findViewById(R.id.chatRecyclerView);
        emptyMessageText = view.findViewById(R.id.emptyMessageText);
        messageAdapter = new MessageAdapter(new ArrayList<>());
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        chatRecyclerView.setAdapter(messageAdapter);

        // Live update UI when contacts change
        viewModel.getContacts().observe(getViewLifecycleOwner(), contacts -> {
            contactAdapter.updateContacts(contacts);
        });

        // Observe the currently selected contact and update message view
        viewModel.getSelectedContact().observe(getViewLifecycleOwner(), contact -> {
            if (contact == null || contact.getThread() == null) {
                showEmptyMessage(true);
                return;
            }

            List<Message> messages = contact.getThread().getMessages();
            if (messages == null || messages.isEmpty()) {
                showEmptyMessage(true);
            } else {
                showEmptyMessage(false);
                messageAdapter.updateMessages(messages);
                chatRecyclerView.scrollToPosition(messages.size() - 1); // Scroll to latest message
            }
        });

        return view;
    }

    // Short Press Contact: Called by adapter
    @Override
    public void onContactClick(Contact contact) {
        // This brings up the edit contact menu - commented out while experimenting with UX
        // AddEditContactDialogFragment dialog = AddEditContactDialogFragment.newInstance(contact);
        // dialog.show(getParentFragmentManager(), "EditContact");

        viewModel.setSelectedContact(contact);
        Log.d(TAG, contact.getCallsign());
    }

    // Long Press Contact: Called by adapter
    @Override
    public void onContactLongClick(Contact contact) {
        viewModel.togglePin(contact); // Tells repository to handle pin/unpin logic
        Log.d(TAG, contact.getCallsign() + " pin toggled");
    }

    // On-Click Handlers
    private void onAddContactClicked(View v) {
        AddEditContactDialogFragment dialog = AddEditContactDialogFragment.newInstance(null);
        dialog.show(getParentFragmentManager(), "AddContact");
    }

    private void onAddGroupClicked(View v) {
        Log.d(TAG, "Add Group clicked");
    }

    private void onVoiceClicked(View v) {
        Log.d(TAG, "Voice Mode clicked");
    }

    private void onSendClicked(View v) {
        Log.d(TAG, "Send Message clicked");
    }

    // Utility: Toggles placeholder message if no history exists
    private void showEmptyMessage(boolean show) {
        if (emptyMessageText != null && chatRecyclerView != null) {
            emptyMessageText.setVisibility(show ? View.VISIBLE : View.GONE);
            chatRecyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
