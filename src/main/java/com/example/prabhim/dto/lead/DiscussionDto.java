package com.example.prabhim.dto.lead;

public class DiscussionDto {

    private String id;
    private String date;
    private String channel = "Phone Call";
    private String author = "Admin Desk";
    private String notes;
    private String nextAction;
    private String expectedJoinDate;

    public DiscussionDto() {
    }

    public DiscussionDto(String id, String date, String channel, String author, String notes, String nextAction, String expectedJoinDate) {
        this.id = id;
        this.date = date;
        this.channel = channel;
        this.author = author;
        this.notes = notes;
        this.nextAction = nextAction;
        this.expectedJoinDate = expectedJoinDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
}
