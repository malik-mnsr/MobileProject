package com.hai811i.mobileproject.callback;

public interface ProfilePictureCallback {
    void onSuccess(byte[] imageBytes);
    void onFailure(String errorMessage);
}
