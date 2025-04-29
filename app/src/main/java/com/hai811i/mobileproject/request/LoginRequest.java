package com.hai811i.mobileproject.request;

public class LoginRequest {
    private final String email;  // Made final
    private final String phone;  // Made final

    public LoginRequest(String email, String phone) {
        this.email = email;
        this.phone = phone;
    }

    // Only getters (no setters)
    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}