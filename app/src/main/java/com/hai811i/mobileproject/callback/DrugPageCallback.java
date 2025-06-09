package com.hai811i.mobileproject.callback;


import com.hai811i.mobileproject.entity.DrugReference;
import com.hai811i.mobileproject.entity.Page;

public interface DrugPageCallback {
    void onSuccess(Page<DrugReference> drugPage);
    void onFailure(String errorMessage);
}