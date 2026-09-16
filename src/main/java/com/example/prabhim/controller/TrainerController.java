package com.example.prabhim.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.prabhim.dto.ApiResponse;
import com.example.prabhim.dto.trainer.TrainerCreateRequest;
import com.example.prabhim.dto.trainer.TrainerResponse;
import com.example.prabhim.dto.trainer.TrainerUpdateRequest;
import com.example.prabhim.service.TrainerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/trainers")
@CrossOrigin(origins = "*")
public class TrainerController {

    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    // 1. Add Trainer
    @PostMapping
    public ResponseEntity<ApiResponse<TrainerResponse>> addTrainer(
            @Valid @RequestBody TrainerCreateRequest request) {
        TrainerResponse created = trainerService.createTrainer(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Trainer created successfully", created));
    }

    // 2. List All Trainers
    @GetMapping
    public ResponseEntity<ApiResponse<Page<TrainerResponse>>> getAllTrainers(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String specialization,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "trainerCode") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<TrainerResponse> trainers = trainerService.getAllTrainers(search, status, specialization, pageable);
        return ResponseEntity.ok(ApiResponse.success("Trainers retrieved successfully", trainers));
    }

    // 3. Get One Trainer Using ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TrainerResponse>> getTrainerById(@PathVariable UUID id) {
        TrainerResponse trainer = trainerService.getTrainerById(id);
        return ResponseEntity.ok(ApiResponse.success("Trainer retrieved successfully", trainer));
    }

    // 4. Edit Trainer
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TrainerResponse>> editTrainer(
            @PathVariable UUID id,
            @Valid @RequestBody TrainerUpdateRequest request) {
        TrainerResponse updated = trainerService.updateTrainer(id, request);
        return ResponseEntity.ok(ApiResponse.success("Trainer updated successfully", updated));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<TrainerResponse>> patchTrainer(
            @PathVariable UUID id,
            @Valid @RequestBody TrainerUpdateRequest request) {
        TrainerResponse updated = trainerService.updateTrainer(id, request);
        return ResponseEntity.ok(ApiResponse.success("Trainer updated successfully", updated));
    }

    // 5. Delete Trainer
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTrainer(@PathVariable UUID id) {
        trainerService.deleteTrainer(id);
        return ResponseEntity.ok(ApiResponse.success("Trainer deleted successfully", null));
    }
}
