package com.example.prabhim.dto.workout;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonAlias;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public class WorkoutRoutineCreateRequest {

    @NotBlank(message = "Workout routine name is required")
    @JsonAlias({"routineName", "workoutRoutineName", "title"})
    private String name;

    @JsonAlias({"athleteId", "assignedAthleteId", "athlete"})
    private UUID memberId;

    private String memberCode;

    @JsonAlias({"assignedAthlete", "assignedAthleteName"})
    private String athleteName;

    @JsonAlias({"coachId", "assignedCoachId"})
    private UUID trainerId;

    @JsonAlias({"coach", "assignedCoach", "coachName"})
    private String coachName;

    @JsonAlias({"goal", "goalDescription"})
    private String trainingGoal;

    @JsonAlias({"programDuration", "duration", "durationInWeeks"})
    private Integer durationWeeks;

    private String status = "ACTIVE";

    @JsonAlias({"coachingInstructions", "notes", "description"})
    private String instructions;

    @JsonAlias({"prescribedExercises", "exerciseRoster", "movements"})
    private List<@Valid WorkoutExerciseRequest> exercises = new ArrayList<>();

    public WorkoutRoutineCreateRequest() {
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

    public List<WorkoutExerciseRequest> getExercises() {
        return exercises;
    }

    public void setExercises(List<WorkoutExerciseRequest> exercises) {
        this.exercises = exercises != null ? exercises : new ArrayList<>();
    }
}
