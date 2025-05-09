package com.hai811i.mobileproject.callback;

import com.hai811i.mobileproject.dto.PatientDTO;

import java.util.List;

public interface PatientsListCallback {
    void onSuccess(List<PatientDTO> patients);
    void onFailure(String errorMessage);
}