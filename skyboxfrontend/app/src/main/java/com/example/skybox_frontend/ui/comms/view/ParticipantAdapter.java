package com.example.skybox_frontend.ui.comms.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skybox_frontend.R;
import com.example.skybox_frontend.ui.comms.model.Participant;

import java.util.List;

public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.ParticipantViewHolder> {

    private final Context context;
    private final List<Participant> participants;

    public ParticipantAdapter(Context context, List<Participant> participants) {
        this.context = context;
        this.participants = participants;
    }

    @NonNull
    @Override
    public ParticipantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_participant, parent, false);
        return new ParticipantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantViewHolder holder, int position) {
        Participant participant = participants.get(position);

        holder.infoText.setText("Callsign: " + participant.getCallsign() + " | IP: " + participant.getIp());
        holder.micButton.setVisibility(participant.isMuted() ? View.VISIBLE : View.GONE);
        holder.videoButton.setVisibility(participant.isVideoOn() ? View.GONE : View.VISIBLE);

        // Enforce square — safer method using itemView width
        holder.itemView.post(() -> {
            int width = holder.itemView.getWidth();
            if (width > 0) {
                ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
                params.height = width;
                holder.itemView.setLayoutParams(params);
            }
        });
    }

    @Override
    public int getItemCount() {
        return participants.size();
    }

    static class ParticipantViewHolder extends RecyclerView.ViewHolder {
        ImageView videoImage;
        TextView infoText;
        ImageButton micButton, videoButton;

        ParticipantViewHolder(@NonNull View itemView) {
            super(itemView);
            videoImage = itemView.findViewById(R.id.img_video);
            infoText = itemView.findViewById(R.id.text_info);
            micButton = itemView.findViewById(R.id.btn_mic);
            videoButton = itemView.findViewById(R.id.btn_video);
        }
    }
}
