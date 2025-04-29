package com.hai811i.mobileproject.response;

import com.hai811i.mobileproject.entity.Doctor;

public class LoginResponse {
    private boolean success;
    private String message;
    private Doctor doctor;

    public LoginResponse(Doctor doctor, String message, boolean success) {
        this.doctor = doctor;
        this.message = message;
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
}
