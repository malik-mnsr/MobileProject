package com.hai811i.mobileproject.callback;


import com.hai811i.mobileproject.response.LoginResponse;

public interface LoginCallback {
    void onSuccess(LoginResponse response);
    void onFailure(String errorMessage);
}