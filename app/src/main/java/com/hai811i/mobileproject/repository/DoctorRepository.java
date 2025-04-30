package com.hai811i.mobileproject.repository;

import com.hai811i.mobileproject.request.DoctorRequestWithBase64;
import com.hai811i.mobileproject.entity.Doctor;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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