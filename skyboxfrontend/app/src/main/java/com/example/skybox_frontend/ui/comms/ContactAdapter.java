package com.example.skybox_frontend.ui.comms;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skybox_frontend.R;
import com.example.skybox_frontend.ui.comms.model.Contact;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private final Context context;
    private final List<Contact> contacts;

    // 🔌 Listener interface to send click events to the fragment
    public interface OnContactClickListener {
        void onContactClick(Contact contact);
    }

    private final OnContactClickListener clickListener;

    // 🔧 Now accepts a listener in the constructor
    public ContactAdapter(Context context, List<Contact> contactList, OnContactClickListener clickListener) {
        this.context = context;
        this.contacts = contactList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_pill, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contacts.get(position);

        // 🧠 Set text
        holder.textInitials.setText(contact.getCallsign());

        // 🎨 Apply color as circular background
        GradientDrawable bgShape = new GradientDrawable();
        bgShape.setShape(GradientDrawable.OVAL);
        try {
            bgShape.setColor(Color.parseColor(contact.getColorHex()));
        } catch (IllegalArgumentException e) {
            bgShape.setColor(Color.GRAY);
        }
        holder.textInitials.setBackground(bgShape);

        // 👆 Click support
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onContactClick(contact);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView textInitials;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            textInitials = itemView.findViewById(R.id.textInitials);
        }
    }
}
