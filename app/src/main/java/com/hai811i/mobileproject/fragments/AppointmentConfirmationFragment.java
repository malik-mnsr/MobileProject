package com.hai811i.mobileproject.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.hai811i.mobileproject.R;
import com.hai811i.mobileproject.dto.AppointmentDTO;
import com.hai811i.mobileproject.dto.PatientDTO;
import com.hai811i.mobileproject.dto.ReserveRequest;
import com.hai811i.mobileproject.entity.Appointment;
import com.hai811i.mobileproject.entity.Motif;
import com.hai811i.mobileproject.viewmodel.ProjectViewModel;
public class AppointmentConfirmationFragment extends Fragment {

    private ProjectViewModel projectViewModel;
    private TextView tvDoctorName, tvDoctorSpecialty, tvAppointmentSlot, tvPatientName, tvPatientAge;
    private RadioGroup rgMotif;

    private Long doctorId;
    private Long slotId;
    private Long patientId;
    private String appointmentDate, appointmentTime;
    private String firstName, lastName;
    private int age;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        projectViewModel = new ViewModelProvider(requireActivity()).get(ProjectViewModel.class);

        if (getArguments() != null) {
            doctorId = getArguments().getLong("doctorId");
            slotId = getArguments().getLong("slotId");
            patientId = getArguments().getLong("patientId");
            appointmentDate = getArguments().getString("appointmentDate");
            appointmentTime = getArguments().getString("appointmentTime");
            firstName = getArguments().getString("firstName");
            lastName = getArguments().getString("lastName");
            age = getArguments().getInt("age");
        }
    }

    public static AppointmentConfirmationFragment newInstance(
            Long doctorId, Long slotId, Long patientId,
            String appointmentDate, String appointmentTime,
            String firstName, String lastName, int age) {

        AppointmentConfirmationFragment fragment = new AppointmentConfirmationFragment();
        Bundle args = new Bundle();
        args.putLong("doctorId", doctorId);
        args.putLong("slotId", slotId);
        args.putLong("patientId", patientId);
        args.putString("appointmentDate", appointmentDate);
        args.putString("appointmentTime", appointmentTime);
        args.putString("firstName", firstName);
        args.putString("lastName", lastName);
        args.putInt("age", age);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment_confirmation, container, false);
        initViews(view);
        setupViewModelObservers();
        return view;
    }

    private void initViews(View view) {
        tvDoctorName = view.findViewById(R.id.tv_doctor_name_confirmation);
        tvDoctorSpecialty = view.findViewById(R.id.tv_doctor_specialty_confirmation);
        tvAppointmentSlot = view.findViewById(R.id.tv_appointment_slot);
        tvPatientName = view.findViewById(R.id.tv_patient_name);
        tvPatientAge = view.findViewById(R.id.tv_patient_age);
        rgMotif = view.findViewById(R.id.rg_motif);

        // Set patient info
        tvPatientName.setText(String.format("%s %s", firstName, lastName));
        tvPatientAge.setText(String.format("Age: %d", age));

        // Set appointment time
        tvAppointmentSlot.setText(String.format("%s %s", appointmentDate, appointmentTime));

        // Confirm button
        view.findViewById(R.id.btn_confirm_appointment).setOnClickListener(v -> confirmAppointment());
    }

    private void confirmAppointment() {
        int selectedMotifId = rgMotif.getCheckedRadioButtonId();
        if (selectedMotifId == -1) {
            Toast.makeText(getContext(), "Please select an appointment type", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedRadioButton = getView().findViewById(selectedMotifId);
        String motif = selectedRadioButton.getText().toString().toUpperCase().replace(" ", "_");

        // Create ReserveRequest with patientId and motif
        ReserveRequest request = new ReserveRequest(patientId, motif);

        // Call ViewModel to reserve appointment
        projectViewModel.reserveAppointment(slotId, request);
        // Observe the appointment creation to trigger notification
        projectViewModel.getAppointment().observe(getViewLifecycleOwner(), appointmentDTO -> {
            if (appointmentDTO != null) {
                // Create a notification when appointment is successfully created
                sendDoctorNotification(appointmentDTO);
            }
        });
    }

    private void sendDoctorNotification(AppointmentDTO appointmentDTO) {
        // Convert AppointmentDTO to Appointment entity if needed
        // (You might need to create a mapper or extension function)
        Appointment appointment = convertToAppointmentEntity(appointmentDTO);

        projectViewModel.notifyDoctor(appointment);

        // Observe notification result
        projectViewModel.getNotificationSuccess().observe(getViewLifecycleOwner(), message -> {
            if (message != null) {
                Toast.makeText(getContext(), "Notification sent to doctor", Toast.LENGTH_SHORT).show();
                projectViewModel.resetNotificationStatus();
            }
        });

        projectViewModel.getNotificationError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(getContext(), "Notification failed: " + error, Toast.LENGTH_SHORT).show();
                projectViewModel.resetNotificationStatus();
            }
        });
    }

    // Helper method to convert DTO to Entity
    private Appointment convertToAppointmentEntity(AppointmentDTO dto) {
        // Implement conversion logic based on your entity structure
        Appointment appointment = new Appointment();
        appointment.setId(dto.getId());
        appointment.setMotif(String.valueOf(Motif.valueOf(dto.getMotif())));
        // Set other necessary fields...
        return appointment;
    }


    private void setupViewModelObservers() {
        projectViewModel.getDoctor().observe(getViewLifecycleOwner(), doctor -> {
            if (doctor != null) {
                tvDoctorName.setText(doctor.getFirstName());
                tvDoctorSpecialty.setText(doctor.getSpecialty());
            }
        });

        projectViewModel.getAppointment().observe(getViewLifecycleOwner(), appointmentDTO -> {
            if (appointmentDTO != null) {
                Toast.makeText(getContext(), "Appointment created successfully!", Toast.LENGTH_SHORT).show();
                requireActivity().onBackPressed();
            }
        });

        projectViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(getContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
