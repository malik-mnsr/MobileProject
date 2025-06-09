package com.hai811i.mobileproject.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Doctor implements Serializable {

    private Long id;
    private String firstName;
    private String lastName;
    private int age;
    private String email;
    private String specialty;
    private String phone;
    private String profilePicture;
    private String profilePictureContentType;
    private WorkingMode currentMode = WorkingMode.NORMAL;
    private String gAccessToken;
    private String gRefreshToken;
    private Long gTokenExpiryMs;
    private String fcmToken;
    private List<Patient> patients = new ArrayList<>();

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getProfilePictureContentType() {
        return profilePictureContentType;
    }

    public void setProfilePictureContentType(String profilePictureContentType) {
        this.profilePictureContentType = profilePictureContentType;
    }

    public WorkingMode getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(WorkingMode currentMode) {
        this.currentMode = currentMode;
    }

    public String getgAccessToken() {
        return gAccessToken;
    }

    public void setgAccessToken(String gAccessToken) {
        this.gAccessToken = gAccessToken;
    }

    public String getgRefreshToken() {
        return gRefreshToken;
    }

    public void setgRefreshToken(String gRefreshToken) {
        this.gRefreshToken = gRefreshToken;
    }

    public Long getgTokenExpiryMs() {
        return gTokenExpiryMs;
    }

    public void setgTokenExpiryMs(Long gTokenExpiryMs) {
        this.gTokenExpiryMs = gTokenExpiryMs;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }
}
