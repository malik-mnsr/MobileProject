package com.hai811i.mobileproject.entity;



public class Appointment {
    private String id;
    private String patientId;
    private long timestamp;
    private String reason;

    public Appointment() {}

    public Appointment(String id, String patientId, long timestamp, String reason) {
        this.id = id;
        this.patientId = patientId;
        this.timestamp = timestamp;
        this.reason = reason;
    }

    // Getters and setters...
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}