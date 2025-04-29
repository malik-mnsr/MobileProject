package com.hai811i.mobileproject.repository;

import com.hai811i.mobileproject.api.ApiService;
import com.hai811i.mobileproject.dto.DoctorRequestWithBase64;
import com.hai811i.mobileproject.entity.Doctor;
import com.hai811i.mobileproject.request.PatientRequest;
import com.hai811i.mobileproject.response.LoginResponse;

import java.io.File;
import java.util.List;


import android.util.Log;


import java.io.IOException;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public interface DoctorRepository {
    void loginDoctor(String email, String phone, LoginCallback callback);

    void createDoctor(Doctor doctor, DoctorCallback callback);

    void getDoctor(int id, DoctorCallback callback);

    void getAllDoctors(DoctorsListCallback callback);

    void getDoctorsBySpecialty(String specialty, DoctorsListCallback callback);

    void updateDoctor(int id, Doctor doctor, DoctorCallback callback);

    void deleteDoctor(int id, VoidCallback callback);

    void uploadProfilePicture(int id, MultipartBody.Part file, VoidCallback callback);

    void getProfilePicture(int id, ProfilePictureCallback callback);

    void createDoctorWithPicture(RequestBody doctorRequest, MultipartBody.Part picture, DoctorCallback callback);

    void createDoctorWithPictureBase64(DoctorRequestWithBase64 request, DoctorCallback callback);
}