package com.example.skybox_frontend.ui.comms.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skybox_frontend.R;
import com.example.skybox_frontend.ui.comms.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment implements ContactAdapter.OnContactClickListener {

    private static final String TAG = "ChatFragment";

    // UI elements
    private ImageButton btnAddContact;
    private ImageButton btnAddGroup;
    private ImageButton btnSelectAda;
    private ImageButton btnVoice;
    private ImageButton btnSend;
    private RecyclerView sidebarList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        // 🔗 Button references
        btnAddContact = view.findViewById(R.id.btn_add_contact);
        btnAddGroup = view.findViewById(R.id.btn_add_group);
        btnSelectAda = view.findViewById(R.id.btn_select_ada);
        btnVoice = view.findViewById(R.id.btn_voice);
        btnSend = view.findViewById(R.id.sendButton);

        // 🧠 Button click hooks
        btnAddContact.setOnClickListener(this::onAddContactClicked);
        btnAddGroup.setOnClickListener(this::onAddGroupClicked);
        btnSelectAda.setOnClickListener(this::onSelectAdaClicked);
        btnVoice.setOnClickListener(this::onVoiceClicked);
        btnSend.setOnClickListener(this::onSendClicked);

        // 📄 Setup RecyclerView
        sidebarList = view.findViewById(R.id.sidebar_list);
        sidebarList.setLayoutManager(new LinearLayoutManager(requireContext()));

        // 🧪 Dummy data
        List<Contact> dummyContacts = new ArrayList<>();
        dummyContacts.add(new Contact("MOM", "192.168.0.12", "#3DAEFF"));
        dummyContacts.add(new Contact("BRAVO 3", "10.0.0.5", "#FF5733"));
        dummyContacts.add(new Contact("HQ", "172.16.0.1", "#7D55FF"));
        dummyContacts.add(new Contact("HQ1", "172.16.0.1", "#7D6FFF"));
        dummyContacts.add(new Contact("HQ2", "172.16.0.1", "#7D1FFF"));

        // 🔗 Set adapter and pass click listener
        ContactAdapter contactAdapter = new ContactAdapter(requireContext(), dummyContacts, this);
        sidebarList.setAdapter(contactAdapter);

        return view;
    }

    // 🧠 Implementing click callback from adapter
    @Override
    public void onContactClick(Contact contact) {
        Log.d(TAG, "Contact clicked: " + contact.getCallsign() + " @ " + contact.getIp());
    }

    // Click handlers for other buttons
    private void onAddContactClicked(View v) {
        AddEditContactDialogFragment dialog = AddEditContactDialogFragment.newInstance(null);
        dialog.show(getParentFragmentManager(), "AddContact");
    }

    private void onAddGroupClicked(View v) {
        Log.d(TAG, "Add Group clicked");
    }

    private void onSelectAdaClicked(View v) {
        Log.d(TAG, "Select ADA Bot clicked");
    }

    private void onVoiceClicked(View v) {
        Log.d(TAG, "Voice Mode clicked");
    }

    private void onSendClicked(View v) {
        Log.d(TAG, "Send Message clicked");
    }
}
