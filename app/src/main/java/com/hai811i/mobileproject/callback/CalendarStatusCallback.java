package com.hai811i.mobileproject.callback;

public interface CalendarStatusCallback {
    void onSuccess(boolean isConnected);
    void onFailure(String error);
}