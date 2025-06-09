package com.hai811i.mobileproject.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hai811i.mobileproject.R;
import com.hai811i.mobileproject.dto.PatientDTO;
import com.hai811i.mobileproject.entity.Patient;

import java.util.List;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientViewHolder> {

    public interface OnPatientClickListener {
        void onPatientClick(PatientDTO patient);
    }

    public interface OnAppointmentClickListener {
        void onAppointmentClick(PatientDTO patient);
    }

    private List<PatientDTO> patientList;
    private OnPatientClickListener patientClickListener;
    private OnAppointmentClickListener appointmentClickListener;

    public PatientAdapter(List<PatientDTO> patientList,
                          OnPatientClickListener patientClickListener,
                          OnAppointmentClickListener appointmentClickListener) {
        this.patientList = patientList;
        this.patientClickListener = patientClickListener;
        this.appointmentClickListener = appointmentClickListener;
    }

    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_patient1, parent, false);
        return new PatientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {
        PatientDTO patient = patientList.get(position);
        holder.bind(patient, patientClickListener, appointmentClickListener); // <-- corrige ici
    }


    @Override
    public int getItemCount() {
        return patientList.size();
    }

    static class PatientViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPicture;
        private TextView tvName, tvAge, tvAddress, tvAntecedents;

        private Button btnAppointment; // à ajouter dans ViewHolder

        PatientViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPicture = itemView.findViewById(R.id.iv_patient_picture);
            tvName = itemView.findViewById(R.id.tv_patient_name);
            tvAge = itemView.findViewById(R.id.tv_patient_age);
            tvAddress = itemView.findViewById(R.id.tv_patient_address);
            btnAppointment = itemView.findViewById(R.id.btn_show_appointments); // <-- bouton "Rendez-vous"
        }

        void bind(PatientDTO patient, OnPatientClickListener patientClickListener, OnAppointmentClickListener appointmentClickListener) {
            tvName.setText(patient.getFirstName() + " " + patient.getLastName());
            tvAge.setText("Âge : " + patient.getAge() + " ans");
            tvAddress.setText("Adresse : " + patient.getAddress());


            ivPicture.setImageResource(R.drawable.ic_default_profile);


            itemView.setOnClickListener(v -> patientClickListener.onPatientClick(patient));
            btnAppointment.setOnClickListener(v -> appointmentClickListener.onAppointmentClick(patient));
        }

    }
}