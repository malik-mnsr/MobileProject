package com.hai811i.mobileproject.implementation;


import com.hai811i.mobileproject.api.ApiService;
import com.hai811i.mobileproject.callback.AppointmentCallback;
import com.hai811i.mobileproject.callback.AppointmentsListCallback;
import com.hai811i.mobileproject.callback.VoidCallback;
import com.hai811i.mobileproject.dto.AppointmentDTO;

import com.hai811i.mobileproject.dto.ReserveRequest;
import com.hai811i.mobileproject.repository.AppointmentRepository;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentRepositoryImpl implements AppointmentRepository {
    private final ApiService apiService;

    public AppointmentRepositoryImpl(ApiService apiService) {
        this.apiService = apiService;
    }


        @Override
        public void reserveAppointment(Long slotId, ReserveRequest request, AppointmentCallback callback) {
            apiService.reserveAppointment(slotId, request).enqueue(new Callback<AppointmentDTO>() {
                @Override
                public void onResponse(Call<AppointmentDTO> call, Response<AppointmentDTO> response) {
                    if (response.isSuccessful()) {
                        callback.onSuccess(response.body());
                    } else {
                        callback.onFailure("Failed to reserve appointment: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<AppointmentDTO> call, Throwable t) {
                    callback.onFailure("Network error: " + t.getMessage());
                }
            });
        }

        @Override
        public void cancelAppointment(Long id, VoidCallback callback) {
            apiService.cancelAppointment(id).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onFailure("Failed to cancel appointment: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    callback.onFailure("Network error: " + t.getMessage());
                }
            });
        }

        @Override
        public void acceptAppointment(Long id, AppointmentCallback callback) {
            apiService.acceptAppointment(id).enqueue(new Callback<AppointmentDTO>() {
                @Override
                public void onResponse(Call<AppointmentDTO> call, Response<AppointmentDTO> response) {
                    if (response.isSuccessful()) {
                        callback.onSuccess(response.body());
                    } else {
                        callback.onFailure("Failed to accept appointment: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<AppointmentDTO> call, Throwable t) {
                    callback.onFailure("Network error: " + t.getMessage());
                }
            });
        }

        @Override
        public void rejectAppointment(Long id, VoidCallback callback) {
            apiService.rejectAppointment(id).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onFailure("Failed to reject appointment: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    callback.onFailure("Network error: " + t.getMessage());
                }
            });
        }

        @Override
        public void getDoctorAppointments(Long doctorId, AppointmentsListCallback callback) {
            apiService.getDoctorAppointments(doctorId).enqueue(new Callback<List<AppointmentDTO>>() {
                @Override
                public void onResponse(Call<List<AppointmentDTO>> call, Response<List<AppointmentDTO>> response) {
                    if (response.isSuccessful()) {
                        callback.onSuccess(response.body());
                    } else {
                        callback.onFailure("Failed to get doctor appointments: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<List<AppointmentDTO>> call, Throwable t) {
                    callback.onFailure("Network error: " + t.getMessage());
                }
            });
        }

        @Override
        public void getPatientAppointments(Long patientId, AppointmentsListCallback callback) {
            apiService.getPatientAppointments(patientId).enqueue(new Callback<List<AppointmentDTO>>() {
                @Override
                public void onResponse(Call<List<AppointmentDTO>> call, Response<List<AppointmentDTO>> response) {
                    if (response.isSuccessful()) {
                        callback.onSuccess(response.body());
                    } else {
                        callback.onFailure("Failed to get patient appointments: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<List<AppointmentDTO>> call, Throwable t) {
                    callback.onFailure("Network error: " + t.getMessage());
                }
            });
        }
    }
