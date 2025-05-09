package com.hai811i.mobileproject.callback;

import com.hai811i.mobileproject.entity.AppointmentSlot;

public interface SlotAppointmentCallback {
    void onSuccess(AppointmentSlot slot);
    void onFailure(String errorMessage);
}
