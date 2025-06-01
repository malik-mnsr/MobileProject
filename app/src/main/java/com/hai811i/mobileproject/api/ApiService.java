package com.hai811i.mobileproject.api;

import android.graphics.pdf.PdfDocument;

import com.hai811i.mobileproject.dto.MedicalRecordDTO;
import com.hai811i.mobileproject.dto.PatientDTO;
import com.hai811i.mobileproject.dto.PatientRequestWithBase64;
import com.hai811i.mobileproject.dto.PrescriptionDTO;
import com.hai811i.mobileproject.dto.SlotCreateDTO;
import com.hai811i.mobileproject.dto.SlotDTO;
import com.hai811i.mobileproject.dto.AppointmentDTO;
import com.hai811i.mobileproject.dto.ReserveRequest;

import com.hai811i.mobileproject.dto.TestNotificationRequest;
import com.hai811i.mobileproject.entity.Appointment;
import com.hai811i.mobileproject.entity.DrugReference;
import com.hai811i.mobileproject.entity.Motif;
import com.hai811i.mobileproject.entity.Page;
import com.hai811i.mobileproject.entity.WorkingMode;
import com.hai811i.mobileproject.request.DoctorRequestWithBase64;
import com.hai811i.mobileproject.entity.Doctor;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

import retrofit2.http.Part;
import retrofit2.http.Query;

import okhttp3.ResponseBody;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


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

    // Add these new endpoints
    @GET("/api/doctors/{id}/mode")
    Call<WorkingMode> getDoctorMode(@Path("id") long doctorId);

    @PUT("/api/doctors/{id}/mode")
    Call<Void> updateDoctorMode(@Path("id") long doctorId, @Body WorkingMode newMode);

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
    @Headers("Content-Type: application/json")
    Call<Doctor> createDoctorWithPictureBase64(@Body DoctorRequestWithBase64 request);

    @POST("/api/doctors/{doctorId}/fcm/token")
    Call<Void> updateDoctorFcmToken(
            @Path("doctorId") long doctorId,
            @Body Map<String, String> tokenRequest
    );

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


    @POST("/api/patients/create-patient/with-picture-base64")
    @Headers("Content-Type: application/json")
    Call<Patient> createPatientWithPictureBase64(@Body PatientRequestWithBase64 request);
    /* ======================= APPOINTMENT ENDPOINTS ======================= */

    // 1. Reserve an appointment
    @POST("/api/appointments/reserve/{slotId}")
    Call<AppointmentDTO> reserveAppointment(
            @Path("slotId") Long slotId,
            @Body ReserveRequest request);

    // 2. Cancel an appointment
    @DELETE("/api/appointments/{id}")
    Call<Void> cancelAppointment(@Path("id") Long id);

    // 3a. Accept pending appointment
    @POST("/api/appointments/{id}/accept")
    Call<AppointmentDTO> acceptAppointment(@Path("id") Long id);

    // 3b. Reject pending appointment
    @POST("/api/appointments/{id}/reject")
    Call<Void> rejectAppointment(@Path("id") Long id);

    // 4a. List doctor's appointments
    @GET("/api/appointments/doctor/{doctorId}")
    Call<List<AppointmentDTO>> getDoctorAppointments(@Path("doctorId") Long doctorId);

    // 4b. List patient's appointments
    @GET("/api/appointments/patient/{patientId}")
    Call<List<AppointmentDTO>> getPatientAppointments(@Path("patientId") Long patientId);
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

    /* ======================= GOOGLE CALENDAR ENDPOINTS ======================= */

    @GET("/google/auth-url/{doctorId}")
    Call<String> getGoogleAuthUrl(@Path("doctorId") Long doctorId);

    @GET("/oauth2/callback")
    Call<String> handleGoogleOAuthCallback(
            @Query("code") String code,
            @Query("state") String state);

    // Add appointment to Google Calendar
    @POST("/appointments/{appointmentId}/google-calendar")
    Call<String> addAppointmentToGoogleCalendar(@Path("appointmentId") Long appointmentId);

    // Remove appointment from Google Calendar
    @DELETE("/appointments/{appointmentId}/google-calendar/{eventId}")
    Call<Void> removeAppointmentFromGoogleCalendar(
            @Path("appointmentId") Long appointmentId,
            @Path("eventId") String eventId);

    // Check if doctor has Google Calendar connected
    @GET("/doctors/{doctorId}/google-calendar/status")
    Call<Boolean> checkGoogleCalendarConnection(@Path("doctorId") Long doctorId);

    /* ======================= MEDICAL RECORDS ENDPOINTS ======================= */

    @POST("/api/records/appointment/{appointmentId}")
    Call<MedicalRecordDTO> createMedicalRecord(
            @Path("appointmentId") Long appointmentId,
            @Body MedicalRecordDTO dto);


    @GET("/api/records/patient/{patientId}/history")
    Call<List<MedicalRecordDTO>> getMedicalRecordsHistory(
            @Path("patientId") Long patientId);


    // Create a prescription for an existing medical record
    @POST("/api/prescriptions/record/{recordId}")
    Call<PrescriptionDTO> createPrescription(
            @Path("recordId") Integer recordId,
            @Body PrescriptionDTO body);

    // Get a single prescription
    @GET("/api/prescriptions/{id}")
    Call<PrescriptionDTO> getPrescription(
            @Path("id") Long id);

    // List all prescriptions for a patient
    @GET("/api/prescriptions/patient/{patientId}")
    Call<List<PrescriptionDTO>> listPrescriptionsForPatient(
            @Path("patientId") Long patientId);

    // Delete a prescription
    @DELETE("/api/prescriptions/{id}")
    Call<Void> deletePrescription(
            @Path("id") Long id);

    // Download prescription as PDF
    @GET("/api/prescriptions/{id}/pdf")
    Call<ResponseBody> downloadPrescriptionPdf(
            @Path("id") Long id);

    // Send prescription to patient via email
    @POST("/api/prescriptions/{id}/send-to-patient")
    Call<Void> sendPrescriptionToPatient(
            @Path("id") Long id);

    @POST("/api/notifications/notify-doctor")
    Call<String> notifyDoctor(@Body Appointment appointment);

    /**
     * Send a test notification
     * @param request Contains doctorId and custom message
     * @return Success message
     */
    @POST("/api/notifications/test-notification")
    Call<String> sendTestNotification(@Body TestNotificationRequest request);

    // Get paginated drug list with optional search
    @GET("/api/drugs")
    Call<Page<DrugReference>> getDrugs(
            @Query("q") String query,
            @Query("page") int page,         // replaces Pageable
            @Query("size") int size,         // replaces Pageable
            @Query("sort") String sort       // replaces Pageable
    );

    // Drug autocomplete
    @GET("/api/drugs/autocomplete")
    Call<List<com.example.mobileproject.dto.DrugLiteDTO>> autocompleteDrugs(
            @Query("term") String term,
            @Query("limit") int limit
    );

    // Get drug by ID
    @GET("/api/drugs/{id}")
    Call<DrugReference> getDrugById(@Path("id") long id);

    // Create new drug
    @POST("/api/drugs")
    Call<DrugReference> createDrug(@Body DrugReference drug);

    // Update drug
    @PATCH("/api/drugs/{id}")
    Call<DrugReference> updateDrug(
            @Path("id") long id,
            @Body DrugReference updatedDrug
    );

    // Delete drug
    @DELETE("/api/drugs/{id}")
    Call<Void> deleteDrug(@Path("id") long id);

    /* ======================= MEDICAL RECORD ENDPOINTS ======================= */

    @POST("/api/records/appointment/{appointmentId}")
    Call<MedicalRecordDTO> createMedicalRecordForAppointment(
            @Path("appointmentId") Long appointmentId,
            @Body MedicalRecordDTO medicalRecordDTO);

    @GET("/api/records/appointment/{appointmentId}")
    Call<MedicalRecordDTO> getMedicalRecordByAppointment(
            @Path("appointmentId") Long appointmentId);

    @PUT("/api/records/{recordId}")
    Call<MedicalRecordDTO> updateMedicalRecord(
            @Path("recordId") Integer recordId,
            @Body MedicalRecordDTO medicalRecordDTO);

    @GET("/api/records/{recordId}")
    Call<MedicalRecordDTO> getMedicalRecordById(
            @Path("recordId") Integer recordId);

    @DELETE("/api/records/{recordId}")
    Call<Void> deleteMedicalRecord(
            @Path("recordId") Integer recordId);

    @GET("/api/records/patient/{patientId}/history")
    Call<List<MedicalRecordDTO>> getPatientMedicalHistory(
            @Path("patientId") Long patientId);

    @GET("/api/records/doctor/{doctorId}/history")
    Call<List<MedicalRecordDTO>> getFilteredMedicalRecords(
            @Path("doctorId") Long doctorId,
            @Query("date") String date,  // Changed to String
            @Query("motif") Motif motif);  // Changed to String

    @GET("/api/records/doctor/{doctorId}/history/today/{motif}")
    Call<List<MedicalRecordDTO>> getTodayMedicalRecordsByMotif(
            @Path("doctorId") Long doctorId,
            @Path("motif") Motif motif);

    @GET("/api/records/doctor/{doctorId}/today")
    Call<List<MedicalRecordDTO>> getTodayMedicalRecords(
            @Path("doctorId") Long doctorId,
            @Query("motif") Motif motif);
}
