package com.example.prabhim.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum LeadStatus {
    NEW("New"),
    CONTACTED("Contacted"),
    TRIAL_SCHEDULED("Trial Scheduled"),
    TRIAL_COMPLETED("Trial Completed"),
    NEGOTIATION("Negotiation"),
    WON("Won"),
    LOST("Lost"),
    ACTION_REQUIRED("Action Required"),
    CONVERTED("Converted");

    private final String displayName;

    LeadStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @JsonValue
    public String toValue() {
        return name();
    }

    @JsonCreator
    public static LeadStatus fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return NEW;
        }
        String normalized = value.trim().toUpperCase().replace(" ", "_").replace("-", "_");
        for (LeadStatus status : LeadStatus.values()) {
            if (status.name().equalsIgnoreCase(normalized) || 
                status.displayName.equalsIgnoreCase(value.trim())) {
                return status;
            }
        }
        return NEW;
    }
}
