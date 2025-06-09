package com.hai811i.mobileproject.repository;


import com.hai811i.mobileproject.callback.MedicalRecordCallback;
import com.hai811i.mobileproject.callback.MedicalRecordsListCallback;
import com.hai811i.mobileproject.callback.VoidCallback;
import com.hai811i.mobileproject.dto.MedicalRecordDTO;
import com.hai811i.mobileproject.entity.Motif;

public interface MedicalRecordRepository {
    void createMedicalRecordForAppointment(Long appointmentId, MedicalRecordDTO dto, MedicalRecordCallback callback);
    void getMedicalRecordByAppointment(Long appointmentId, MedicalRecordCallback callback);
    void updateMedicalRecord(Integer recordId, MedicalRecordDTO dto, MedicalRecordCallback callback);
    void getMedicalRecordById(Integer recordId, MedicalRecordCallback callback);
    void deleteMedicalRecord(Integer recordId, VoidCallback callback);

    void getPatientMedicalHistory(Long patientId, MedicalRecordsListCallback callback);
    void getFilteredMedicalRecords(Long doctorId, String date, Motif motif, MedicalRecordsListCallback callback);
    void getTodayMedicalRecordsByMotif(Long doctorId, Motif motif, MedicalRecordsListCallback callback);
    void getTodayMedicalRecords(Long doctorId, Motif motif, MedicalRecordsListCallback callback);
}