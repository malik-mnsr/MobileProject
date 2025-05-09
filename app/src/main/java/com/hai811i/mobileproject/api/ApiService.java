package com.hai811i.mobileproject.api;

import com.hai811i.mobileproject.dto.PatientDTO;
import com.hai811i.mobileproject.dto.SlotCreateDTO;
import com.hai811i.mobileproject.dto.SlotDTO;
import com.hai811i.mobileproject.dto.AppointmentDTO;
import com.hai811i.mobileproject.dto.ReserveRequest;

import com.hai811i.mobileproject.request.DoctorRequestWithBase64;
import com.hai811i.mobileproject.entity.Doctor;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;

import retrofit2.http.Part;
import retrofit2.http.Query;

import okhttp3.ResponseBody;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import java.util.List;


import com.hai811i.mobileproject.entity.Patient;

public interface ApiService {
    /* ======================= DOCTOR ENDPOINTS ======================= */

    // Doctor CRUD
    @POST("/api/doctors")
    Call<Doctor> createDoctor(@Body Doctor doctor);

    @GET("/api/doctors/{id}")
    Call<Doctor> getDoctor(@Path("id") long id);

    @GET("/api/doctors")
    Call<List<Doctor>> getAllDoctors();

    @GET("/api/doctors/specialty/{specialty}")
    Call<List<Doctor>> getDoctorsBySpecialty(@Path("specialty") String specialty);

    @PUT("/api/doctors/{id}")
    Call<Doctor> updateDoctor(@Path("id") long id, @Body Doctor doctor);

    @DELETE("/api/doctors/{id}")
    Call<Void> deleteDoctor(@Path("id") long id);

    // Doctor Authentication
    @POST("/api/doctors/login")
    Call<Doctor> loginDoctor(@Query("email") String email, @Query("phone") String phone);

    // Profile Picture
    @Multipart
    @POST("/api/doctors/{id}/profile-picture")
    Call<Void> uploadDoctorProfilePicture(
            @Path("id") long id,
            @Part MultipartBody.Part file);

    @GET("/api/doctors/{id}/profile-picture")
    Call<ResponseBody> getDoctorProfilePicture(@Path("id") long id);

    // Create with picture
    @Multipart
    @POST("/api/doctors/create-doctor/with-picture")
    Call<Doctor> createDoctorWithPicture(
            @Part("doctorData") RequestBody doctorRequest,
            @Part MultipartBody.Part picture);

    @POST("/api/doctors/create-doctor/with-picture-base64")
    Call<Doctor> createDoctorWithPictureBase64(@Body DoctorRequestWithBase64 request);

    /* ======================= PATIENT ENDPOINTS ======================= */

    @POST("/api/patients/doctor/{doctorId}")
    Call<PatientDTO> createPatient(
            @Path("doctorId") Long doctorId,
            @Body Patient patient);

    @GET("/api/patients/{id}")
    Call<PatientDTO> getPatient(@Path("id") Long id);

    @GET("/api/patients")
    Call<List<PatientDTO>> getAllPatients();

    @GET("/api/patients/doctor/{doctorId}")
    Call<List<PatientDTO>> getPatientsByDoctor(@Path("doctorId") Long doctorId);

    @PUT("/api/patients/{id}")
    Call<PatientDTO> updatePatient(
            @Path("id") Long id,
            @Body Patient patient);

    @DELETE("/api/patients/{id}")
    Call<Void> deletePatient(@Path("id") Long id);

    @Multipart
    @POST("/api/patients/{id}/profile-picture")
    Call<Void> uploadPatientProfilePicture(
            @Path("id") Long id,
            @Part MultipartBody.Part file);

    @GET("/api/patients/{id}/profile-picture")
    Call<ResponseBody> getPatientProfilePicture(@Path("id") Long id);

    /* ======================= APPOINTMENT ENDPOINTS ======================= */

    @POST("/api/appointments/reserve/{slotId}")
    Call<AppointmentDTO> reserveAppointment(
            @Path("slotId") Long slotId,
            @Body ReserveRequest request);

    @DELETE("/api/appointments/{id}")
    Call<Void> cancelAppointment(@Path("id") Long id);

    @GET("/api/appointments/doctor/{doctorId}")
    Call<List<AppointmentDTO>> getAppointmentsByDoctor(@Path("doctorId") Long doctorId);

    @GET("/api/appointments/patient/{patientId}")
    Call<List<AppointmentDTO>> getAppointmentsByPatient(@Path("patientId") Long patientId);

    /* ======================= SLOT ENDPOINTS ======================= */

    @POST("/api/slots/doctor/{doctorId}")
    Call<List<SlotDTO>> createSlots(
            @Path("doctorId") Long doctorId,
            @Body List<SlotCreateDTO> slotCreateDTOs);

    @GET("/api/slots/doctor/{doctorId}")
    Call<List<SlotDTO>> getFreeSlots(
            @Path("doctorId") Long doctorId,
            @Query("date") String date); // Use String and parse to LocalDate on backend

    /* ======================= GOOGLE CALENDAR ENDPOINTS ======================= */

    @GET("/google/auth-url/{doctorId}")
    Call<String> getGoogleAuthUrl(@Path("doctorId") Long doctorId);

    @GET("/oauth2/callback")
    Call<String> handleGoogleOAuthCallback(
            @Query("code") String code,
            @Query("state") String state);
}