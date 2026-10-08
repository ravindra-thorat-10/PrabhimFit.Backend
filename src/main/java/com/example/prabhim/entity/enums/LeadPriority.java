package com.example.prabhim.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum LeadPriority {
    HOT("Hot"),
    HIGH("High"),
    MEDIUM("Med"),
    LOW("Low");

    private final String displayName;

    LeadPriority(String displayName) {
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
    public static LeadPriority fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return HIGH;
        }
        String normalized = value.trim().toUpperCase();
        if (normalized.equals("MED") || normalized.equals("MEDIUM")) {
            return MEDIUM;
        }
        if (normalized.equals("HOT")) {
            return HOT;
        }
        if (normalized.equals("HIGH")) {
            return HIGH;
        }
        if (normalized.equals("LOW")) {
            return LOW;
        }
        for (LeadPriority priority : LeadPriority.values()) {
            if (priority.name().equalsIgnoreCase(normalized) || 
                priority.displayName.equalsIgnoreCase(value.trim())) {
                return priority;
            }
        }
        return HIGH;
    }
}
