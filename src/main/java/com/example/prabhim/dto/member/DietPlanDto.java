package com.example.prabhim.dto.member;

import java.time.LocalDate;
import java.util.UUID;

import com.example.prabhim.entity.DietPlan;

public class DietPlanDto {

    private UUID id;
    private String planName;
    private Integer dailyCalories;
    private Integer proteinGrams;
    private Integer carbsGrams;
    private Integer fatGrams;
    private String mealSchedule;
    private String status;
    private String assignedBy;
    private LocalDate startDate;
    private LocalDate endDate;

    public DietPlanDto() {
    }

    public static DietPlanDto fromEntity(DietPlan dietPlan) {
        if (dietPlan == null) {
            return null;
        }
        DietPlanDto dto = new DietPlanDto();
        dto.setId(dietPlan.getId());
        dto.setPlanName(dietPlan.getPlanName());
        dto.setDailyCalories(dietPlan.getDailyCalories());
        dto.setProteinGrams(dietPlan.getProteinGrams());
        dto.setCarbsGrams(dietPlan.getCarbsGrams());
        dto.setFatGrams(dietPlan.getFatGrams());
        dto.setMealSchedule(dietPlan.getMealSchedule());
        dto.setStatus(dietPlan.getStatus());
        dto.setAssignedBy(dietPlan.getAssignedBy());
        dto.setStartDate(dietPlan.getStartDate());
        dto.setEndDate(dietPlan.getEndDate());
        return dto;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public Integer getDailyCalories() {
        return dailyCalories;
    }

    public void setDailyCalories(Integer dailyCalories) {
        this.dailyCalories = dailyCalories;
    }

    public Integer getProteinGrams() {
        return proteinGrams;
    }

    public void setProteinGrams(Integer proteinGrams) {
        this.proteinGrams = proteinGrams;
    }

    public Integer getCarbsGrams() {
        return carbsGrams;
    }

    public void setCarbsGrams(Integer carbsGrams) {
        this.carbsGrams = carbsGrams;
    }

    public Integer getFatGrams() {
        return fatGrams;
    }

    public void setFatGrams(Integer fatGrams) {
        this.fatGrams = fatGrams;
    }

    public String getMealSchedule() {
        return mealSchedule;
    }

    public void setMealSchedule(String mealSchedule) {
        this.mealSchedule = mealSchedule;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(String assignedBy) {
        this.assignedBy = assignedBy;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
