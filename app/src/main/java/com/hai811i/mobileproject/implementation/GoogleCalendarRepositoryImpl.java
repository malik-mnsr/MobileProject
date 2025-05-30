package com.hai811i.mobileproject.implementation;

import android.util.Log;

import com.hai811i.mobileproject.api.ApiService;

import com.hai811i.mobileproject.callback.AuthUrlCallback;
import com.hai811i.mobileproject.callback.OAuthCallback;
import com.hai811i.mobileproject.repository.GoogleCalendarRepository;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.hai811i.mobileproject.api.ApiService;
import com.hai811i.mobileproject.callback.AuthUrlCallback;
import com.hai811i.mobileproject.callback.CalendarEventCallback;
import com.hai811i.mobileproject.callback.CalendarStatusCallback;
import com.hai811i.mobileproject.callback.OAuthCallback;
import com.hai811i.mobileproject.repository.GoogleCalendarRepository;

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
                    callback.onFailure("Failed to get auth URL (HTTP " + response.code() + "): " + response.message());
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
                    Log.e("OAuth", "Failed OAuth callback: HTTP " + response.code() + " " + response.message());
                    callback.onFailure("OAuth failed (HTTP " + response.code() + "): " + response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("OAuth", "Network error during OAuth callback", t);
                callback.onFailure("Network error: " + t.getMessage());
            }

        });
    }

    @Override
    public void addAppointmentToCalendar(Long appointmentId, CalendarEventCallback callback) {
        apiService.addAppointmentToGoogleCalendar(appointmentId).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Add appointment failed (HTTP " + response.code() + "): " + response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public void removeAppointmentFromCalendar(Long appointmentId, String eventId, OAuthCallback callback) {
        apiService.removeAppointmentFromGoogleCalendar(appointmentId, eventId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess("Appointment removed successfully");
                } else {
                    callback.onFailure("Remove failed (HTTP " + response.code() + "): " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public void checkCalendarConnection(Long doctorId, CalendarStatusCallback callback) {
        apiService.checkGoogleCalendarConnection(doctorId).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Connection check failed (HTTP " + response.code() + "): " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

}