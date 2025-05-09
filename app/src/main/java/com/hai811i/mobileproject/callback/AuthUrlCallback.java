package com.hai811i.mobileproject.callback;

public interface AuthUrlCallback {
    void onSuccess(String authUrl);
    void onFailure(String errorMessage);
}