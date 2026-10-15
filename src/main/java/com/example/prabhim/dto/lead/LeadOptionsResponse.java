package com.example.prabhim.dto.lead;

import java.util.List;

public class LeadOptionsResponse {

    private List<String> statuses;
    private List<String> sources;
    private List<String> priorities;
    private List<String> fitnessGoals;
    private List<String> workoutTimePreferences;
    private List<String> channels;

    public LeadOptionsResponse() {
    }

    public LeadOptionsResponse(List<String> statuses, List<String> sources, List<String> priorities,
                               List<String> fitnessGoals, List<String> workoutTimePreferences, List<String> channels) {
        this.statuses = statuses;
        this.sources = sources;
        this.priorities = priorities;
        this.fitnessGoals = fitnessGoals;
        this.workoutTimePreferences = workoutTimePreferences;
        this.channels = channels;
    }

    public List<String> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<String> statuses) {
        this.statuses = statuses;
    }

    public List<String> getSources() {
        return sources;
    }

    public void setSources(List<String> sources) {
        this.sources = sources;
    }

    public List<String> getPriorities() {
        return priorities;
    }

    public void setPriorities(List<String> priorities) {
        this.priorities = priorities;
    }

    public List<String> getFitnessGoals() {
        return fitnessGoals;
    }

    public void setFitnessGoals(List<String> fitnessGoals) {
        this.fitnessGoals = fitnessGoals;
    }

    public List<String> getWorkoutTimePreferences() {
        return workoutTimePreferences;
    }

    public void setWorkoutTimePreferences(List<String> workoutTimePreferences) {
        this.workoutTimePreferences = workoutTimePreferences;
    }

    public List<String> getChannels() {
        return channels;
    }

    public void setChannels(List<String> channels) {
        this.channels = channels;
    }
}
