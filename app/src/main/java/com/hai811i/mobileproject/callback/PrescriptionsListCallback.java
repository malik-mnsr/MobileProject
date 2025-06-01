package com.hai811i.mobileproject.callback;

import com.hai811i.mobileproject.dto.PrescriptionDTO;

import java.util.List;

public interface PrescriptionsListCallback {
    void onSuccess(List<PrescriptionDTO> prescriptions);
    void onFailure(String error);
}