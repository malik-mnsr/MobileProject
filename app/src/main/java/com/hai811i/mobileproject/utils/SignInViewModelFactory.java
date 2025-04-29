package com.hai811i.mobileproject.utils;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.hai811i.mobileproject.repository.DoctorRepository;
import com.hai811i.mobileproject.viewmodel.SignInViewModel;

public class SignInViewModelFactory implements ViewModelProvider.Factory {
    private final DoctorRepository userRepository;

    public SignInViewModelFactory(DoctorRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SignInViewModel.class)) {
            return (T) new SignInViewModel(userRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
