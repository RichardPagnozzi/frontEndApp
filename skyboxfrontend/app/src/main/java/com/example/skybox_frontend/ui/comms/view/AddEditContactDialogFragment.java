package com.example.skybox_frontend.ui.comms.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.TypedValue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.skybox_frontend.R;
import com.example.skybox_frontend.ui.comms.model.Contact;
import com.example.skybox_frontend.ui.comms.viewmodel.CommsViewModel;

import java.util.List;
import java.util.Random;

/**
 * DialogFragment that allows the user to add a new contact or edit an existing one.
 */
public class AddEditContactDialogFragment extends DialogFragment {

    private EditText inputCallsign;
    private EditText inputIp;
    private Button btnSave;
    private boolean isEditMode = false;
    private static final String ARG_CONTACT = "contact";
    private Contact existingContact;
    private CommsViewModel viewModel;

    // Create a new instance of the dialog, optionally with a contact to edit
    public static AddEditContactDialogFragment newInstance(@Nullable Contact contact) {
        AddEditContactDialogFragment fragment = new AddEditContactDialogFragment();
        Bundle args = new Bundle();
        if (contact != null) {
            args.putSerializable(ARG_CONTACT, contact); // Pass the contact as a serializable object
        }
        fragment.setArguments(args);
        return fragment;
    }

    // Adjusts the dialog's width and height when it starts.
    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(
                    (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics()),
                    (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics())
            );
        }
    }

    // Sets up internal state
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            existingContact = (Contact) getArguments().getSerializable(ARG_CONTACT);
            isEditMode = existingContact != null;
        }
        setStyle(STYLE_NORMAL, R.style.CustomDialogStyle); // Apply custom dialog theme
    }

    // Inflates the layout and sets up UI elements and event listeners.
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_add_edit_contact, container, false);

        inputCallsign = view.findViewById(R.id.input_callsign);
        inputIp = view.findViewById(R.id.input_ip);
        btnSave = view.findViewById(R.id.btn_save);

        // Obtain shared ViewModel scoped to activity
        viewModel = new ViewModelProvider(requireActivity()).get(CommsViewModel.class);

        // Pre-fill inputs if editing an existing contact
        if (isEditMode && existingContact != null) {
            inputCallsign.setText(existingContact.getCallsign());
            inputIp.setText(existingContact.getIp());
            btnSave.setText("Update");
        }

        btnSave.setOnClickListener(v -> onSaveClicked());

        return view;
    }

    // On Save/Update Clicked
    private void onSaveClicked() {
        String callsign = inputCallsign.getText().toString().trim();
        String ip = inputIp.getText().toString().trim();

        // Input validation
        if (TextUtils.isEmpty(callsign)) {
            inputCallsign.setError("Callsign is required");
            return;
        }
        if (TextUtils.isEmpty(ip)) {
            inputIp.setError("IP address is required");
            return;
        }

        // Create new contact with either existing color or a new random color
        Contact newContact = new Contact(callsign, ip,
                isEditMode ? existingContact.getColorHex() : getRandomColorHex());

        if (isEditMode) {
            // Update the contact if it still exists in the current list
            List<Contact> currentList = viewModel.getContacts().getValue();
            if (currentList != null) {
                int index = currentList.indexOf(existingContact);
                if (index != -1) {
                    viewModel.updateContact(index, newContact);
                } else {
                    Toast.makeText(requireContext(), "Could not update contact", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            // Add the new contact
            viewModel.addContact(newContact);
        }

        dismiss(); // Close the dialog
    }

    // Get Random Color
    private String getRandomColorHex() {
        Random random = new Random();
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);
        return String.format("#%02X%02X%02X", red, green, blue);
    }
}
