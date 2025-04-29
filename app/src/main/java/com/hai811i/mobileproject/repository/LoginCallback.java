package com.hai811i.mobileproject.repository;


import com.hai811i.mobileproject.response.LoginResponse;

public interface LoginCallback {
    void onSuccess(LoginResponse response);   // Accepts a LoginResponse with a Doctor object
    void onFailure(String errorMessage);      // Failure callback
}
