package com.example.prabhim.dto.member;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.prabhim.entity.MemberActivityLog;

public class MemberActivityLogDto {

    private UUID id;
    private String activityType;
    private String description;
    private String performedBy;
    private LocalDateTime timestamp;

    public MemberActivityLogDto() {
    }

    public static MemberActivityLogDto fromEntity(MemberActivityLog log) {
        if (log == null) {
            return null;
        }
        MemberActivityLogDto dto = new MemberActivityLogDto();
        dto.setId(log.getId());
        dto.setActivityType(log.getActivityType());
        dto.setDescription(log.getDescription());
        dto.setPerformedBy(log.getPerformedBy());
        dto.setTimestamp(log.getTimestamp());
        return dto;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(String performedBy) {
        this.performedBy = performedBy;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
