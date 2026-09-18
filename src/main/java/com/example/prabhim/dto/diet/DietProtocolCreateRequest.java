package com.example.prabhim.dto.diet;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonAlias;

import jakarta.validation.constraints.NotBlank;

public class DietProtocolCreateRequest {

    @NotBlank(message = "Diet Protocol Name is required")
    @JsonAlias({"planName", "protocolName", "dietProtocolName"})
    private String name;

    private UUID memberId;

    private String memberCode;

    @JsonAlias({"athlete", "traineeName"})
    private String athleteName;

    private UUID trainerId;

    @JsonAlias({"coach", "assignedBy", "trainerName"})
    private String coachName;

    @JsonAlias({"dailyCalories", "calories", "calorieGoal"})
    private Integer calorieTarget;

    @JsonAlias({"waterTarget", "waterTargetLiters", "dailyWater"})
    private Double dailyWaterTarget;

    @JsonAlias({"breakfast", "breakfastPlan"})
    private String breakfastProtocol;

    @JsonAlias({"lunch", "lunchPlan"})
    private String lunchProtocol;

    @JsonAlias({"dinner", "dinnerPlan"})
    private String dinnerProtocol;

    @JsonAlias({"snacks", "snacksAndPrePostWorkout", "prePostWorkoutProtocol"})
    private String snacksProtocol;

    @JsonAlias({"guidance", "specialInstructions", "nutritionalGuidance", "instructions"})
    private String specialGuidance;

    private Integer proteinGrams;
    private Integer carbsGrams;
    private Integer fatGrams;

    private String mealSchedule;
    private String status = "ACTIVE";
    private LocalDate startDate;
    private LocalDate endDate;
    private String notes;

    public DietProtocolCreateRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Double getDailyWaterTarget() {
        return dailyWaterTarget;
    }

    public void setDailyWaterTarget(Double dailyWaterTarget) {
        this.dailyWaterTarget = dailyWaterTarget;
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
}
