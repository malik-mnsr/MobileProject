package com.hai811i.mobileproject.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.hai811i.mobileproject.repository.DoctorRepository;
import com.hai811i.mobileproject.viewmodel.ProjectViewModel;

public class ProjectViewModelFactory implements ViewModelProvider.Factory {
    private final DoctorRepository repository;

    public ProjectViewModelFactory(DoctorRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ProjectViewModel.class)) {
            return (T) new ProjectViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
