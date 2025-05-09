package com.hai811i.mobileproject.dto;

public class ReserveRequest {
    private Long patientId;
    private String motif; // or use Motif enum if preferred

    // Constructor
    public ReserveRequest(Long patientId, String motif) {
        this.patientId = patientId;
        this.motif = motif;
    }

    // Getters
    public Long getPatientId() { return patientId; }
    public String getMotif() { return motif; }

    // Setters (optional)
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public void setMotif(String motif) { this.motif = motif; }
}
