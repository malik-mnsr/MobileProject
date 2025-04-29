package com.hai811i.mobileproject.implementation;

import com.hai811i.mobileproject.api.ApiService;
import com.hai811i.mobileproject.entity.Doctor;
import com.hai811i.mobileproject.repository.DoctorRepository;
import com.hai811i.mobileproject.repository.LoginCallback;
import com.hai811i.mobileproject.request.LoginRequest;
import com.hai811i.mobileproject.response.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class DoctorRepositoryImpl implements DoctorRepository {
    private final ApiService apiService;

    public DoctorRepositoryImpl(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void loginDoctor(String email, String phone, LoginCallback callback) {
        apiService.loginDoctor(email, phone).enqueue(new Callback<Doctor>() {  // Change String to Doctor here
            @Override
            public void onResponse(Call<Doctor> call, Response<Doctor> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Doctor doctor = response.body();
                    String message = "Login successful!";
                    boolean success = true;
                    // Now, pass the Doctor object and other details to the callback
                    callback.onSuccess(new LoginResponse(doctor, message, success));
                } else {
                    callback.onFailure("Login failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Doctor> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }
}
