package com.hai811i.mobileproject.implementation;

import com.hai811i.mobileproject.api.ApiService;

import com.hai811i.mobileproject.callback.AuthUrlCallback;
import com.hai811i.mobileproject.callback.OAuthCallback;
import com.hai811i.mobileproject.repository.GoogleCalendarRepository;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoogleCalendarRepositoryImpl implements GoogleCalendarRepository {
    private final ApiService apiService;

    public GoogleCalendarRepositoryImpl(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void getGoogleAuthUrl(Long doctorId, AuthUrlCallback callback) {
        apiService.getGoogleAuthUrl(doctorId).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Failed to get auth URL: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public void handleGoogleOAuthCallback(String code, String state, OAuthCallback callback) {
        apiService.handleGoogleOAuthCallback(code, state).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Failed to handle OAuth callback: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }
}