package com.hai811i.mobileproject.callback;

import com.hai811i.mobileproject.entity.Patient;

public interface RawPatientCallback {
    void onSuccess(Patient patient);
    void onFailure(String errorMessage);
}
