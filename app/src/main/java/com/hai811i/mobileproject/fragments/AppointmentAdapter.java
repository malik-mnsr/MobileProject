package com.hai811i.mobileproject.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hai811i.mobileproject.R;
import com.hai811i.mobileproject.entity.Appointment;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class AppointmentAdapter
        extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    private final List<Appointment> appointments;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Appointment appointment);
    }

    public AppointmentAdapter(List<Appointment> appointments, OnItemClickListener listener) {
        this.appointments = appointments;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_appointment, parent, false);
        return new AppointmentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment appt = appointments.get(position);
        // Format timestamp en date lisible
        String dateStr = DateFormat.getDateTimeInstance().format(new Date(appt.getTimestamp()));
        holder.tvTime.setText(dateStr);
        // TODO : charger le nom du patient à partir de son ID
        holder.tvPatient.setText(appt.getPatientId());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(appt));
    }

    @Override public int getItemCount() {
        return appointments.size();
    }

    static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView tvPatient, tvTime;
        AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPatient = itemView.findViewById(R.id.tvPatientName);
            tvTime    = itemView.findViewById(R.id.tvAppointmentTime);
        }
    }
}