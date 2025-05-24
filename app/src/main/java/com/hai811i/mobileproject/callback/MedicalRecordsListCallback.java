package com.hai811i.mobileproject.callback;

import com.hai811i.mobileproject.dto.MedicalRecordDTO;

import java.util.List;

public interface MedicalRecordsListCallback {
    void onSuccess(List<MedicalRecordDTO> medicalRecords);
    void onFailure(String message);
}
