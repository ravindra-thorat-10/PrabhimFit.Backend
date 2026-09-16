package com.example.prabhim.dto.workout;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.example.prabhim.entity.WorkoutRoutine;

public class WorkoutRoutineResponse {

    private UUID id;
    private String name;
    private UUID memberId;
    private String athleteName;
    private String memberCode;
    private UUID trainerId;
    private String coachName;
    private String trainingGoal;
    private Integer durationWeeks;
    private String durationDisplay;
    private String status;
    private String instructions;
    private Integer exerciseCount;
    private List<WorkoutExerciseResponse> exercises = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public WorkoutRoutineResponse() {
    }

    public static WorkoutRoutineResponse fromEntity(WorkoutRoutine entity) {
        if (entity == null) {
            return null;
        }
        WorkoutRoutineResponse resp = new WorkoutRoutineResponse();
        resp.setId(entity.getId());
        resp.setName(entity.getName());
        resp.setTrainingGoal(entity.getTrainingGoal());
        resp.setDurationWeeks(entity.getDurationWeeks());
        if (entity.getDurationWeeks() != null) {
            resp.setDurationDisplay(entity.getDurationWeeks() + " Weeks Duration");
        }
        resp.setStatus(entity.getStatus() != null ? entity.getStatus() : "ACTIVE");
        resp.setInstructions(entity.getInstructions());

        if (entity.getMember() != null) {
            resp.setMemberId(entity.getMember().getId());
            resp.setMemberCode(entity.getMember().getMemberCode());
            String fullName = (entity.getMember().getFirstName() != null ? entity.getMember().getFirstName() : "")
                    + " " + (entity.getMember().getLastName() != null ? entity.getMember().getLastName() : "");
            resp.setAthleteName(fullName.trim());
        } else {
            resp.setAthleteName(entity.getAthleteName());
        }

        if (entity.getTrainer() != null) {
            resp.setTrainerId(entity.getTrainer().getId());
            resp.setCoachName(entity.getTrainer().getFullName());
        } else {
            resp.setCoachName(entity.getCoachName());
        }

        if (entity.getExercises() != null) {
            resp.setExercises(entity.getExercises().stream()
                    .map(WorkoutExerciseResponse::fromEntity)
                    .collect(Collectors.toList()));
            resp.setExerciseCount(entity.getExercises().size());
        } else {
            resp.setExerciseCount(0);
        }

        resp.setCreatedAt(entity.getCreatedAt());
        resp.setUpdatedAt(entity.getUpdatedAt());
        return resp;
    }

    // Getters and Setters
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

    public UUID getMemberId() {
        return memberId;
    }

    public void setMemberId(UUID memberId) {
        this.memberId = memberId;
    }

    public String getAthleteName() {
        return athleteName;
    }

    public void setAthleteName(String athleteName) {
        this.athleteName = athleteName;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
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

    public String getTrainingGoal() {
        return trainingGoal;
    }

    public void setTrainingGoal(String trainingGoal) {
        this.trainingGoal = trainingGoal;
    }

    public Integer getDurationWeeks() {
        return durationWeeks;
    }

    public void setDurationWeeks(Integer durationWeeks) {
        this.durationWeeks = durationWeeks;
    }

    public String getDurationDisplay() {
        return durationDisplay;
    }

    public void setDurationDisplay(String durationDisplay) {
        this.durationDisplay = durationDisplay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Integer getExerciseCount() {
        return exerciseCount;
    }

    public void setExerciseCount(Integer exerciseCount) {
        this.exerciseCount = exerciseCount;
    }

    public List<WorkoutExerciseResponse> getExercises() {
        return exercises;
    }

    public void setExercises(List<WorkoutExerciseResponse> exercises) {
        this.exercises = exercises != null ? exercises : new ArrayList<>();
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
