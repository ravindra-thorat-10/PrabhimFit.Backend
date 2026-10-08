package com.example.prabhim.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum LeadSource {
    WALK_IN("Walk-in Visitor"),
    WEBSITE("Website"),
    REFERRAL("Referral"),
    SOCIAL_MEDIA("Social Media / Instagram"),
    WHATSAPP("WhatsApp Inquiry"),
    PHONE_INQUIRY("Phone Call / Direct"),
    GOOGLE("Google Search / Maps"),
    CAMPAIGN("Campaign / Advertisement"),
    OTHER("Other");

    private final String displayName;

    LeadSource(String displayName) {
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
    public static LeadSource fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return WALK_IN;
        }
        String normalized = value.trim().toUpperCase().replace(" ", "_").replace("-", "_").replace("/", "_");
        if (normalized.contains("WALK") || normalized.contains("VISITOR")) {
            return WALK_IN;
        }
        if (normalized.contains("WEB")) {
            return WEBSITE;
        }
        if (normalized.contains("REFERRAL") || normalized.contains("FRIEND")) {
            return REFERRAL;
        }
        if (normalized.contains("SOCIAL") || normalized.contains("INSTA") || normalized.contains("FB") || normalized.contains("FACEBOOK")) {
            return SOCIAL_MEDIA;
        }
        if (normalized.contains("WHATSAPP")) {
            return WHATSAPP;
        }
        if (normalized.contains("PHONE") || normalized.contains("CALL")) {
            return PHONE_INQUIRY;
        }
        if (normalized.contains("GOOGLE") || normalized.contains("MAPS")) {
            return GOOGLE;
        }
        if (normalized.contains("CAMPAIGN") || normalized.contains("AD")) {
            return CAMPAIGN;
        }
        for (LeadSource source : LeadSource.values()) {
            if (source.name().equalsIgnoreCase(normalized) || 
                source.displayName.equalsIgnoreCase(value.trim())) {
                return source;
            }
        }
        return OTHER;
    }
}
