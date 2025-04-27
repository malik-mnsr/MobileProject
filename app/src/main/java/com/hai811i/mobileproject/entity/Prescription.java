package com.hai811i.mobileproject.entity;

public class Prescription {
    private String id;
    private String appointmentId;
    private String medication;
    private String dosage;

    public Prescription() {}

    public Prescription(String id, String appointmentId, String medication, String dosage) {
        this.id = id;
        this.appointmentId = appointmentId;
        this.medication = medication;
        this.dosage = dosage;
    }

    // Getters and setters...
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getAppointmentId() { return appointmentId; }
    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }

    public String getMedication() { return medication; }
    public void setMedication(String medication) { this.medication = medication; }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }
}