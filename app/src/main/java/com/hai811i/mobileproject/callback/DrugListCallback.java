package com.hai811i.mobileproject.callback;

import com.hai811i.mobileproject.entity.DrugReference;

import java.util.List;

public interface DrugListCallback {
    void onSuccess(List<DrugReference> drugs);
    void onFailure(String errorMessage);
}