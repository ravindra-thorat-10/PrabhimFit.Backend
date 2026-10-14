package com.example.prabhim.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "lead_discussions")
public class LeadDiscussion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lead_id", nullable = false)
    @org.hibernate.annotations.OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
    private Lead lead;

    @Column(name = "discussion_date", nullable = false)
    private LocalDateTime date;

    @Column(name = "channel")
    private String channel = "Phone Call";

    @Column(name = "author")
    private String author = "Admin Desk";

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "next_action")
    private String nextAction;

    @Column(name = "expected_join_date")
    private String expectedJoinDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public LeadDiscussion() {
    }

    public LeadDiscussion(Lead lead, LocalDateTime date, String channel, String author, String notes, String nextAction, String expectedJoinDate) {
        this.lead = lead;
        this.date = date != null ? date : LocalDateTime.now();
        this.channel = channel;
        this.author = author;
        this.notes = notes;
        this.nextAction = nextAction;
        this.expectedJoinDate = expectedJoinDate;
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.date == null) {
            this.date = LocalDateTime.now();
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Lead getLead() {
        return lead;
    }

    public void setLead(Lead lead) {
        this.lead = lead;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
