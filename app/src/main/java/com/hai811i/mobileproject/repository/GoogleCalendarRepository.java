package com.hai811i.mobileproject.repository;

import com.hai811i.mobileproject.callback.AuthUrlCallback;
import com.hai811i.mobileproject.callback.OAuthCallback;

public interface GoogleCalendarRepository {
    void getGoogleAuthUrl(Long doctorId, AuthUrlCallback callback);
    void handleGoogleOAuthCallback(String code, String state, OAuthCallback callback);
}