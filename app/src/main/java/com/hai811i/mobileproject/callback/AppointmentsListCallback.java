package com.hai811i.mobileproject.callback;

import com.hai811i.mobileproject.dto.AppointmentDTO;

import java.util.List;

public interface AppointmentsListCallback {
    void onSuccess(List<AppointmentDTO> appointments);
    void onFailure(String errorMessage);
}