package com.hai811i.mobileproject.callback;

public interface OAuthCallback {
    void onSuccess(String message);
    void onFailure(String errorMessage);
}