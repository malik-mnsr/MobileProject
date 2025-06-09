package com.hai811i.mobileproject.entity;



public class DiagnosisSupport {

    private Integer supportId;
    private Integer recordId;
    private String symptoms;
    private String possibleDiagnoses;
    private String recommendedTests;
    private String aiNotes;
    private Float confidenceScore;

    public DiagnosisSupport() {
    }

    public DiagnosisSupport(Integer supportId, Integer recordId, String symptoms, String possibleDiagnoses,
                            String recommendedTests, String aiNotes, Float confidenceScore) {
        this.supportId = supportId;
        this.recordId = recordId;
        this.symptoms = symptoms;
        this.possibleDiagnoses = possibleDiagnoses;
        this.recommendedTests = recommendedTests;
        this.aiNotes = aiNotes;
        this.confidenceScore = confidenceScore;
    }

    public Integer getSupportId() {
        return supportId;
    }

    public void setSupportId(Integer supportId) {
        this.supportId = supportId;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getPossibleDiagnoses() {
        return possibleDiagnoses;
    }

    public void setPossibleDiagnoses(String possibleDiagnoses) {
        this.possibleDiagnoses = possibleDiagnoses;
    }

    public String getRecommendedTests() {
        return recommendedTests;
    }

    public void setRecommendedTests(String recommendedTests) {
        this.recommendedTests = recommendedTests;
    }

    public String getAiNotes() {
        return aiNotes;
    }

    public void setAiNotes(String aiNotes) {
        this.aiNotes = aiNotes;
    }

    public Float getConfidenceScore() {
        return confidenceScore;
    }

    public void setConfidenceScore(Float confidenceScore) {
        this.confidenceScore = confidenceScore;
    }
}
