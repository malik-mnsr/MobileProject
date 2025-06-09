package com.hai811i.mobileproject.entity;


import java.time.LocalDate;

public class MedicalRecord {

    private Integer recordId;
    private Patient patient;
    private Doctor doctor;
    private Appointment appointment;
    private RecordType recordType;
    private String title;
    private String description;
    private String content;
    private LocalDate dateCreated;

    public MedicalRecord() {
        this.dateCreated = LocalDate.now();
    }

    public MedicalRecord(Integer recordId, Patient patient, Doctor doctor, Appointment appointment,
                         RecordType recordType, String title, String description,
                         String content, LocalDate dateCreated) {
        this.recordId = recordId;
        this.patient = patient;
        this.doctor = doctor;
        this.appointment = appointment;
        this.recordType = recordType;
        this.title = title;
        this.description = description;
        this.content = content;
        this.dateCreated = dateCreated != null ? dateCreated : LocalDate.now();
    }

    // Getters and Setters

    public Integer getRecordId() { return recordId; }
    public void setRecordId(Integer recordId) { this.recordId = recordId; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }

    public Appointment getAppointment() { return appointment; }
    public void setAppointment(Appointment appointment) { this.appointment = appointment; }

    public RecordType getRecordType() { return recordType; }
    public void setRecordType(RecordType recordType) { this.recordType = recordType; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDate getDateCreated() { return dateCreated; }
    public void setDateCreated(LocalDate dateCreated) { this.dateCreated = dateCreated; }
}
