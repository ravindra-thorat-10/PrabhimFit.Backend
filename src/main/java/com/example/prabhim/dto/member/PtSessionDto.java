package com.example.prabhim.dto.member;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.prabhim.entity.PtSession;
import com.example.prabhim.entity.enums.PtSessionStatus;

public class PtSessionDto {

    private UUID id;
    private UUID trainerId;
    private String trainerName;
    private LocalDateTime sessionDate;
    private String sessionType;
    private Integer durationMinutes;
    private PtSessionStatus status;
    private String notes;

    public PtSessionDto() {
    }

    public static PtSessionDto fromEntity(PtSession session) {
        if (session == null) {
            return null;
        }
        PtSessionDto dto = new PtSessionDto();
        dto.setId(session.getId());
        if (session.getTrainer() != null) {
            dto.setTrainerId(session.getTrainer().getId());
            dto.setTrainerName(session.getTrainer().getFirstName() + " " + (session.getTrainer().getLastName() != null ? session.getTrainer().getLastName() : "").trim());
        } else {
            dto.setTrainerName(session.getTrainerName());
        }
        dto.setSessionDate(session.getSessionDate());
        dto.setSessionType(session.getSessionType());
        dto.setDurationMinutes(session.getDurationMinutes());
        dto.setStatus(session.getStatus());
        dto.setNotes(session.getNotes());
        return dto;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(UUID trainerId) {
        this.trainerId = trainerId;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }

    public LocalDateTime getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(LocalDateTime sessionDate) {
        this.sessionDate = sessionDate;
    }

    public String getSessionType() {
        return sessionType;
    }

    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public PtSessionStatus getStatus() {
        return status;
    }

    public void setStatus(PtSessionStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
