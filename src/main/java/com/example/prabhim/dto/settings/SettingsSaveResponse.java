package com.example.prabhim.dto.settings;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SettingsSaveResponse {

    private String message;

    @JsonProperty("updatedAt")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime updatedAt;

    private SettingsDTO settings;

    public SettingsSaveResponse() {
    }

    public SettingsSaveResponse(String message, LocalDateTime updatedAt) {
        this.message = message;
        this.updatedAt = updatedAt;
    }

    public SettingsSaveResponse(String message, LocalDateTime updatedAt, SettingsDTO settings) {
        this.message = message;
        this.updatedAt = updatedAt;
        this.settings = settings;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public SettingsDTO getSettings() {
        return settings;
    }

    public void setSettings(SettingsDTO settings) {
        this.settings = settings;
    }
}
