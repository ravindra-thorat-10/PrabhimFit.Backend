package com.example.prabhim.controller;

import java.util.List;
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
import com.example.prabhim.dto.trainer.TrainerStatsResponse;
import com.example.prabhim.dto.trainer.TrainerUpdateRequest;
import com.example.prabhim.service.TrainerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping({"/api/v1/trainers", "/api/v1/trainers/"})
@CrossOrigin(origins = "*")
public class TrainerController {

    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    // 1. Add Trainer
    @PostMapping({"", "/"})
    public ResponseEntity<ApiResponse<TrainerResponse>> addTrainer(
            @Valid @RequestBody TrainerCreateRequest request) {
        TrainerResponse created = trainerService.createTrainer(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Trainer created successfully", created));
    }

    // 2. List All Trainers
    @GetMapping({"", "/"})
    public ResponseEntity<ApiResponse<Page<TrainerResponse>>> getAllTrainers(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) String tenantId,
            @RequestParam(required = false) String creatorEmail,
            @RequestParam(required = false) String gymId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
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

    // 3. Trainer Stats - Placed before /{id} to prevent UUID parse error
    @GetMapping({"/stats", "/stats/"})
    public ResponseEntity<ApiResponse<TrainerStatsResponse>> getTrainerStats() {
        TrainerStatsResponse stats = trainerService.getTrainerStats();
        return ResponseEntity.ok(ApiResponse.success("Trainer statistics retrieved successfully", stats));
    }

    // 4. Active Trainers List
    @GetMapping({"/active", "/active/"})
    public ResponseEntity<ApiResponse<List<TrainerResponse>>> getActiveTrainers() {
        List<TrainerResponse> active = trainerService.getActiveTrainers();
        return ResponseEntity.ok(ApiResponse.success("Active trainers retrieved successfully", active));
    }

    // 5. Get Trainer By Code
    @GetMapping({"/code/{code}", "/code/{code}/"})
    public ResponseEntity<ApiResponse<TrainerResponse>> getTrainerByCode(@PathVariable String code) {
        TrainerResponse trainer = trainerService.getTrainerByCode(code);
        return ResponseEntity.ok(ApiResponse.success("Trainer retrieved successfully", trainer));
    }

    // 6. Get One Trainer Using ID
    @GetMapping({"/{id}", "/{id}/"})
    public ResponseEntity<ApiResponse<TrainerResponse>> getTrainerById(@PathVariable UUID id) {
        TrainerResponse trainer = trainerService.getTrainerById(id);
        return ResponseEntity.ok(ApiResponse.success("Trainer retrieved successfully", trainer));
    }

    // 7. Edit Trainer
    @PutMapping({"/{id}", "/{id}/"})
    public ResponseEntity<ApiResponse<TrainerResponse>> editTrainer(
            @PathVariable UUID id,
            @Valid @RequestBody TrainerUpdateRequest request) {
        TrainerResponse updated = trainerService.updateTrainer(id, request);
        return ResponseEntity.ok(ApiResponse.success("Trainer updated successfully", updated));
    }

    @PatchMapping({"/{id}", "/{id}/"})
    public ResponseEntity<ApiResponse<TrainerResponse>> patchTrainer(
            @PathVariable UUID id,
            @Valid @RequestBody TrainerUpdateRequest request) {
        TrainerResponse updated = trainerService.updateTrainer(id, request);
        return ResponseEntity.ok(ApiResponse.success("Trainer updated successfully", updated));
    }

    // 8. Delete Trainer
    @DeleteMapping({"/{id}", "/{id}/"})
    public ResponseEntity<ApiResponse<Void>> deleteTrainer(@PathVariable UUID id) {
        trainerService.deleteTrainer(id);
        return ResponseEntity.ok(ApiResponse.success("Trainer deleted successfully", null));
    }
}
