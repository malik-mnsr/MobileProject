package com.hai811i.mobileproject.entity;

public class Doctor {
    private int id;
    private String firstName;
    private String lastName;
    private int age;
    private String email;
    private String specialty;
    private String phone;
    private String profilePicture; // base64 encoded
    private String profilePictureContentType;
    private String currentMode;

    // Constructeur
    public Doctor(int id, String firstName, String lastName, int age, String email, String specialty,
                  String phone, String profilePicture, String profilePictureContentType, String currentMode) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.specialty = specialty;
        this.phone = phone;
        this.profilePicture = profilePicture;
        this.profilePictureContentType = profilePictureContentType;
        this.currentMode = currentMode;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(String currentMode) {
        this.currentMode = currentMode;
    }
}
