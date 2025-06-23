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

        holder.infoText.setText(String.format("%s | %s", participant.getCallsign(), participant.getIp()));

        setupMicButton(holder, participant, position);
        setupVideoButton(holder, participant, position);
        setupConnectionStatus(holder, participant);
        enforceSquareLayout(holder);
    }

    @Override
    public int getItemCount() {
        return participants.size();
    }

    private void setupMicButton(@NonNull ParticipantViewHolder holder, Participant participant, int position) {
        updateMicButtonUI(holder, participant);

        holder.micButton.setOnClickListener(v -> {
            participant.setMuted(!participant.isMuted());
            notifyItemChanged(position);
            android.util.Log.d("ParticipantAdapter", "Mic toggled for: " + participant.getCallsign());
        });
    }

    private void updateMicButtonUI(@NonNull ParticipantViewHolder holder, Participant participant) {
        holder.micButton.setVisibility(View.VISIBLE);
        holder.micButton.setImageResource(
                participant.isMuted() ? R.drawable.ic_mic_off : R.drawable.ic_mic_on
        );
        holder.micButton.setColorFilter(
                context.getResources().getColor(
                        participant.isMuted() ? R.color.status_red : R.color.accent
                )
        );
    }

    private void setupVideoButton(@NonNull ParticipantViewHolder holder, Participant participant, int position) {
        updateVideoButtonUI(holder, participant);

        holder.videoButton.setOnClickListener(v -> {
            participant.setVideoOn(!participant.isVideoOn());
            notifyItemChanged(position);
            android.util.Log.d("ParticipantAdapter", "Video toggled for: " + participant.getCallsign());
        });
    }

    private void updateVideoButtonUI(@NonNull ParticipantViewHolder holder, Participant participant) {
        holder.videoButton.setVisibility(View.VISIBLE);
        holder.videoButton.setImageResource(
                participant.isVideoOn() ? R.drawable.ic_camera_on : R.drawable.ic_camera_off
        );
        holder.videoButton.setColorFilter(
                context.getResources().getColor(
                        participant.isVideoOn() ? R.color.accent : R.color.status_red
                )
        );
    }

    private void setupConnectionStatus(@NonNull ParticipantViewHolder holder, Participant participant) {
        holder.connectionStatus.setBackgroundResource(
                participant.isConnected() ? R.drawable.bg_status_circle_green : R.drawable.bg_status_circle_red
        );
    }

    private void enforceSquareLayout(@NonNull ParticipantViewHolder holder) {
        holder.itemView.post(() -> {
            int width = holder.itemView.getWidth();
            if (width > 0) {
                ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
                params.height = width;
                holder.itemView.setLayoutParams(params);
            }
        });
    }

    static class ParticipantViewHolder extends RecyclerView.ViewHolder {
        ImageView videoImage;
        TextView infoText;
        ImageButton micButton, videoButton;
        View connectionStatus;

        ParticipantViewHolder(@NonNull View itemView) {
            super(itemView);
            videoImage = itemView.findViewById(R.id.img_video);
            infoText = itemView.findViewById(R.id.text_info);
            micButton = itemView.findViewById(R.id.btn_mic);
            videoButton = itemView.findViewById(R.id.btn_video);
            connectionStatus = itemView.findViewById(R.id.connection_status);
        }
    }
}
