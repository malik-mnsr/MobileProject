package com.hai811i.mobileproject.repository;


import com.hai811i.mobileproject.callback.MedicalRecordCallback;
import com.hai811i.mobileproject.callback.MedicalRecordsListCallback;
import com.hai811i.mobileproject.callback.VoidCallback;
import com.hai811i.mobileproject.dto.MedicalRecordDTO;

public interface MedicalRecordRepository {
    void createMedicalRecord(Long appointmentId, MedicalRecordDTO dto, MedicalRecordCallback callback);
    void getMedicalRecordByAppointment(Long appointmentId, MedicalRecordCallback callback);
    void updateMedicalRecord(Integer recordId, MedicalRecordDTO dto, MedicalRecordCallback callback);
    void getMedicalRecordsHistory(Long patientId, MedicalRecordsListCallback callback);
    void getMedicalRecordById(Integer recordId, MedicalRecordCallback callback);
    void deleteMedicalRecord(Integer recordId, VoidCallback callback);
}