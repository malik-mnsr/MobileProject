package com.hai811i.mobileproject.repository;

import com.hai811i.mobileproject.callback.DoctorCallback;
import com.hai811i.mobileproject.callback.DoctorsListCallback;
import com.hai811i.mobileproject.callback.LoginCallback;
import com.hai811i.mobileproject.callback.ModeCallback;
import com.hai811i.mobileproject.callback.ProfilePictureCallback;
import com.hai811i.mobileproject.callback.SimpleCallback;
import com.hai811i.mobileproject.callback.VoidCallback;
import com.hai811i.mobileproject.entity.WorkingMode;
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

    void uploadDoctorProfilePicture(int id, MultipartBody.Part file, VoidCallback callback);

    void getDoctorProfilePicture(int id, ProfilePictureCallback callback);

    void createDoctorWithPicture(RequestBody doctorRequest, MultipartBody.Part picture, DoctorCallback callback);

    void createDoctorWithPictureBase64(DoctorRequestWithBase64 request, DoctorCallback callback);

    void updateDoctorFcmToken(int doctorId, String token, VoidCallback callback);

    // Add these new methods for working with modes
    void getDoctorMode(long doctorId, ModeCallback callback);
    void updateDoctorMode(long doctorId, WorkingMode newMode, SimpleCallback callback);
}
