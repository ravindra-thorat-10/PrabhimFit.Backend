package com.example.prabhim.dto.member;

import java.time.LocalDate;
import java.util.UUID;

import com.example.prabhim.entity.BodyMeasurement;

public class BodyMeasurementDto {

    private UUID id;
    private LocalDate recordedDate;
    private Double weightKg;
    private Double heightCm;
    private Double bodyFatPercentage;
    private Double chestCm;
    private Double waistCm;
    private Double hipsCm;
    private Double bicepsCm;
    private String notes;

    public BodyMeasurementDto() {
    }

    public static BodyMeasurementDto fromEntity(BodyMeasurement measurement) {
        if (measurement == null) {
            return null;
        }
        BodyMeasurementDto dto = new BodyMeasurementDto();
        dto.setId(measurement.getId());
        dto.setRecordedDate(measurement.getRecordedDate());
        dto.setWeightKg(measurement.getWeightKg());
        dto.setHeightCm(measurement.getHeightCm());
        dto.setBodyFatPercentage(measurement.getBodyFatPercentage());
        dto.setChestCm(measurement.getChestCm());
        dto.setWaistCm(measurement.getWaistCm());
        dto.setHipsCm(measurement.getHipsCm());
        dto.setBicepsCm(measurement.getBicepsCm());
        dto.setNotes(measurement.getNotes());
        return dto;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getRecordedDate() {
        return recordedDate;
    }

    public void setRecordedDate(LocalDate recordedDate) {
        this.recordedDate = recordedDate;
    }

    public Double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(Double weightKg) {
        this.weightKg = weightKg;
    }

    public Double getHeightCm() {
        return heightCm;
    }

    public void setHeightCm(Double heightCm) {
        this.heightCm = heightCm;
    }

    public Double getBodyFatPercentage() {
        return bodyFatPercentage;
    }

    public void setBodyFatPercentage(Double bodyFatPercentage) {
        this.bodyFatPercentage = bodyFatPercentage;
    }

    public Double getChestCm() {
        return chestCm;
    }

    public void setChestCm(Double chestCm) {
        this.chestCm = chestCm;
    }

    public Double getWaistCm() {
        return waistCm;
    }

    public void setWaistCm(Double waistCm) {
        this.waistCm = waistCm;
    }

    public Double getHipsCm() {
        return hipsCm;
    }

    public void setHipsCm(Double hipsCm) {
        this.hipsCm = hipsCm;
    }

    public Double getBicepsCm() {
        return bicepsCm;
    }

    public void setBicepsCm(Double bicepsCm) {
        this.bicepsCm = bicepsCm;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
