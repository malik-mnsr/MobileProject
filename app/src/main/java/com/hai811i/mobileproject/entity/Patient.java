package com.hai811i.mobileproject.entity;

import java.io.Serializable;
import java.util.List;

public class Patient implements Serializable {
    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private int age;
    private String phone;
    private String address;
    private String profilePicture; // base64 encoded
    private String profilePictureContentType;
    private Doctor doctor;


    // Constructor
    public Patient(int id, String email, String firstName, String lastName, int age,
                   String phone, String address, String profilePicture,
                   String profilePictureContentType, Doctor doctor
                   ) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.phone = phone;
        this.address = address;
        this.profilePicture = profilePicture;
        this.profilePictureContentType = profilePictureContentType;
        this.doctor = doctor;

    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }




    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", profilePicture='" + (profilePicture != null ? "[base64 image]" : "null") + '\'' +
                ", profilePictureContentType='" + profilePictureContentType + '\'' +
                ", doctor=" + (doctor != null ? doctor.getFirstName() + " " + doctor.getLastName() : "null") +

                '}';
    }
}
