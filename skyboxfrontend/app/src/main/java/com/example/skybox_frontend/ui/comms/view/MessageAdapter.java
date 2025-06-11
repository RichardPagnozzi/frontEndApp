package com.example.skybox_frontend.ui.comms.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skybox_frontend.R;
import com.example.skybox_frontend.ui.comms.model.Message;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_INCOMING = 0;
    private static final int VIEW_TYPE_OUTGOING = 1;

    private List<Message> messages;

    public MessageAdapter(List<Message> messages) {
        this.messages = messages;
    }

    // Updates list and refreshes view
    public void updateMessages(List<Message> newMessages) {
        this.messages = newMessages;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return messages != null ? messages.size() : 0;
    }

    // Return the view type based on whether the message is outgoing
    @Override
    public int getItemViewType(int position) {
        return messages.get(position).isOutgoing() ? VIEW_TYPE_OUTGOING : VIEW_TYPE_INCOMING;
    }

    // Inflate the correct layout based on message type
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_OUTGOING) {
            View view = inflater.inflate(R.layout.chat_bubble_outgoing, parent, false);
            return new OutgoingViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.chat_bubble_incoming, parent, false);
            return new IncomingViewHolder(view);
        }
    }

    // Bind the message to the correct view holder
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);

        if (holder instanceof OutgoingViewHolder) {
            ((OutgoingViewHolder) holder).bind(message);
        } else if (holder instanceof IncomingViewHolder) {
            ((IncomingViewHolder) holder).bind(message);
        }
    }

    // ViewHolder for outgoing messages
    static class OutgoingViewHolder extends RecyclerView.ViewHolder {
        private final TextView messageText;
        private final TextView timestampText;

        public OutgoingViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.outgoingMessageText);
            timestampText = itemView.findViewById(R.id.outgoingTimestamp);
        }

        public void bind(Message message) {
            messageText.setText(message.getContent());

            String formattedTime = new SimpleDateFormat("h:mm a", Locale.getDefault())
                    .format(new Date(message.getTimestamp()));
            timestampText.setText(formattedTime);
        }
    }

    // ViewHolder for incoming messages
    static class IncomingViewHolder extends RecyclerView.ViewHolder {
        private final TextView messageText;
        private final TextView timestampText;

        public IncomingViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.incomingMessageText);
            timestampText = itemView.findViewById(R.id.incomingTimestamp);
        }

        public void bind(Message message) {
            messageText.setText(message.getContent());

            String formattedTime = new SimpleDateFormat("h:mm a", Locale.getDefault())
                    .format(new Date(message.getTimestamp()));
            timestampText.setText(formattedTime);
        }
    }
}
