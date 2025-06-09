package com.hai811i.mobileproject.utils;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hai811i.mobileproject.R;
import com.hai811i.mobileproject.dto.AppointmentDTO;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hai811i.mobileproject.R;
import com.hai811i.mobileproject.dto.AppointmentDTO;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    private final List<AppointmentDTO> appointmentList;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private final OnAppointmentClickListener listener;

    public interface OnAppointmentClickListener {
        void onAppointmentClick(AppointmentDTO appointment);
    }

    public AppointmentAdapter(List<AppointmentDTO> appointmentList, OnAppointmentClickListener listener) {
        this.appointmentList = appointmentList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_appointment, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        AppointmentDTO appointment = appointmentList.get(position);
        holder.bind(appointment, listener);
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvDate;
        private final TextView tvTime;
        private final TextView tvMotif;
        private final TextView tvStatus;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvMotif = itemView.findViewById(R.id.tv_motif);
            tvStatus = itemView.findViewById(R.id.tv_status);
        }

        void bind(AppointmentDTO appointment, OnAppointmentClickListener listener) {
            // Format date and time
            String startAt = String.valueOf(appointment.getSlot().getStartAt());

            try {
                // Parse the date string
                DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss", Locale.getDefault());
                Date date = inputFormat.parse(startAt);

                // Set formatted date
                tvDate.setText(dateFormat.format(date));

                // Set formatted time
                if (appointment.getSlot().getEndAt() != null) {
                    Date endDate = inputFormat.parse(String.valueOf(appointment.getSlot().getEndAt()));
                    String timeRange = timeFormat.format(date) + " - " + timeFormat.format(endDate);
                    tvTime.setText(timeRange);
                } else {
                    tvTime.setText(timeFormat.format(date));
                }
            } catch (ParseException e) {
                e.printStackTrace();
                // Safer fallback if parsing fails
                tvDate.setText(startAt); // Show full date string
                tvTime.setText(""); // Empty time
            }

            // Set motif and status
            tvMotif.setText(appointment.getMotif() != null ? appointment.getMotif() : "");
            tvStatus.setText(appointment.getStatus() != null ? appointment.getStatus() : "");

            // Set status color
            if (appointment.getStatus() != null) {
                switch (appointment.getStatus().toLowerCase()) {
                    case "confirmed":
                        tvStatus.setTextColor(itemView.getContext().getColor(R.color.NeonGreen));
                        break;
                    case "cancelled":
                        tvStatus.setTextColor(itemView.getContext().getColor(R.color.red));
                        break;
                    case "pending":
                        tvStatus.setTextColor(itemView.getContext().getColor(R.color.black));
                        break;
                    default:
                        tvStatus.setTextColor(itemView.getContext().getColor(R.color.gray));
                }
            }

            // Set click listener
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAppointmentClick(appointment);
                }
            });
        }
    }
}