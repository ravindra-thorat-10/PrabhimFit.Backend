package com.example.prabhim.dto.diet;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.example.prabhim.entity.DietPlan;

public class DietProtocolResponse {

    private UUID id;
    private String name;
    private String planName;
    private UUID memberId;
    private String memberCode;
    private String athleteName;
    private UUID trainerId;
    private String coachName;
    private Integer calorieTarget;
    private Integer dailyCalories;
    private String calorieDisplay;
    private Double dailyWaterTarget;
    private String waterDisplay;
    private String breakfastProtocol;
    private String lunchProtocol;
    private String dinnerProtocol;
    private String snacksProtocol;
    private String specialGuidance;
    private Integer proteinGrams;
    private Integer carbsGrams;
    private Integer fatGrams;
    private String mealSchedule;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public DietProtocolResponse() {
    }

    public static DietProtocolResponse fromEntity(DietPlan entity) {
        if (entity == null) {
            return null;
        }
        DietProtocolResponse resp = new DietProtocolResponse();
        resp.setId(entity.getId());
        resp.setName(entity.getPlanName());
        resp.setPlanName(entity.getPlanName());

        resp.setCalorieTarget(entity.getDailyCalories());
        resp.setDailyCalories(entity.getDailyCalories());
        if (entity.getDailyCalories() != null) {
            resp.setCalorieDisplay(entity.getDailyCalories() + " KCAL");
        }

        resp.setDailyWaterTarget(entity.getDailyWaterTarget());
        if (entity.getDailyWaterTarget() != null) {
            double water = entity.getDailyWaterTarget();
            if (water == Math.floor(water)) {
                resp.setWaterDisplay(String.format("%.0fL WATER", water));
            } else {
                resp.setWaterDisplay(String.format("%.1fL WATER", water));
            }
        }

        resp.setBreakfastProtocol(entity.getBreakfastProtocol());
        resp.setLunchProtocol(entity.getLunchProtocol());
        resp.setDinnerProtocol(entity.getDinnerProtocol());
        resp.setSnacksProtocol(entity.getSnacksProtocol());
        resp.setSpecialGuidance(entity.getSpecialGuidance());
        resp.setProteinGrams(entity.getProteinGrams());
        resp.setCarbsGrams(entity.getCarbsGrams());
        resp.setFatGrams(entity.getFatGrams());
        resp.setMealSchedule(entity.getMealSchedule());
        resp.setStatus(entity.getStatus() != null ? entity.getStatus() : "ACTIVE");
        resp.setStartDate(entity.getStartDate());
        resp.setEndDate(entity.getEndDate());
        resp.setNotes(entity.getNotes());
        resp.setCreatedAt(entity.getCreatedAt());
        resp.setUpdatedAt(entity.getUpdatedAt());

        // Resolve Athlete / Member
        if (entity.getMember() != null) {
            resp.setMemberId(entity.getMember().getId());
            resp.setMemberCode(entity.getMember().getMemberCode());
            String fullName = (entity.getMember().getFirstName() != null ? entity.getMember().getFirstName() : "")
                    + " " + (entity.getMember().getLastName() != null ? entity.getMember().getLastName() : "");
            resp.setAthleteName(fullName.trim());
        } else {
            resp.setAthleteName(entity.getAthleteName());
        }

        // Resolve Coach / Trainer
        if (entity.getTrainer() != null) {
            resp.setTrainerId(entity.getTrainer().getId());
            resp.setCoachName(entity.getTrainer().getFullName());
        } else if (entity.getCoachName() != null) {
            resp.setCoachName(entity.getCoachName());
        } else {
            resp.setCoachName(entity.getAssignedBy());
        }

        return resp;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public UUID getMemberId() {
        return memberId;
    }

    public void setMemberId(UUID memberId) {
        this.memberId = memberId;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public String getAthleteName() {
        return athleteName;
    }

    public void setAthleteName(String athleteName) {
        this.athleteName = athleteName;
    }

    public UUID getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(UUID trainerId) {
        this.trainerId = trainerId;
    }

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }

    public Integer getCalorieTarget() {
        return calorieTarget;
    }

    public void setCalorieTarget(Integer calorieTarget) {
        this.calorieTarget = calorieTarget;
    }

    public Integer getDailyCalories() {
        return dailyCalories;
    }

    public void setDailyCalories(Integer dailyCalories) {
        this.dailyCalories = dailyCalories;
    }

    public String getCalorieDisplay() {
        return calorieDisplay;
    }

    public void setCalorieDisplay(String calorieDisplay) {
        this.calorieDisplay = calorieDisplay;
    }

    public Double getDailyWaterTarget() {
        return dailyWaterTarget;
    }

    public void setDailyWaterTarget(Double dailyWaterTarget) {
        this.dailyWaterTarget = dailyWaterTarget;
    }

    public String getWaterDisplay() {
        return waterDisplay;
    }

    public void setWaterDisplay(String waterDisplay) {
        this.waterDisplay = waterDisplay;
    }

    public String getBreakfastProtocol() {
        return breakfastProtocol;
    }

    public void setBreakfastProtocol(String breakfastProtocol) {
        this.breakfastProtocol = breakfastProtocol;
    }

    public String getLunchProtocol() {
        return lunchProtocol;
    }

    public void setLunchProtocol(String lunchProtocol) {
        this.lunchProtocol = lunchProtocol;
    }

    public String getDinnerProtocol() {
        return dinnerProtocol;
    }

    public void setDinnerProtocol(String dinnerProtocol) {
        this.dinnerProtocol = dinnerProtocol;
    }

    public String getSnacksProtocol() {
        return snacksProtocol;
    }

    public void setSnacksProtocol(String snacksProtocol) {
        this.snacksProtocol = snacksProtocol;
    }

    public String getSpecialGuidance() {
        return specialGuidance;
    }

    public void setSpecialGuidance(String specialGuidance) {
        this.specialGuidance = specialGuidance;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
