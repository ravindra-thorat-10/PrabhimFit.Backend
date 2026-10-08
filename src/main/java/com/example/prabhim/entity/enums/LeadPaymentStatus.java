package com.example.prabhim.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum LeadPaymentStatus {
    PENDING("Pending"),
    PARTIAL("Partial"),
    PAID("Paid");

    private final String displayName;

    LeadPaymentStatus(String displayName) {
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
    public static LeadPaymentStatus fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return PENDING;
        }
        String normalized = value.trim().toUpperCase();
        for (LeadPaymentStatus status : LeadPaymentStatus.values()) {
            if (status.name().equalsIgnoreCase(normalized) || 
                status.displayName.equalsIgnoreCase(value.trim())) {
                return status;
            }
        }
        return PENDING;
    }
}
