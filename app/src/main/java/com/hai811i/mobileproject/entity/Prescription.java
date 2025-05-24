package com.hai811i.mobileproject.entity;


import java.time.LocalDate;

public class Prescription {

    private Integer prescriptionId;
    private MedicalRecord record;
    private String medications;  // JSON list of {name, dosage, frequency}
    private Integer validityDays = 30;
    private PrescriptionStatus status = PrescriptionStatus.ACTIVE;
    private boolean sentToPharmacy = false;
    private String pharmacyDetails;
    private LocalDate dateIssued = LocalDate.now();

    public Prescription() {}

    public Prescription(Integer prescriptionId, MedicalRecord record, String medications,
                        Integer validityDays, PrescriptionStatus status,
                        boolean sentToPharmacy, String pharmacyDetails,
                        LocalDate dateIssued) {
        this.prescriptionId = prescriptionId;
        this.record = record;
        this.medications = medications;
        this.validityDays = validityDays != null ? validityDays : 30;
        this.status = status != null ? status : PrescriptionStatus.ACTIVE;
        this.sentToPharmacy = sentToPharmacy;
        this.pharmacyDetails = pharmacyDetails;
        this.dateIssued = dateIssued != null ? dateIssued : LocalDate.now();
    }

    // Getters and Setters

    public Integer getPrescriptionId() { return prescriptionId; }
    public void setPrescriptionId(Integer prescriptionId) { this.prescriptionId = prescriptionId; }

    public MedicalRecord getRecord() { return record; }
    public void setRecord(MedicalRecord record) { this.record = record; }

    public String getMedications() { return medications; }
    public void setMedications(String medications) { this.medications = medications; }

    public Integer getValidityDays() { return validityDays; }
    public void setValidityDays(Integer validityDays) { this.validityDays = validityDays; }

    public PrescriptionStatus getStatus() { return status; }
    public void setStatus(PrescriptionStatus status) { this.status = status; }

    public boolean isSentToPharmacy() { return sentToPharmacy; }
    public void setSentToPharmacy(boolean sentToPharmacy) { this.sentToPharmacy = sentToPharmacy; }

    public String getPharmacyDetails() { return pharmacyDetails; }
    public void setPharmacyDetails(String pharmacyDetails) { this.pharmacyDetails = pharmacyDetails; }

    public LocalDate getDateIssued() { return dateIssued; }
    public void setDateIssued(LocalDate dateIssued) { this.dateIssued = dateIssued; }
}
