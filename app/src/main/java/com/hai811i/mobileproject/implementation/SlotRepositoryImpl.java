package com.hai811i.mobileproject.implementation;

import androidx.annotation.NonNull;

import com.hai811i.mobileproject.api.ApiService;
import com.hai811i.mobileproject.callback.SlotAppointmentsCallback;
import com.hai811i.mobileproject.callback.SlotsListCallback;
import com.hai811i.mobileproject.dto.SlotCreateDTO;
import com.hai811i.mobileproject.dto.SlotDTO;
import com.hai811i.mobileproject.repository.SlotRepository;


import java.time.LocalDate;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SlotRepositoryImpl implements SlotRepository {
    private final ApiService apiService;

    public SlotRepositoryImpl(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void createSlots(long doctorId, List<SlotCreateDTO> slots, SlotAppointmentsCallback callback) {
        apiService.createSlots(doctorId, slots).enqueue(new Callback<List<SlotDTO>>() {
            @Override
            public void onResponse(@NonNull Call<List<SlotDTO>> call, @NonNull Response<List<SlotDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<SlotDTO>> call, @NonNull Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    @Override
    public void getFreeSlots(long doctorId, LocalDate date, SlotAppointmentsCallback callback) {
        apiService.getFreeSlots(doctorId, String.valueOf(date)).enqueue(new Callback<List<SlotDTO>>() {
            @Override
            public void onResponse(@NonNull Call<List<SlotDTO>> call, @NonNull Response<List<SlotDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<SlotDTO>> call, @NonNull Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }
}
