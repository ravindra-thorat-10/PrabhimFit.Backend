package com.example.prabhim.dto.workout;

import java.util.UUID;

import com.example.prabhim.entity.WorkoutExercise;

public class WorkoutExerciseResponse {

    private UUID id;
    private String exerciseName;
    private Integer sets;
    private String reps;
    private String targetWeight;
    private String restInterval;
    private Integer orderIndex;
    private String notes;
    private String setsRepsDisplay;

    public WorkoutExerciseResponse() {
    }

    public static WorkoutExerciseResponse fromEntity(WorkoutExercise entity) {
        if (entity == null) {
            return null;
        }
        WorkoutExerciseResponse resp = new WorkoutExerciseResponse();
        resp.setId(entity.getId());
        resp.setExerciseName(entity.getExerciseName());
        resp.setSets(entity.getSets());
        resp.setReps(entity.getReps());
        resp.setTargetWeight(entity.getTargetWeight());
        resp.setRestInterval(entity.getRestInterval());
        resp.setOrderIndex(entity.getOrderIndex());
        resp.setNotes(entity.getNotes());

        if (entity.getSets() != null && entity.getReps() != null) {
            String repsStr = entity.getReps().trim();
            if (!repsStr.toLowerCase().contains("rep")) {
                resp.setSetsRepsDisplay(entity.getSets() + " Sets × " + repsStr + " Reps");
            } else {
                resp.setSetsRepsDisplay(entity.getSets() + " Sets × " + repsStr);
            }
        }
        return resp;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    public String getSetsRepsDisplay() {
        return setsRepsDisplay;
    }

    public void setSetsRepsDisplay(String setsRepsDisplay) {
        this.setsRepsDisplay = setsRepsDisplay;
    }
}
