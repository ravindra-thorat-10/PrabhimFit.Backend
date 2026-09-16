package com.example.prabhim.dto.member;

import java.time.LocalDate;
import java.util.UUID;

import com.example.prabhim.entity.WorkoutLog;

public class WorkoutLogDto {

    private UUID id;
    private LocalDate workoutDate;
    private String workoutType;
    private Integer durationMinutes;
    private Integer caloriesBurned;
    private String notes;

    public WorkoutLogDto() {
    }

    public static WorkoutLogDto fromEntity(WorkoutLog log) {
        if (log == null) {
            return null;
        }
        WorkoutLogDto dto = new WorkoutLogDto();
        dto.setId(log.getId());
        dto.setWorkoutDate(log.getWorkoutDate());
        dto.setWorkoutType(log.getWorkoutType());
        dto.setDurationMinutes(log.getDurationMinutes());
        dto.setCaloriesBurned(log.getCaloriesBurned());
        dto.setNotes(log.getNotes());
        return dto;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getWorkoutDate() {
        return workoutDate;
    }

    public void setWorkoutDate(LocalDate workoutDate) {
        this.workoutDate = workoutDate;
    }

    public String getWorkoutType() {
        return workoutType;
    }

    public void setWorkoutType(String workoutType) {
        this.workoutType = workoutType;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public Integer getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setCaloriesBurned(Integer caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
