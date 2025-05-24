package com.hai811i.mobileproject.entity;


import java.time.LocalDateTime;

public class Appointment {

    private Long id;
    private Long slotId;
    private Long patientId;
    private String motif;
    private String status;
    private String googleEventId;
    private String googleEventLink;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private LocalDateTime start;
    private LocalDateTime end;

    public Appointment() {
    }

    public Appointment(Long id, Long slotId, Long patientId, String motif, String status,
                       String googleEventId, String googleEventLink,
                       LocalDateTime createdAt, LocalDateTime updatedAt,
                       LocalDateTime start, LocalDateTime end) {
        this.id = id;
        this.slotId = slotId;
        this.patientId = patientId;
        this.motif = motif;
        this.status = status;
        this.googleEventId = googleEventId;
        this.googleEventLink = googleEventLink;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.start = start;
        this.end = end;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGoogleEventId() {
        return googleEventId;
    }

    public void setGoogleEventId(String googleEventId) {
        this.googleEventId = googleEventId;
    }

    public String getGoogleEventLink() {
        return googleEventLink;
    }

    public void setGoogleEventLink(String googleEventLink) {
        this.googleEventLink = googleEventLink;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
}
