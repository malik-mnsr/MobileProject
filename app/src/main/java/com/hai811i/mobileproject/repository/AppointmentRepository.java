package com.hai811i.mobileproject.repository;



import com.hai811i.mobileproject.callback.AppointmentCallback;
import com.hai811i.mobileproject.callback.AppointmentsListCallback;
import com.hai811i.mobileproject.callback.VoidCallback;
import com.hai811i.mobileproject.dto.ReserveRequest;

public interface AppointmentRepository {
    void reserveAppointment(Long slotId, ReserveRequest request, AppointmentCallback callback);
    void cancelAppointment(Long id, VoidCallback callback);
    void getAppointmentsByDoctor(Long doctorId, AppointmentsListCallback callback);
    void getAppointmentsByPatient(Long patientId, AppointmentsListCallback callback);
}