package com.hai811i.mobileproject.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.hai811i.mobileproject.repository.AppointmentRepository;
import com.hai811i.mobileproject.repository.DoctorRepository;
import com.hai811i.mobileproject.repository.DrugRepository;
import com.hai811i.mobileproject.repository.GoogleCalendarRepository;
import com.hai811i.mobileproject.repository.MedicalRecordRepository;
import com.hai811i.mobileproject.repository.NotificationRepository;
import com.hai811i.mobileproject.repository.PatientRepository;
import com.hai811i.mobileproject.repository.PrescriptionsRepository;
import com.hai811i.mobileproject.repository.SlotRepository;
import com.hai811i.mobileproject.viewmodel.ProjectViewModel;

public class ProjectViewModelFactory implements ViewModelProvider.Factory {
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final SlotRepository slotRepository;
    private final AppointmentRepository appointmentRepository;
    private final GoogleCalendarRepository googleCalendarRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final PrescriptionsRepository prescriptionsRepository;
    private final NotificationRepository notificationRepository;
    private final DrugRepository drugRepository;
    public ProjectViewModelFactory(DoctorRepository doctorRepository,
                            PatientRepository patientRepository,
                            SlotRepository slotRepository,
                            AppointmentRepository appointmentRepository,
                            GoogleCalendarRepository googleCalendarRepository,
                            MedicalRecordRepository medicalRecordRepository,
                            PrescriptionsRepository prescriptionsRepository, NotificationRepository notificationRepository,
                                   DrugRepository drugRepository) {  // Add prescriptions repository
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.slotRepository = slotRepository;
        this.appointmentRepository = appointmentRepository;
        this.googleCalendarRepository = googleCalendarRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.prescriptionsRepository = prescriptionsRepository;  // Initialize prescriptions repository
        this.notificationRepository = notificationRepository;
        this.drugRepository=drugRepository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ProjectViewModel.class)) {
            return (T) new ProjectViewModel(
                    doctorRepository,
                    patientRepository,
                    slotRepository,
                    appointmentRepository,
                    googleCalendarRepository,
                    medicalRecordRepository,
                    prescriptionsRepository,
                    notificationRepository,drugRepository
            );
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}