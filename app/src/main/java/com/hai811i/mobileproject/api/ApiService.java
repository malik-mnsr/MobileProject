package com.hai811i.mobileproject.api;

import com.hai811i.mobileproject.entity.Doctor;
import com.hai811i.mobileproject.request.LoginRequest;
import com.hai811i.mobileproject.response.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @POST("/api/doctors/login")
    Call<Doctor> loginDoctor(@Query("email") String email, @Query("phone") String phone);  // Change to return Doctor, not String
}

