package com.example.prabhim.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "diet_plans")
public class DietPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "athlete_name")
    private String athleteName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    @Column(name = "coach_name")
    private String coachName;

    @Column(name = "plan_name", nullable = false)
    private String planName;

    @Column(name = "daily_calories")
    private Integer dailyCalories;

    @Column(name = "daily_water_target")
    private Double dailyWaterTarget;

    @Column(name = "breakfast_protocol", columnDefinition = "TEXT")
    private String breakfastProtocol;

    @Column(name = "lunch_protocol", columnDefinition = "TEXT")
    private String lunchProtocol;

    @Column(name = "dinner_protocol", columnDefinition = "TEXT")
    private String dinnerProtocol;

    @Column(name = "snacks_protocol", columnDefinition = "TEXT")
    private String snacksProtocol;

    @Column(name = "special_guidance", columnDefinition = "TEXT")
    private String specialGuidance;

    @Column(name = "protein_grams")
    private Integer proteinGrams;

    @Column(name = "carbs_grams")
    private Integer carbsGrams;

    @Column(name = "fat_grams")
    private Integer fatGrams;

    @Column(name = "meal_schedule", columnDefinition = "TEXT")
    private String mealSchedule;

    @Column(nullable = false)
    private String status = "ACTIVE";

    @Column(name = "assigned_by")
    private String assignedBy;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public DietPlan() {
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.updatedAt == null) {
            this.updatedAt = LocalDateTime.now();
        }
        if (this.status == null || this.status.trim().isEmpty()) {
            this.status = "ACTIVE";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
        if (member != null) {
            String fullName = (member.getFirstName() != null ? member.getFirstName() : "")
                    + " " + (member.getLastName() != null ? member.getLastName() : "");
            this.athleteName = fullName.trim();
        }
    }

    public String getAthleteName() {
        return athleteName;
    }

    public void setAthleteName(String athleteName) {
        this.athleteName = athleteName;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
        if (trainer != null) {
            this.coachName = trainer.getFullName();
            this.assignedBy = trainer.getFullName();
        }
    }

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
        if (this.assignedBy == null || this.assignedBy.trim().isEmpty()) {
            this.assignedBy = coachName;
        }
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
