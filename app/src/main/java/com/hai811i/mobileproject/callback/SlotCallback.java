package com.hai811i.mobileproject.callback;

import com.hai811i.mobileproject.dto.SlotDTO;

public interface SlotCallback {
    void onSuccess(SlotDTO slot);
    void onFailure(String errorMessage);
}