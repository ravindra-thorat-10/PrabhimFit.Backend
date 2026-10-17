package com.example.prabhim.dto.workout;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkoutRoutineUpdateRequest {

    @JsonAlias({"routineName", "workoutRoutineName", "title", "workoutName", "name"})
    private String name;

    @JsonAlias({"athleteId", "assignedAthleteId", "athlete", "memberId"})
    private String memberId;

    private String memberCode;

    @JsonAlias({"assignedAthlete", "assignedAthleteName", "memberName", "athleteName"})
    private String athleteName;

    @JsonAlias({"coachId", "assignedCoachId", "trainerId"})
    private String trainerId;

    @JsonAlias({"coach", "assignedCoach", "coachName"})
    private String coachName;

    @JsonAlias({"goal", "goalDescription", "trainingGoal"})
    private String trainingGoal;

    @JsonAlias({"programDuration", "duration", "durationInWeeks", "durationWeeks"})
    private Integer durationWeeks;

    private String status;

    @JsonAlias({"coachingInstructions", "notes", "description", "instructions"})
    private String instructions;

    @JsonAlias({"prescribedExercises", "exerciseRoster", "movements", "exercises"})
    private List<@Valid WorkoutExerciseRequest> exercises;

    @JsonAlias({"weeklySchedule", "schedule", "split"})
    private Object weeklySchedule;

    private String tenantId;
    private String creatorEmail;
    private String gymId;

    public WorkoutRoutineUpdateRequest() {
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

    public String resolveWeeklyScheduleString() {
        if (weeklySchedule == null) return null;
        if (weeklySchedule instanceof String s) return s;
        try {
            return new ObjectMapper().writeValueAsString(weeklySchedule);
        } catch (Exception e) {
            return String.valueOf(weeklySchedule);
        }
    }

    public String resolveWeeklyScheduleJson() {
        return resolveWeeklyScheduleString();
    }

    public String getName() {
        return name;
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
        this.exercises = exercises;
    }

    public Object getWeeklySchedule() {
        return weeklySchedule;
    }

    public void setWeeklySchedule(Object weeklySchedule) {
        this.weeklySchedule = weeklySchedule;
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
