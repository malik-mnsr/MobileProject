package com.hai811i.mobileproject.implementation;

import com.hai811i.mobileproject.api.ApiService;
import com.hai811i.mobileproject.callback.NotificationCallback;
import com.hai811i.mobileproject.dto.TestNotificationRequest;
import com.hai811i.mobileproject.entity.Appointment;
import com.hai811i.mobileproject.repository.NotificationRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationRepositoryImpl implements NotificationRepository {
    private final ApiService apiService;

    public NotificationRepositoryImpl(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void notifyDoctor(Appointment appointment, NotificationCallback callback) {
        apiService.notifyDoctor(appointment).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Failed to send notification: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public void sendTestNotification(TestNotificationRequest request, NotificationCallback callback) {
        apiService.sendTestNotification(request).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Failed to send test notification: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }
}