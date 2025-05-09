package com.hai811i.mobileproject.implementation;


import com.hai811i.mobileproject.api.ApiService;
import com.hai811i.mobileproject.callback.PatientCallback;
import com.hai811i.mobileproject.callback.PatientsListCallback;
import com.hai811i.mobileproject.dto.PatientDTO;
import com.hai811i.mobileproject.entity.Patient;

import com.hai811i.mobileproject.repository.PatientRepository;

import com.hai811i.mobileproject.callback.ProfilePictureCallback;
import com.hai811i.mobileproject.callback.VoidCallback;

import java.io.IOException;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientRepositoryImpl implements PatientRepository {
    private final ApiService apiService;

    public PatientRepositoryImpl(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void createPatient(Long doctorId, Patient patient, PatientCallback callback) {
        apiService.createPatient(doctorId, patient).enqueue(new Callback<PatientDTO>() {
            @Override
            public void onResponse(Call<PatientDTO> call, Response<PatientDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Failed to create patient: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<PatientDTO> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public void getPatient(Long id, PatientCallback callback) {
        apiService.getPatient(id).enqueue(new Callback<PatientDTO>() {
            @Override
            public void onResponse(Call<PatientDTO> call, Response<PatientDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Failed to get patient: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<PatientDTO> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public void getAllPatients(PatientsListCallback callback) {
        apiService.getAllPatients().enqueue(new Callback<List<PatientDTO>>() {
            @Override
            public void onResponse(Call<List<PatientDTO>> call, Response<List<PatientDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Failed to get patients: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<PatientDTO>> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public void getPatientsByDoctor(Long doctorId, PatientsListCallback callback) {
        apiService.getPatientsByDoctor(doctorId).enqueue(new Callback<List<PatientDTO>>() {
            @Override
            public void onResponse(Call<List<PatientDTO>> call, Response<List<PatientDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Failed to get patients by doctor: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<PatientDTO>> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public void updatePatient(Long id, Patient patient, PatientCallback callback) {
        apiService.updatePatient(id, patient).enqueue(new Callback<PatientDTO>() {
            @Override
            public void onResponse(Call<PatientDTO> call, Response<PatientDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Failed to update patient: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<PatientDTO> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public void deletePatient(Long id, VoidCallback callback) {
        apiService.deletePatient(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onFailure("Failed to delete patient: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public void uploadProfilePicture(Long id, MultipartBody.Part file, VoidCallback callback) {
        apiService.uploadPatientProfilePicture(id, file).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onFailure("Failed to upload profile picture: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public void getProfilePicture(Long id, ProfilePictureCallback callback) {
        apiService.getPatientProfilePicture(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        byte[] bytes = response.body().bytes();
                        callback.onSuccess(bytes);
                    } catch (IOException e) {
                        callback.onFailure("Failed to read profile picture: " + e.getMessage());
                    }
                } else {
                    callback.onFailure("Failed to get profile picture: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }
}