package com.hai811i.mobileproject.repository;

import com.hai811i.mobileproject.response.LoginResponse;


public interface DoctorRepository {
    void loginDoctor(String email, String phone, LoginCallback callback);


}