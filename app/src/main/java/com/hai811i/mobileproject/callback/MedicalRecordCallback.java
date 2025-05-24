package com.hai811i.mobileproject.callback;

import com.hai811i.mobileproject.dto.MedicalRecordDTO;

// MedicalRecordCallback.java
public interface MedicalRecordCallback {
    void onSuccess(MedicalRecordDTO medicalRecord);
    void onFailure(String message);
}
