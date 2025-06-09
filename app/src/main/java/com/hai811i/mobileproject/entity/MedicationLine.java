package com.example.mobileproject.entity;

import com.hai811i.mobileproject.entity.Prescription;

public class MedicationLine {

    private Long id;
    private String name;         // e.g., "Doliprane"
    private String dosage;       // e.g., "500 mg"
    private String frequency;    // e.g., "3 × jour"
    private int durationDays;
    private Prescription prescription;

    // No-arg constructor
    public MedicationLine() {}

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public int getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(int durationDays) {
        this.durationDays = durationDays;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }
}
