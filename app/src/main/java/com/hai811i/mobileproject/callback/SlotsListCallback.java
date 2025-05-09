package com.hai811i.mobileproject.callback;

import com.hai811i.mobileproject.dto.SlotDTO;

import java.util.List;

public interface SlotsListCallback {
    void onSuccess(List<SlotDTO> slots);
    void onFailure(String errorMessage);
}
