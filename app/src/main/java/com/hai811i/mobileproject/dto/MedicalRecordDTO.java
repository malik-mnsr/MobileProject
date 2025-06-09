package com.hai811i.mobileproject.dto;

import java.time.LocalDate;

public class MedicalRecordDTO {
    private Integer recordId;
    private Long patientId;
    private Long doctorId;
    private Long appointmentId;
    private String recordType;
    private String title;
    private String description;
    private String content;
    private String dateCreated; // Use String if using Gson or Moshi

    // Empty constructor (required for deserialization)
    public MedicalRecordDTO() {}

    // Getters and Setters
    public Integer getRecordId() { return recordId; }
    public void setRecordId(Integer recordId) { this.recordId = recordId; }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }

    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }

    public String getRecordType() { return recordType; }
    public void setRecordType(String recordType) { this.recordType = recordType; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getDateCreated() { return dateCreated; }
    public void setDateCreated(String dateCreated) { this.dateCreated = dateCreated; }
}
