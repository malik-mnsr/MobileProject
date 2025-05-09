package com.hai811i.mobileproject.entity;


import java.time.LocalDateTime;

public class AppointmentSlot {
    private Integer id;
    private Doctor doctor;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private SlotStatus status;

    public AppointmentSlot() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public SlotStatus getStatus() {
        return status;
    }

    public void setStatus(SlotStatus status) {
        this.status = status;
    }

    public String getFormattedTime() {
        // Format your time as needed, e.g., "8:00 AM - 9:00 AM"
        return startTime + " - " + endTime;
    }

    public boolean isAvailable() {
        return status == SlotStatus.AVAILABLE;
    }

    public boolean isBooked() {
        return status == SlotStatus.BOOKED;
    }

    public boolean isUnavailable() {
        return status == SlotStatus.UNAVAILABLE;
    }
}

