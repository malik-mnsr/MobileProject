package com.hai811i.mobileproject.repository;

public interface ProfilePictureCallback {
    void onSuccess(byte[] imageBytes);
    void onFailure(String errorMessage);
}
