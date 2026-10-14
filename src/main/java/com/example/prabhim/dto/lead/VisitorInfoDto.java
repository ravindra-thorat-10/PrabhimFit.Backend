package com.example.prabhim.dto.lead;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VisitorInfoDto {

    private String visitDate;
    private String trialSlot = "06:00 PM - 07:00 PM";
    private Boolean trialCompleted = false;

    @JsonProperty("interestedInPT")
    private Boolean interestedInPT = false;

    private String bodyFatPercentage;
    private String healthConditions = "None";
    private String notes;

    public VisitorInfoDto() {
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public String getTrialSlot() {
        return trialSlot;
    }

    public void setTrialSlot(String trialSlot) {
        this.trialSlot = trialSlot;
    }

    public Boolean getTrialCompleted() {
        return trialCompleted;
    }

    public void setTrialCompleted(Boolean trialCompleted) {
        this.trialCompleted = trialCompleted;
    }

    public Boolean getInterestedInPT() {
        return interestedInPT;
    }

    public void setInterestedInPT(Boolean interestedInPT) {
        this.interestedInPT = interestedInPT;
    }

    public String getBodyFatPercentage() {
        return bodyFatPercentage;
    }

    public void setBodyFatPercentage(String bodyFatPercentage) {
        this.bodyFatPercentage = bodyFatPercentage;
    }

    public String getHealthConditions() {
        return healthConditions;
    }

    public void setHealthConditions(String healthConditions) {
        this.healthConditions = healthConditions;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
