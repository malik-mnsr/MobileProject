package com.hai811i.mobileproject.repository;

import com.hai811i.mobileproject.callback.AuthUrlCallback;
import com.hai811i.mobileproject.callback.CalendarEventCallback;
import com.hai811i.mobileproject.callback.CalendarStatusCallback;
import com.hai811i.mobileproject.callback.OAuthCallback;

public interface GoogleCalendarRepository {
    void getGoogleAuthUrl(Long doctorId, AuthUrlCallback callback);
    void handleGoogleOAuthCallback(String code, String state, OAuthCallback callback);
    void addAppointmentToCalendar(Long appointmentId, CalendarEventCallback callback);
    void removeAppointmentFromCalendar(Long appointmentId, String eventId, OAuthCallback callback);
    void checkCalendarConnection(Long doctorId, CalendarStatusCallback callback);
}