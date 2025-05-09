package com.hai811i.mobileproject.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hai811i.mobileproject.R;
import com.hai811i.mobileproject.entity.Patient;
import java.util.List;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientViewHolder> {

    public interface OnPatientClickListener {
        void onPatientClick(Patient patient);
    }

    private List<Patient> patientList;
    private OnPatientClickListener listener;

    public PatientAdapter(List<Patient> patientList, OnPatientClickListener listener) {
        this.patientList = patientList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_patient, parent, false);
        return new PatientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {
        Patient patient = patientList.get(position);
        holder.bind(patient);
        holder.itemView.setOnClickListener(v -> listener.onPatientClick(patient));
    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }

    static class PatientViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPicture;
        private TextView tvName, tvAge, tvAddress, tvAntecedents;

        PatientViewHolder(@NonNull View itemView) {
            super(itemView);
      //      ivPicture = itemView.findViewById(R.id.iv_patient_picture);
        //    tvName = itemView.findViewById(R.id.tv_patient_name);
          //  tvAge = itemView.findViewById(R.id.tv_patient_age);
           // tvAddress = itemView.findViewById(R.id.tv_patient_address);
          //  tvAntecedents = itemView.findViewById(R.id.tv_patient_antecedents);
        }

        void bind(Patient patient) {

            tvName.setText(patient.getFirstName() + " " + patient.getLastName());
            tvAge.setText("Âge: " + patient.getAge());
            tvAddress.setText("Adresse: " + patient.getAddress());
            tvAntecedents.setText("Antécédents: " + patient.getAntecedents());

            // Decode and set profile picture
            if (patient.getProfilePicture() != null && !patient.getProfilePicture().isEmpty()) {
                byte[] decodedBytes = Base64.decode(patient.getProfilePicture(), Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                ivPicture.setImageBitmap(bitmap);
            } else {
                ivPicture.setImageResource(R.drawable.ic_default_profile); // fallback image
            }





            // Nom complet
            tvName.setText(patient.getFirstName() + " " + patient.getLastName());

            // Âge
            tvAge.setText("Âge : " + patient.getAge() + " ans");

            // Adresse
            tvAddress.setText("Adresse : " + patient.getAddress());

            // Antécédents
            if (patient.getAntecedents() != null && !patient.getAntecedents().isEmpty()) {
                tvAntecedents.setText("Antécédents : " + String.join(", ", patient.getAntecedents()));
            } else {
                tvAntecedents.setText("Antécédents : Aucun");
            }
        }
    }
}
