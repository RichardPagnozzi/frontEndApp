package com.example.skybox_frontend.utilities;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skybox_frontend.R;
import com.example.skybox_frontend.ui.comms.model.Contact;
import com.example.skybox_frontend.ui.comms.view.ContactAdapter;

import java.util.List;

public class NavDrawerController {

    public interface NavDrawerListener {
        void onContactClicked(Contact contact);
        void onContactLongClicked(Contact contact);
        void onAddContactClicked();
        void onAddGroupClicked();
    }

    private final View sidebarRoot;
    private final Context context;
    private final ContactAdapter adapter;

    public NavDrawerController(View sidebarRoot, Context context, List<Contact> contacts, NavDrawerListener listener) {
        this.sidebarRoot = sidebarRoot;
        this.context = context;

        // IMPORTANT: Make sure this ID matches your actual layout ID
        RecyclerView contactList = sidebarRoot.findViewById(R.id.contacts_list);
        if (contactList == null) {
            throw new IllegalStateException("RecyclerView (contacts_list) not found in NavDrawerController layout");
        }

        contactList.setLayoutManager(new LinearLayoutManager(context));

        adapter = new ContactAdapter(context, contacts, new ContactAdapter.OnContactClickListener() {
            @Override
            public void onContactClick(Contact contact) {
                listener.onContactClicked(contact);
            }

            @Override
            public void onContactLongClick(Contact contact) {
                listener.onContactLongClicked(contact);
            }
        });

        contactList.setAdapter(adapter);

        View btnAddContact = sidebarRoot.findViewById(R.id.btn_add_contact);
        View btnAddGroup = sidebarRoot.findViewById(R.id.btn_add_group);

        if (btnAddContact != null) {
            btnAddContact.setOnClickListener(v -> listener.onAddContactClicked());
        }

        if (btnAddGroup != null) {
            btnAddGroup.setOnClickListener(v -> listener.onAddGroupClicked());
        }
    }

    public void updateContacts(List<Contact> newContacts) {
        adapter.updateContacts(newContacts);
    }
}
