package com.hai811i.mobileproject.api;

import com.hai811i.mobileproject.dto.DoctorRequestWithBase64;
import com.hai811i.mobileproject.entity.Doctor;
import com.hai811i.mobileproject.request.LoginRequest;
import com.hai811i.mobileproject.response.LoginResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface ApiService {
    // Login
    @POST("/api/doctors/login")
    Call<Doctor> loginDoctor(@Query("email") String email, @Query("phone") String phone);

    // Doctor CRUD
    @POST("/api/doctors")
    Call<Doctor> createDoctor(@Body Doctor doctor);

    @GET("/api/doctors/{id}")
    Call<Doctor> getDoctor(@Path("id") int id);

    @GET("/api/doctors")
    Call<List<Doctor>> getAllDoctors();

    @GET("/api/doctors/specialty/{specialty}")
    Call<List<Doctor>> getDoctorsBySpecialty(@Path("specialty") String specialty);

    @PUT("/api/doctors/{id}")
    Call<Doctor> updateDoctor(@Path("id") int id, @Body Doctor doctor);

    @DELETE("/api/doctors/{id}")
    Call<Void> deleteDoctor(@Path("id") int id);

    // Profile Picture
    @Multipart
    @POST("/api/doctors/{id}/profile-picture")
    Call<Void> uploadProfilePicture(
            @Path("id") int id,
            @Part MultipartBody.Part file);

    @GET("/api/doctors/{id}/profile-picture")
    Call<ResponseBody> getProfilePicture(@Path("id") int id);

    // Create doctor with picture
    @Multipart
    @POST("/api/doctors/create-doctor/with-picture")
    Call<Doctor> createDoctorWithPicture(
            @Part("doctorData") RequestBody doctorRequest,
            @Part MultipartBody.Part picture);


    @POST("/api/doctors/create-doctor/with-picture-base64")
    Call<Doctor> createDoctorWithPictureBase64(
            @Body DoctorRequestWithBase64 request);
}