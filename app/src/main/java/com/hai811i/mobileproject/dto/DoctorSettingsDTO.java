package com.hai811i.mobileproject.dto;

public class DoctorSettingsDTO {

    private Integer settingId;
    private Long doctorId;
    private String modePreferences;
    private String notificationPreferences;
    private String emergencyContacts;
    private String awayMessage;

    // Default constructor
    public DoctorSettingsDTO() {}

    // All-arguments constructor
    public DoctorSettingsDTO(Integer settingId, Long doctorId, String modePreferences,
                             String notificationPreferences, String emergencyContacts,
                             String awayMessage) {
        this.settingId = settingId;
        this.doctorId = doctorId;
        this.modePreferences = modePreferences;
        this.notificationPreferences = notificationPreferences;
        this.emergencyContacts = emergencyContacts;
        this.awayMessage = awayMessage;
    }

    // Getters and Setters
    public Integer getSettingId() {
        return settingId;
    }

    public void setSettingId(Integer settingId) {
        this.settingId = settingId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getModePreferences() {
        return modePreferences;
    }

    public void setModePreferences(String modePreferences) {
        this.modePreferences = modePreferences;
    }

    public String getNotificationPreferences() {
        return notificationPreferences;
    }

    public void setNotificationPreferences(String notificationPreferences) {
        this.notificationPreferences = notificationPreferences;
    }

    public String getEmergencyContacts() {
        return emergencyContacts;
    }

    public void setEmergencyContacts(String emergencyContacts) {
        this.emergencyContacts = emergencyContacts;
    }

    public String getAwayMessage() {
        return awayMessage;
    }

    public void setAwayMessage(String awayMessage) {
        this.awayMessage = awayMessage;
    }
}
