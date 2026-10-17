package com.example.prabhim.dto.diet;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.example.prabhim.entity.DietPlan;
import com.example.prabhim.entity.Member;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DietProtocolResponse {

    private UUID id;
    private String name;
    private String planName;
    private UUID memberId;
    private String memberCode;
    private String athleteName;
    private String memberName;
    private DietMemberSummary member;
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
    private String goal;
    private String preference;
    private Object supplements;
    private Object weeklyMealPlan;
    private String tenantId;
    private String creatorEmail;
    private String gymId;
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
        resp.setGoal(entity.getGoal());
        resp.setPreference(entity.getPreference());
        resp.setCreatedAt(entity.getCreatedAt());
        resp.setUpdatedAt(entity.getUpdatedAt());

        // Parse supplements if present
        if (entity.getSupplements() != null && !entity.getSupplements().trim().isEmpty()) {
            try {
                resp.setSupplements(new ObjectMapper().readTree(entity.getSupplements()));
            } catch (Exception e) {
                resp.setSupplements(entity.getSupplements());
            }
        }

        // Parse weekly meal plan if present
        if (entity.getWeeklyMealPlan() != null && !entity.getWeeklyMealPlan().trim().isEmpty()) {
            try {
                resp.setWeeklyMealPlan(new ObjectMapper().readTree(entity.getWeeklyMealPlan()));
            } catch (Exception e) {
                resp.setWeeklyMealPlan(entity.getWeeklyMealPlan());
            }
        }

        // Resolve Athlete / Member
        if (entity.getMember() != null) {
            Member m = entity.getMember();
            resp.setMemberId(m.getId());
            resp.setMemberCode(m.getMemberCode());
            String fullName = (m.getFirstName() != null ? m.getFirstName() : "")
                    + " " + (m.getLastName() != null ? m.getLastName() : "");
            fullName = fullName.trim();
            resp.setAthleteName(fullName);
            resp.setMemberName(fullName);
            resp.setMember(new DietMemberSummary(m.getId(), fullName, m.getMemberCode(), m.getAvatar(), m.getPhone()));
        } else {
            resp.setAthleteName(entity.getAthleteName());
            resp.setMemberName(entity.getAthleteName());
            if (entity.getAthleteName() != null) {
                resp.setMember(new DietMemberSummary(null, entity.getAthleteName(), null, null, null));
            }
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

        // Tenancy
        String email = entity.getCreatorEmail() != null ? entity.getCreatorEmail() : (entity.getTenantId() != null ? entity.getTenantId() : "");
        if (email.isEmpty()) {
            try {
                var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
                if (auth != null && auth.getName() != null && !auth.getName().equalsIgnoreCase("anonymousUser")) {
                    email = auth.getName();
                }
            } catch (Exception ignored) {}
        }
        resp.setTenantId(email);
        resp.setCreatorEmail(email);
        resp.setGymId(email);

        return resp;
    }

    public static class DietMemberSummary {
        private UUID id;
        private String fullName;
        private String name;
        private String memberCode;
        private String avatar;
        private String phone;

        public DietMemberSummary() {
        }

        public DietMemberSummary(UUID id, String fullName, String memberCode, String avatar, String phone) {
            this.id = id;
            this.fullName = fullName;
            this.name = fullName;
            this.memberCode = memberCode;
            this.avatar = avatar;
            this.phone = phone;
        }

        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
            this.name = fullName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMemberCode() {
            return memberCode;
        }

        public void setMemberCode(String memberCode) {
            this.memberCode = memberCode;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String get_id() {
        return id != null ? id.toString() : null;
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

    public String getMemberName() {
        return memberName != null ? memberName : athleteName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public DietMemberSummary getMember() {
        return member;
    }

    public void setMember(DietMemberSummary member) {
        this.member = member;
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

    public Integer getCalories() {
        return dailyCalories != null ? dailyCalories : calorieTarget;
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

    public Double getWaterIntakeLiters() {
        return dailyWaterTarget;
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

    public String getBreakfast() {
        return breakfastProtocol;
    }

    public String getLunchProtocol() {
        return lunchProtocol;
    }

    public void setLunchProtocol(String lunchProtocol) {
        this.lunchProtocol = lunchProtocol;
    }

    public String getLunch() {
        return lunchProtocol;
    }

    public String getDinnerProtocol() {
        return dinnerProtocol;
    }

    public void setDinnerProtocol(String dinnerProtocol) {
        this.dinnerProtocol = dinnerProtocol;
    }

    public String getDinner() {
        return dinnerProtocol;
    }

    public String getSnacksProtocol() {
        return snacksProtocol;
    }

    public void setSnacksProtocol(String snacksProtocol) {
        this.snacksProtocol = snacksProtocol;
    }

    public String getSnacks() {
        return snacksProtocol;
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
