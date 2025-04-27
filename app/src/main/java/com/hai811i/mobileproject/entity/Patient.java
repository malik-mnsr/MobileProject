package com.hai811i.mobileproject.entity;

public class Patient {
    private String id;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String medicalHistory;

    public Patient() {}

    public Patient(String id, String firstName, String lastName, String dateOfBirth, String medicalHistory) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.medicalHistory = medicalHistory;
    }

    // Getters and setters...
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getMedicalHistory() { return medicalHistory; }
    public void setMedicalHistory(String medicalHistory) { this.medicalHistory = medicalHistory; }
}