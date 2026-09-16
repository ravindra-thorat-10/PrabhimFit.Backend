package com.example.prabhim.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.prabhim.dto.trainer.TrainerCreateRequest;
import com.example.prabhim.dto.trainer.TrainerResponse;
import com.example.prabhim.dto.trainer.TrainerStatsResponse;
import com.example.prabhim.dto.trainer.TrainerUpdateRequest;

public interface TrainerService {

    Page<TrainerResponse> getAllTrainers(String search, String status, String specialization, Pageable pageable);

    List<TrainerResponse> getActiveTrainers();

    TrainerStatsResponse getTrainerStats();

    TrainerResponse getTrainerById(UUID id);

    TrainerResponse getTrainerByCode(String code);

    TrainerResponse createTrainer(TrainerCreateRequest request);

    TrainerResponse updateTrainer(UUID id, TrainerUpdateRequest request);

    void deleteTrainer(UUID id);

    void seedDefaultTrainersIfEmpty();
}
