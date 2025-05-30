package com.hai811i.mobileproject.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hai811i.mobileproject.R;
import com.hai811i.mobileproject.entity.GoogleEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;

public class GoogleEventAdapter extends RecyclerView.Adapter<GoogleEventAdapter.ViewHolder> {
    private List<GoogleEvent> events;
    private Consumer<GoogleEvent> onEventClicked;

    public GoogleEventAdapter(List<GoogleEvent> events, Consumer<GoogleEvent> onEventClicked) {
        this.events = events;
        this.onEventClicked = onEventClicked;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_google_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GoogleEvent event = events.get(position);
        holder.bind(event);
        holder.itemView.setOnClickListener(v -> onEventClicked.accept(event));
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvSummary, tvTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSummary = itemView.findViewById(R.id.tv_summary);
            tvTime = itemView.findViewById(R.id.tv_time);
        }

        public void bind(GoogleEvent event) {
            tvSummary.setText(event.getSummary());
            tvTime.setText(formatTimeRange(event.getStartTime(), event.getEndTime()));
        }

        private String formatTimeRange(LocalDateTime start, LocalDateTime end) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, h:mm a");
            return start.format(formatter) + " - " + end.format(formatter);
        }
    }
}