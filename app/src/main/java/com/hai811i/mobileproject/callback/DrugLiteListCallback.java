package com.hai811i.mobileproject.callback;

import java.util.List;

public interface DrugLiteListCallback {
    void onSuccess(List<com.example.mobileproject.dto.DrugLiteDTO> drugs);
    void onFailure(String errorMessage);
}