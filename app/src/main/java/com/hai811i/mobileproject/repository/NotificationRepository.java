package com.hai811i.mobileproject.repository;

import com.hai811i.mobileproject.callback.NotificationCallback;
import com.hai811i.mobileproject.dto.TestNotificationRequest;
import com.hai811i.mobileproject.entity.Appointment;

public interface NotificationRepository {
    void notifyDoctor(Appointment appointment, NotificationCallback callback);
    void sendTestNotification(TestNotificationRequest request, NotificationCallback callback);
}
