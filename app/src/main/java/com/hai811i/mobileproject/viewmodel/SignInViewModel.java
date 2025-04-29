package com.hai811i.mobileproject.viewmodel;

// SignInViewModel.java
import androidx.lifecycle.ViewModel;

import com.hai811i.mobileproject.entity.Doctor;
import com.hai811i.mobileproject.repository.DoctorRepository;
import com.hai811i.mobileproject.repository.LoginCallback;
public class SignInViewModel extends ViewModel {
    private final DoctorRepository doctorRepository;

    public SignInViewModel(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public void loginDoctor(String email, String phone, LoginCallback callback) {
        doctorRepository.loginDoctor(email, phone, callback);
    }
}
