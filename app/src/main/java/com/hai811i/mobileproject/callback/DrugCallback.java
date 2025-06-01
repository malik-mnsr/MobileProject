package com.hai811i.mobileproject.callback;

import com.hai811i.mobileproject.entity.DrugReference;
import com.hai811i.mobileproject.entity.Page;

import java.util.List;

public interface DrugCallback {
    void onSuccess(DrugReference drug);
    void onFailure(String errorMessage);
}