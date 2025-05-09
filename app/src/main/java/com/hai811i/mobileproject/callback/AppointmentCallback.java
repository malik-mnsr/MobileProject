package com.hai811i.mobileproject.callback;

import com.hai811i.mobileproject.dto.AppointmentDTO;

public interface AppointmentCallback {
    void onSuccess(AppointmentDTO appointment);
    void onFailure(String errorMessage);
}