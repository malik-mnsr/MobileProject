package com.hai811i.mobileproject.entity;


import java.time.LocalDateTime;

public class Slot {

    private Long id;
    private Doctor doctor;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private boolean reserved = false;
    private Appointment appointment;

    public Slot() {}

    public Slot(Long id, Doctor doctor, LocalDateTime startAt, LocalDateTime endAt,
                boolean reserved, Appointment appointment) {
        this.id = id;
        this.doctor = doctor;
        this.startAt = startAt;
        this.endAt = endAt;
        this.reserved = reserved;
        this.appointment = appointment;
    }

    // Getters and Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }

    public LocalDateTime getStartAt() { return startAt; }
    public void setStartAt(LocalDateTime startAt) { this.startAt = startAt; }

    public LocalDateTime getEndAt() { return endAt; }
    public void setEndAt(LocalDateTime endAt) { this.endAt = endAt; }

    public boolean isReserved() { return reserved; }
    public void setReserved(boolean reserved) { this.reserved = reserved; }

    public Appointment getAppointment() { return appointment; }
    public void setAppointment(Appointment appointment) { this.appointment = appointment; }
}
