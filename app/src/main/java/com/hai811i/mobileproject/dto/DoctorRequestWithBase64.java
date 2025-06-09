package com.hai811i.mobileproject.dto;


import com.hai811i.mobileproject.entity.WorkingMode;
import com.hai811i.mobileproject.request.PatientRequest;



import java.util.List;


public class DoctorRequestWithBase64 {

    private String firstName;
    private String lastName;
    private int age;
    private String email;
    private String specialty;
    private String phone;
    private WorkingMode currentMode;
    private String profilePictureBase64;
    private String fcmToken; // <-- Add this
    private transient List<PatientRequest> patients;   // sous‑DTO défini plus bas


    public DoctorRequestWithBase64() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public WorkingMode getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(WorkingMode currentMode) {
        this.currentMode = currentMode;
    }

    public String getProfilePictureBase64() {
        return profilePictureBase64;
    }

    public void setProfilePictureBase64(String profilePictureBase64) {
        this.profilePictureBase64 = profilePictureBase64;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public List<PatientRequest> getPatients() {
        return patients;
    }

    public void setPatients(List<PatientRequest> patients) {
        this.patients = patients;
    }
}
