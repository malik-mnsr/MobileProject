package com.hai811i.mobileproject.callback;

import com.hai811i.mobileproject.dto.PrescriptionDTO;

public interface PrescriptionCallback {
    void onSuccess(PrescriptionDTO prescription);
    void onFailure(String error);
}