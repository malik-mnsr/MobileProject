package com.hai811i.mobileproject.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hai811i.mobileproject.dto.DoctorRequestWithBase64;
import com.hai811i.mobileproject.entity.Doctor;
import com.hai811i.mobileproject.repository.DoctorCallback;
import com.hai811i.mobileproject.repository.DoctorRepository;
import com.hai811i.mobileproject.repository.DoctorsListCallback;
import com.hai811i.mobileproject.repository.LoginCallback;
import com.hai811i.mobileproject.repository.ProfilePictureCallback;
import com.hai811i.mobileproject.repository.VoidCallback;
import com.hai811i.mobileproject.response.LoginResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ProjectViewModel extends ViewModel {
    private final DoctorRepository doctorRepository;


    // LiveData for login
    private final MutableLiveData<LoginResponse> loginResponse = new MutableLiveData<>();
    private final MutableLiveData<String> loginError = new MutableLiveData<>();

    // LiveData for doctor operations
    private final MutableLiveData<Doctor> doctor = new MutableLiveData<>();
    private final MutableLiveData<String> doctorError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    // LiveData for doctors list
    private final MutableLiveData<List<Doctor>> doctorsList = new MutableLiveData<>();
    private final MutableLiveData<String> doctorsListError = new MutableLiveData<>();

    // LiveData for profile picture
    private final MutableLiveData<byte[]> profilePicture = new MutableLiveData<>();
    private final MutableLiveData<String> profilePictureError = new MutableLiveData<>();

    // LiveData for operation completion
    private final MutableLiveData<Boolean> operationCompleted = new MutableLiveData<>();
    private final MutableLiveData<String> operationError = new MutableLiveData<>();

    public ProjectViewModel(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    // Login
    public void loginDoctor(String email, String phone) {
        doctorRepository.loginDoctor(email, phone, new LoginCallback() {
            @Override
            public void onSuccess(LoginResponse response) {
                loginResponse.postValue(response);
            }

            @Override
            public void onFailure(String errorMessage) {
                loginError.postValue(errorMessage);
            }
        });
    }

    public LiveData<LoginResponse> getLoginResponse() {
        return loginResponse;
    }

    public LiveData<String> getLoginError() {
        return loginError;
    }

    // Doctor CRUD operations
    public void createDoctor(Doctor doctor) {
        doctorRepository.createDoctor(doctor, new DoctorCallback() {
            @Override
            public void onSuccess(Doctor createdDoctor) {
                ProjectViewModel.this.doctor.postValue(createdDoctor);
                operationCompleted.postValue(true);
            }

            @Override
            public void onFailure(String errorMessage) {
                doctorError.postValue(errorMessage);
                operationError.postValue(errorMessage);
            }
        });
    }

    public void getDoctor(int id) {
        doctorRepository.getDoctor(id, new DoctorCallback() {
            @Override
            public void onSuccess(Doctor doctor) {
                ProjectViewModel.this.doctor.postValue(doctor);
            }

            @Override
            public void onFailure(String errorMessage) {
                doctorError.postValue(errorMessage);
            }
        });
    }

    public LiveData<Doctor> getDoctor() {
        return doctor;
    }

    public LiveData<String> getDoctorError() {
        return doctorError;
    }

    public void getAllDoctors() {
        doctorRepository.getAllDoctors(new DoctorsListCallback() {
            @Override
            public void onSuccess(List<Doctor> doctors) {
                doctorsList.postValue(doctors);
            }

            @Override
            public void onFailure(String errorMessage) {
                doctorsListError.postValue(errorMessage);
            }
        });
    }

    public void getDoctorsBySpecialty(String specialty) {
        doctorRepository.getDoctorsBySpecialty(specialty, new DoctorsListCallback() {
            @Override
            public void onSuccess(List<Doctor> doctors) {
                doctorsList.postValue(doctors);
            }

            @Override
            public void onFailure(String errorMessage) {
                doctorsListError.postValue(errorMessage);
            }
        });
    }

    public LiveData<List<Doctor>> getDoctorsList() {
        return doctorsList;
    }

    public LiveData<String> getDoctorsListError() {
        return doctorsListError;
    }

    public void updateDoctor(int id, Doctor doctor) {
        doctorRepository.updateDoctor(id, doctor, new DoctorCallback() {
            @Override
            public void onSuccess(Doctor updatedDoctor) {
                ProjectViewModel.this.doctor.postValue(updatedDoctor);
                operationCompleted.postValue(true);
            }

            @Override
            public void onFailure(String errorMessage) {
                doctorError.postValue(errorMessage);
                operationError.postValue(errorMessage);
            }
        });
    }

    public void deleteDoctor(int id) {
        doctorRepository.deleteDoctor(id, new VoidCallback() {
            @Override
            public void onSuccess() {
                operationCompleted.postValue(true);
            }

            @Override
            public void onFailure(String errorMessage) {
                operationError.postValue(errorMessage);
            }
        });
    }

    // Profile picture operations
    public void uploadProfilePicture(int id, MultipartBody.Part file) {
        doctorRepository.uploadProfilePicture(id, file, new VoidCallback() {
            @Override
            public void onSuccess() {
                operationCompleted.postValue(true);
            }

            @Override
            public void onFailure(String errorMessage) {
                operationError.postValue(errorMessage);
            }
        });
    }

    public void getProfilePicture(int id) {
        doctorRepository.getProfilePicture(id, new ProfilePictureCallback() {
            @Override
            public void onSuccess(byte[] imageBytes) {
                profilePicture.postValue(imageBytes);
            }

            @Override
            public void onFailure(String errorMessage) {
                profilePictureError.postValue(errorMessage);
            }
        });
    }

    public LiveData<byte[]> getProfilePicture() {
        return profilePicture;
    }

    public LiveData<String> getProfilePictureError() {
        return profilePictureError;
    }

    // Create doctor with picture
    public void createDoctorWithPicture(RequestBody doctorRequest, MultipartBody.Part picture) {
        doctorRepository.createDoctorWithPicture(doctorRequest, picture, new DoctorCallback() {
            @Override
            public void onSuccess(Doctor doctor) {
                ProjectViewModel.this.doctor.postValue(doctor);
                operationCompleted.postValue(true);
            }

            @Override
            public void onFailure(String errorMessage) {
                doctorError.postValue(errorMessage);
                operationError.postValue(errorMessage);
            }
        });
    }
    public void createDoctorWithPictureBase64(DoctorRequestWithBase64 request) {
        isLoading.setValue(true);

        doctorRepository.createDoctorWithPictureBase64(request, new DoctorCallback() {
            @Override
            public void onSuccess(Doctor createdDoctor) {
                isLoading.postValue(false);
                doctor.postValue(createdDoctor);
                operationCompleted.postValue(true);
            }

            @Override
            public void onFailure(String errorMessage) {
                isLoading.postValue(false);
                doctorError.postValue(errorMessage);
                operationError.postValue(errorMessage);
            }
        });
    }

    // Loading state
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    // Operation status
    public LiveData<Boolean> getOperationCompleted() {
        return operationCompleted;
    }

    public LiveData<String> getOperationError() {
        return operationError;
    }

    // Reset LiveData
    public void resetOperationStatus() {
        operationCompleted.setValue(false);
        operationError.setValue(null);
    }

    public void resetLoginStatus() {
        loginResponse.setValue(null);
        loginError.setValue(null);
    }
}