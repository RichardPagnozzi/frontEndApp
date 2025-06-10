package com.example.skybox_frontend.ui.comms.view;

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

// Adapter class to bind Contact data into RecyclerView items
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private final Context context;
    private final List<Contact> contacts;

    // Interface to delegate click events to the host fragment
    public interface OnContactClickListener {
        void onContactClick(Contact contact);
        void onContactLongClick(Contact contact);
    }

    private final OnContactClickListener clickListener;

    // Constructor injects context, list of contacts, and a click listener
    public ContactAdapter(Context context, List<Contact> contactList, OnContactClickListener clickListener) {
        this.context = context;
        this.contacts = contactList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout for each contact
        View view = LayoutInflater.from(context).inflate(R.layout.contact_pill, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contacts.get(position);

        // Set the text to display the contacts callsign
        holder.textInitials.setText(contact.getCallsign());

        // Set a circular background using the contact's color hex
        GradientDrawable bgShape = new GradientDrawable();
        bgShape.setShape(GradientDrawable.OVAL);
        try {
            bgShape.setColor(Color.parseColor(contact.getColorHex()));
        } catch (IllegalArgumentException e) {
            // Fallback to gray if the color hex is invalid
            bgShape.setColor(Color.GRAY);
        }
        if (contact.getIsPinned()) {
            bgShape.setStroke(4, Color.WHITE);
        }
        holder.textInitials.setBackground(bgShape);

        // Register click listener for the contact pill
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onContactClick(contact);
            }
        });

        // Register long click listener for contact pill
        holder.itemView.setOnLongClickListener(v -> {
            if (clickListener != null) {
                clickListener.onContactLongClick(contact);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    // Update the contact list and notify listeners
    public void updateContacts(List<Contact> newContacts) {
        contacts.clear();
        contacts.addAll(newContacts);
        notifyDataSetChanged();
    }

    // ViewHolder to cache the text view reference
    static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView textInitials;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            textInitials = itemView.findViewById(R.id.textInitials);
        }
    }
}
