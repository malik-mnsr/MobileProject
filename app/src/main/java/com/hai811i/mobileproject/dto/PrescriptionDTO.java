package com.hai811i.mobileproject.dto;

import java.time.LocalDate;

public class PrescriptionDTO {

    private Integer prescriptionId;
    private Integer recordId;
    private String medications;
    private Integer validityDays;
    private String status;
    private boolean sentToPharmacy;
    private String pharmacyDetails;
    private LocalDate dateIssued;

    // Default constructor
    public PrescriptionDTO() {}

    // All-arguments constructor
    public PrescriptionDTO(Integer prescriptionId, Integer recordId, String medications,
                           Integer validityDays, String status, boolean sentToPharmacy,
                           String pharmacyDetails, LocalDate dateIssued) {
        this.prescriptionId = prescriptionId;
        this.recordId = recordId;
        this.medications = medications;
        this.validityDays = validityDays;
        this.status = status;
        this.sentToPharmacy = sentToPharmacy;
        this.pharmacyDetails = pharmacyDetails;
        this.dateIssued = dateIssued;
    }

    // Getters and Setters
    public Integer getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(Integer prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public String getMedications() {
        return medications;
    }

    public void setMedications(String medications) {
        this.medications = medications;
    }

    public Integer getValidityDays() {
        return validityDays;
    }

    public void setValidityDays(Integer validityDays) {
        this.validityDays = validityDays;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isSentToPharmacy() {
        return sentToPharmacy;
    }

    public void setSentToPharmacy(boolean sentToPharmacy) {
        this.sentToPharmacy = sentToPharmacy;
    }

    public String getPharmacyDetails() {
        return pharmacyDetails;
    }

    public void setPharmacyDetails(String pharmacyDetails) {
        this.pharmacyDetails = pharmacyDetails;
    }

    public LocalDate getDateIssued() {
        return dateIssued;
    }

    public void setDateIssued(LocalDate dateIssued) {
        this.dateIssued = dateIssued;
    }
}
