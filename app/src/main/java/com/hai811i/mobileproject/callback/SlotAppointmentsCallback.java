package com.hai811i.mobileproject.callback;


import com.hai811i.mobileproject.dto.SlotDTO;
import com.hai811i.mobileproject.entity.AppointmentSlot;

import java.util.List;

// For a list of slots
public interface SlotAppointmentsCallback {
    void onSuccess(List<SlotDTO> slots);
    void onFailure(String errorMessage);
}
