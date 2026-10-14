package com.example.prabhim.dto.lead;

import jakarta.validation.constraints.NotBlank;

public class LeadDiscussionRequest {

    private String channel = "Phone Call";
    private String author = "Admin Desk";

    @NotBlank(message = "Discussion notes are required")
    private String notes;

    private String nextAction;
    private String expectedJoinDate;
    private String nextFollowUpDate;
    private String nextFollowUpTime;
    private String newStatus;

    public LeadDiscussionRequest() {
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getNextAction() {
        return nextAction;
    }

    public void setNextAction(String nextAction) {
        this.nextAction = nextAction;
    }

    public String getExpectedJoinDate() {
        return expectedJoinDate;
    }

    public void setExpectedJoinDate(String expectedJoinDate) {
        this.expectedJoinDate = expectedJoinDate;
    }

    public String getNextFollowUpDate() {
        return nextFollowUpDate;
    }

    public void setNextFollowUpDate(String nextFollowUpDate) {
        this.nextFollowUpDate = nextFollowUpDate;
    }

    public String getNextFollowUpTime() {
        return nextFollowUpTime;
    }

    public void setNextFollowUpTime(String nextFollowUpTime) {
        this.nextFollowUpTime = nextFollowUpTime;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }
}
