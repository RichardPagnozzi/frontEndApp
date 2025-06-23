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
    private CommsViewModel viewModel;
    private ImageButton btnAddContact;
    private ImageButton btnAddGroup;
    private ImageButton btnVoice;
    private ImageButton btnSend;
    private RecyclerView sidebarList;
    private RecyclerView chatRecyclerView;
    private TextView emptyMessageText;
    private MessageAdapter messageAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(CommsViewModel.class);

        btnAddContact = view.findViewById(R.id.btn_add_contact);
        btnAddGroup = view.findViewById(R.id.btn_add_group);
        btnVoice = view.findViewById(R.id.btn_voice);
        btnSend = view.findViewById(R.id.sendButton);

        btnAddContact.setOnClickListener(v -> {
            AddEditContactDialogFragment dialog = AddEditContactDialogFragment.newInstance(null);
            dialog.show(getParentFragmentManager(), "AddContact");
        });

        btnAddGroup.setOnClickListener(v -> {
            Log.d(TAG, "Add Group clicked");
        });

        btnVoice.setOnClickListener(v -> {
            Log.d(TAG, "Voice Mode clicked");
        });

        btnSend.setOnClickListener(v -> {
            Log.d(TAG, "Send Message clicked");
        });

        sidebarList = view.findViewById(R.id.sidebar_list);
        sidebarList.setLayoutManager(new LinearLayoutManager(requireContext()));
        ContactAdapter contactAdapter = new ContactAdapter(requireContext(), new ArrayList<>(), this);
        sidebarList.setAdapter(contactAdapter);

        chatRecyclerView = view.findViewById(R.id.chatRecyclerView);
        emptyMessageText = view.findViewById(R.id.emptyMessageText);
        messageAdapter = new MessageAdapter(new ArrayList<>());
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        chatRecyclerView.setAdapter(messageAdapter);

        viewModel.getContacts().observe(getViewLifecycleOwner(), contacts -> {
            contactAdapter.updateContacts(contacts);
        });

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
                chatRecyclerView.scrollToPosition(messages.size() - 1);
            }
        });
    }

    @Override
    public void onContactClick(Contact contact) {
        viewModel.setSelectedContact(contact);
        Log.d(TAG, "Contact clicked: " + contact.getCallsign());
    }

    @Override
    public void onContactLongClick(Contact contact) {
        viewModel.togglePin(contact);
        Log.d(TAG, "Contact long-clicked: " + contact.getCallsign());
    }

    private void showEmptyMessage(boolean show) {
        emptyMessageText.setVisibility(show ? View.VISIBLE : View.GONE);
        chatRecyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
