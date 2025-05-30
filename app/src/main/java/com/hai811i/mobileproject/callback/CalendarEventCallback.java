package com.hai811i.mobileproject.callback;

public interface CalendarEventCallback {
    void onSuccess(String eventUrl);
    void onFailure(String error);
}