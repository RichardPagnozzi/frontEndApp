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

import java.util.Random;

public class AddEditContactDialogFragment extends DialogFragment {

    private EditText inputCallsign;
    private EditText inputIp;
    private Button btnSave;

    private boolean isEditMode = false;

    private CommsViewModel viewModel;
    private String getRandomColorHex() {
        Random random = new Random();
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);
        return String.format("#%02X%02X%02X", red, green, blue);
    }

    public static AddEditContactDialogFragment newInstance(@Nullable Contact contact) {
        AddEditContactDialogFragment fragment = new AddEditContactDialogFragment();
        Bundle args = new Bundle();
        if (contact != null) {
            args.putString("callsign", contact.getCallsign());
            args.putString("ip", contact.getIp());
            args.putString("color", contact.getColorHex());
        }
        fragment.setArguments(args);
        return fragment;
    }

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CustomDialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_add_edit_contact, container, false);

        inputCallsign = view.findViewById(R.id.input_callsign);
        inputIp = view.findViewById(R.id.input_ip);
        btnSave = view.findViewById(R.id.btn_save);

        viewModel = new ViewModelProvider(requireActivity()).get(CommsViewModel.class);

        // Check for edit mode
        Bundle args = getArguments();
        if (args != null && args.containsKey("callsign")) {
            isEditMode = true;
            inputCallsign.setText(args.getString("callsign"));
            inputIp.setText(args.getString("ip"));
            btnSave.setText("Update");
        }

        btnSave.setOnClickListener(v -> onSaveClicked());

        return view;
    }

    private void onSaveClicked() {
        String callsign = inputCallsign.getText().toString().trim();
        String ip = inputIp.getText().toString().trim();

        if (TextUtils.isEmpty(callsign)) {
            inputCallsign.setError("Callsign is required");
            return;
        }
        if (TextUtils.isEmpty(ip)) {
            inputIp.setError("IP address is required");
            return;
        }

        Contact contact = new Contact(callsign, ip, getRandomColorHex());

        if (isEditMode) {
            Toast.makeText(requireContext(), "Edit mode not yet implemented", Toast.LENGTH_SHORT).show();
        } else {
            viewModel.addContact(contact);
        }

        dismiss();
    }
}
