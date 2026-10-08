package com.example.prabhim.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentModePreference {
    UPI("UPI"),
    CASH("Cash"),
    CARD("Credit / Debit Card"),
    NET_BANKING("Net Banking"),
    OTHER("Other");

    private final String displayName;

    PaymentModePreference(String displayName) {
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
    public static PaymentModePreference fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return UPI;
        }
        String normalized = value.trim().toUpperCase().replace(" ", "_").replace("/", "_").replace("-", "_");
        if (normalized.contains("UPI") || normalized.contains("GPAY") || normalized.contains("PHONEPE")) {
            return UPI;
        }
        if (normalized.contains("CASH")) {
            return CASH;
        }
        if (normalized.contains("CARD") || normalized.contains("CREDIT") || normalized.contains("DEBIT") || normalized.contains("POS")) {
            return CARD;
        }
        if (normalized.contains("NET") || normalized.contains("BANK") || normalized.contains("ONLINE")) {
            return NET_BANKING;
        }
        for (PaymentModePreference mode : PaymentModePreference.values()) {
            if (mode.name().equalsIgnoreCase(normalized) || 
                mode.displayName.equalsIgnoreCase(value.trim())) {
                return mode;
            }
        }
        return OTHER;
    }
}
