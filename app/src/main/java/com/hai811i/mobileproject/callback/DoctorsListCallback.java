package com.hai811i.mobileproject.callback;


import com.hai811i.mobileproject.entity.Doctor;

import java.util.List;

public interface DoctorsListCallback {
    void onSuccess(List<Doctor> doctors);
    void onFailure(String errorMessage);
}