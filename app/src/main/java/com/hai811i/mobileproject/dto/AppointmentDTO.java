package com.hai811i.mobileproject.dto;

public class AppointmentDTO {
    private Long id;
    private SlotDTO slot;
    private PatientDTO patient;
    private String motif;
    private String status;
    private String googleEventLink;

    // Constructor
    public AppointmentDTO(Long id, SlotDTO slot, PatientDTO patient, String motif, String status, String googleEventLink) {
        this.id = id;
        this.slot = slot;
        this.patient = patient;
        this.motif = motif;
        this.status = status;
        this.googleEventLink = googleEventLink;
    }

    // Getters
    public Long getId() { return id; }
    public SlotDTO getSlot() { return slot; }
    public PatientDTO getPatient() { return patient; }
    public String getMotif() { return motif; }
    public String getStatus() { return status; }
    public String getGoogleEventLink() { return googleEventLink; }

    // Setters (optional, if mutability is needed)
    public void setId(Long id) { this.id = id; }
    public void setSlot(SlotDTO slot) { this.slot = slot; }
    public void setPatient(PatientDTO patient) { this.patient = patient; }
    public void setMotif(String motif) { this.motif = motif; }
    public void setStatus(String status) { this.status = status; }
    public void setGoogleEventLink(String googleEventLink) { this.googleEventLink = googleEventLink; }
}
