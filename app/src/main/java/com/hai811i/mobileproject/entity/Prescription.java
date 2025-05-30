package com.hai811i.mobileproject.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Prescription {

    private Long id;
    private MedicalRecord medicalRecord;
    private String note;
    private List<com.example.mobileproject.entity.MedicationLine> medications = new ArrayList<>();
    private Instant dateCreated = Instant.now();

    // No-arg constructor
    public Prescription() {}

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }

    public void setMedicalRecord(MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<com.example.mobileproject.entity.MedicationLine> getMedications() {
        return medications;
    }

    public void setMedications(List<com.example.mobileproject.entity.MedicationLine> medications) {
        this.medications = medications;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }
}
