package com.hai811i.mobileproject.repository;

import com.hai811i.mobileproject.callback.PatientCallback;
import com.hai811i.mobileproject.callback.PatientsListCallback;
import com.hai811i.mobileproject.callback.ProfilePictureCallback;
import com.hai811i.mobileproject.callback.VoidCallback;
import com.hai811i.mobileproject.entity.Patient;

import okhttp3.MultipartBody;

public interface PatientRepository {
    void createPatient(Long doctorId, Patient patient, PatientCallback callback);
    void getPatient(Long id, PatientCallback callback);
    void getAllPatients(PatientsListCallback callback);
    void getPatientsByDoctor(Long doctorId, PatientsListCallback callback);
    void updatePatient(Long id, Patient patient, PatientCallback callback);
    void deletePatient(Long id, VoidCallback callback);
    void uploadProfilePicture(Long id, MultipartBody.Part file, VoidCallback callback);
    void getProfilePicture(Long id, ProfilePictureCallback callback);
}