package com.hai811i.mobileproject.callback;

import com.hai811i.mobileproject.dto.PatientDTO;

public interface PatientCallback {
    void onSuccess(PatientDTO patient);
    void onFailure(String errorMessage);
}