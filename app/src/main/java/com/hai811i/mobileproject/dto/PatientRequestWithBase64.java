package com.hai811i.mobileproject.dto;


import com.hai811i.mobileproject.entity.Doctor;

public class PatientRequestWithBase64 {
    private Long id;
    private String firstName;
    private String lastName;
    private int age;
    private String email;
    private String phone;
    private String address;
    private String profilePictureBase64;
    private Doctor doctorId;
    // Constructors
    public PatientRequestWithBase64() {
    }

    public PatientRequestWithBase64(Long id, String firstName, String lastName, int age,
                      String email, String phone, String address, String profilePictureBase64, Doctor doctorId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.profilePictureBase64 = profilePictureBase64;
        this.doctorId = doctorId;
    }

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

    public String getProfilePictureBase64() {
        return profilePictureBase64;
    }

    public void setProfilePictureBase64(String profilePictureBase64) {
        this.profilePictureBase64 = profilePictureBase64;
    }

    public Doctor getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Doctor doctorId) {
        this.doctorId = doctorId;
    }

    // Optional: toString() method
    @Override
    public String toString() {
        return "PatientDTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", profilePictureBase64='" + (profilePictureBase64 != null ? "[BASE64_IMAGE]" : "null") + '\'' +
                '}';
    }
}