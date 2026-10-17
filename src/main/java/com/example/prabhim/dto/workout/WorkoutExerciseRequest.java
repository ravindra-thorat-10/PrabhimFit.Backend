package com.example.prabhim.dto.workout;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkoutExerciseRequest {

    private String id;

    @NotBlank(message = "Exercise name is required")
    @JsonAlias({"name", "title", "exerciseName"})
    private String exerciseName;

    @NotNull(message = "Sets is required")
    @Min(value = 1, message = "Sets must be at least 1")
    private Integer sets = 3;

    @NotBlank(message = "Reps is required")
    @JsonAlias({"repsRange", "repetitions"})
    private String reps = "10-12";

    @JsonAlias({"weight", "load", "prescribedLoad", "targetWeight"})
    private String targetWeight;

    @JsonAlias({"restTime", "rest"})
    private String restInterval;

    private Integer orderIndex;

    @JsonAlias({"instructions", "instruction"})
    private String notes;

    public WorkoutExerciseRequest() {
    }

    public WorkoutExerciseRequest(String exerciseName, Integer sets, String reps, String targetWeight, Integer orderIndex) {
        this.exerciseName = exerciseName;
        this.sets = sets;
        this.reps = reps;
        this.targetWeight = targetWeight;
        this.orderIndex = orderIndex;
    }

    public UUID resolveId() {
        if (id == null || id.trim().isEmpty()) return null;
        try {
            return UUID.fromString(id.trim());
        } catch (Exception e) {
            return null;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public Integer getSets() {
        return sets;
    }

    public void setSets(Integer sets) {
        this.sets = sets;
    }

    public String getReps() {
        return reps;
    }

    public void setReps(String reps) {
        this.reps = reps;
    }

    public String getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(String targetWeight) {
        this.targetWeight = targetWeight;
    }

    public String getRestInterval() {
        return restInterval;
    }

    public void setRestInterval(String restInterval) {
        this.restInterval = restInterval;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
