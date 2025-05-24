package com.hai811i.mobileproject.implementation;

import com.hai811i.mobileproject.api.ApiService;
import com.hai811i.mobileproject.callback.MedicalRecordCallback;
import com.hai811i.mobileproject.callback.MedicalRecordsListCallback;
import com.hai811i.mobileproject.callback.VoidCallback;
import com.hai811i.mobileproject.dto.MedicalRecordDTO;
import com.hai811i.mobileproject.repository.MedicalRecordRepository;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MedicalRecordRepositoryImpl implements MedicalRecordRepository {
    private final ApiService apiService;

    public MedicalRecordRepositoryImpl(ApiService apiService) {
        this.apiService = apiService;
    }
    @Override
    public void createMedicalRecord(Long appointmentId, MedicalRecordDTO dto, MedicalRecordCallback callback) {
        apiService.createMedicalRecord(appointmentId, dto).enqueue(new Callback<MedicalRecordDTO>() {
            @Override
            public void onResponse(Call<MedicalRecordDTO> call, Response<MedicalRecordDTO> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Failed to create medical record: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<MedicalRecordDTO> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public void getMedicalRecordByAppointment(Long appointmentId, MedicalRecordCallback callback) {
        apiService.getMedicalRecordByAppointment(appointmentId).enqueue(new Callback<MedicalRecordDTO>() {
            @Override
            public void onResponse(Call<MedicalRecordDTO> call, Response<MedicalRecordDTO> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Failed to get medical record: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<MedicalRecordDTO> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public void updateMedicalRecord(Integer recordId, MedicalRecordDTO dto, MedicalRecordCallback callback) {
        apiService.updateMedicalRecord(recordId, dto).enqueue(new Callback<MedicalRecordDTO>() {
            @Override
            public void onResponse(Call<MedicalRecordDTO> call, Response<MedicalRecordDTO> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Failed to update medical record: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<MedicalRecordDTO> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public void getMedicalRecordsHistory(Long patientId, MedicalRecordsListCallback callback) {
        apiService.getMedicalRecordsHistory(patientId).enqueue(new Callback<List<MedicalRecordDTO>>() {
            @Override
            public void onResponse(Call<List<MedicalRecordDTO>> call, Response<List<MedicalRecordDTO>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Failed to get medical records history: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<MedicalRecordDTO>> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public void getMedicalRecordById(Integer recordId, MedicalRecordCallback callback) {
        apiService.getMedicalRecordById(recordId).enqueue(new Callback<MedicalRecordDTO>() {
            @Override
            public void onResponse(Call<MedicalRecordDTO> call, Response<MedicalRecordDTO> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Failed to get medical record: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<MedicalRecordDTO> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public void deleteMedicalRecord(Integer recordId, VoidCallback callback) {
        apiService.deleteMedicalRecord(recordId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onFailure("Failed to delete medical record: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }
}