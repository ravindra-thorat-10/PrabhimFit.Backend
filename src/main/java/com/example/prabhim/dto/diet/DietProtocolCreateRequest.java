package com.example.prabhim.dto.diet;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DietProtocolCreateRequest {

    @JsonAlias({"planName", "protocolName", "dietProtocolName", "title", "name"})
    private String name;

    @JsonAlias({"athleteId", "assignedAthleteId", "athlete", "memberId"})
    private String memberId;

    private String memberCode;

    @JsonAlias({"athlete", "traineeName", "memberName", "assignedAthlete", "athleteName"})
    private String athleteName;

    @JsonAlias({"coachId", "assignedCoachId", "trainerId"})
    private String trainerId;

    @JsonAlias({"coach", "assignedBy", "trainerName", "assignedCoach", "coachName"})
    private String coachName;

    @JsonAlias({"dailyCalories", "calories", "calorieGoal", "calorieTarget"})
    private Integer calorieTarget;

    @JsonAlias({"waterTarget", "waterTargetLiters", "dailyWater", "waterIntakeLiters", "dailyWaterTarget"})
    private Double dailyWaterTarget;

    @JsonAlias({"breakfast", "breakfastPlan", "breakfastProtocol"})
    private String breakfastProtocol;

    @JsonAlias({"lunch", "lunchPlan", "lunchProtocol"})
    private String lunchProtocol;

    @JsonAlias({"dinner", "dinnerPlan", "dinnerProtocol"})
    private String dinnerProtocol;

    @JsonAlias({"snacks", "snacksAndPrePostWorkout", "prePostWorkoutProtocol", "snacksProtocol"})
    private String snacksProtocol;

    @JsonAlias({"guidance", "specialInstructions", "nutritionalGuidance", "instructions", "notes", "specialGuidance"})
    private String specialGuidance;

    private Integer proteinGrams;
    private Integer carbsGrams;
    private Integer fatGrams;

    private String mealSchedule;
    private String status = "ACTIVE";
    private LocalDate startDate;
    private LocalDate endDate;
    private String notes;

    @JsonAlias({"targetGoal", "dietGoal", "goal"})
    private String goal;

    @JsonAlias({"dietaryPreference", "dietPreference", "preference"})
    private String preference;

    @JsonAlias({"supplementStack", "supplementList", "supplements"})
    private Object supplements;

    @JsonAlias({"weeklySchedule", "mealPlan7Day", "weeklyPlan", "weeklyMealPlan"})
    private Object weeklyMealPlan;

    private String tenantId;
    private String creatorEmail;
    private String gymId;

    public DietProtocolCreateRequest() {
    }

    public UUID resolveMemberId() {
        if (memberId == null || memberId.trim().isEmpty()) return null;
        try {
            return UUID.fromString(memberId.trim());
        } catch (Exception e) {
            return null;
        }
    }

    public UUID resolveTrainerId() {
        if (trainerId == null || trainerId.trim().isEmpty()) return null;
        try {
            return UUID.fromString(trainerId.trim());
        } catch (Exception e) {
            return null;
        }
    }

    public String resolveSupplementsJson() {
        if (supplements == null) return null;
        if (supplements instanceof String s) return s;
        try {
            return new ObjectMapper().writeValueAsString(supplements);
        } catch (Exception e) {
            return String.valueOf(supplements);
        }
    }

    public String resolveWeeklyMealPlanJson() {
        if (weeklyMealPlan == null) return null;
        if (weeklyMealPlan instanceof String s) return s;
        try {
            return new ObjectMapper().writeValueAsString(weeklyMealPlan);
        } catch (Exception e) {
            return String.valueOf(weeklyMealPlan);
        }
    }

    public String getName() {
        if (name != null && !name.trim().isEmpty()) {
            return name.trim();
        }
        if (goal != null && !goal.trim().isEmpty()) {
            return goal.trim();
        }
        return "Nutritional Protocol";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
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

    public String getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(String trainerId) {
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

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getPreference() {
        return preference;
    }

    public void setPreference(String preference) {
        this.preference = preference;
    }

    public Object getSupplements() {
        return supplements;
    }

    public void setSupplements(Object supplements) {
        this.supplements = supplements;
    }

    public Object getWeeklyMealPlan() {
        return weeklyMealPlan;
    }

    public void setWeeklyMealPlan(Object weeklyMealPlan) {
        this.weeklyMealPlan = weeklyMealPlan;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getCreatorEmail() {
        return creatorEmail;
    }

    public void setCreatorEmail(String creatorEmail) {
        this.creatorEmail = creatorEmail;
    }

    public String getGymId() {
        return gymId;
    }

    public void setGymId(String gymId) {
        this.gymId = gymId;
    }
}
