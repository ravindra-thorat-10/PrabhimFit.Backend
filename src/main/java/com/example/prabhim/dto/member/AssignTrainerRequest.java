package com.example.prabhim.dto.member;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AssignTrainerRequest {

    private UUID trainerId;

    public AssignTrainerRequest() {
    }

    public AssignTrainerRequest(UUID trainerId) {
        this.trainerId = trainerId;
    }

    public UUID getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(UUID trainerId) {
        this.trainerId = trainerId;
    }
}
