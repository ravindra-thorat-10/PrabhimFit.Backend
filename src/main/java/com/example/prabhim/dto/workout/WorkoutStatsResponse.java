package com.example.prabhim.dto.workout;

public class WorkoutStatsResponse {

    private long totalRoutines;
    private long activeRoutines;
    private long totalExercises;
    private long totalAthletes;

    public WorkoutStatsResponse() {
    }

    public WorkoutStatsResponse(long totalRoutines, long activeRoutines, long totalExercises, long totalAthletes) {
        this.totalRoutines = totalRoutines;
        this.activeRoutines = activeRoutines;
        this.totalExercises = totalExercises;
        this.totalAthletes = totalAthletes;
    }

    public long getTotalRoutines() {
        return totalRoutines;
    }

    public void setTotalRoutines(long totalRoutines) {
        this.totalRoutines = totalRoutines;
    }

    public long getActiveRoutines() {
        return activeRoutines;
    }

    public void setActiveRoutines(long activeRoutines) {
        this.activeRoutines = activeRoutines;
    }

    public long getTotalExercises() {
        return totalExercises;
    }

    public void setTotalExercises(long totalExercises) {
        this.totalExercises = totalExercises;
    }

    public long getTotalAthletes() {
        return totalAthletes;
    }

    public void setTotalAthletes(long totalAthletes) {
        this.totalAthletes = totalAthletes;
    }
}
