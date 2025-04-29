package com.hai811i.mobileproject.repository;

import com.hai811i.mobileproject.entity.Doctor;

public interface DoctorCallback {
    void onSuccess(Doctor doctor);
    void onFailure(String errorMessage);
}
