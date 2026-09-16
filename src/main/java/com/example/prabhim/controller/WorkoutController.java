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
import com.example.prabhim.dto.workout.WorkoutExerciseRequest;
import com.example.prabhim.dto.workout.WorkoutRoutineCreateRequest;
import com.example.prabhim.dto.workout.WorkoutRoutineResponse;
import com.example.prabhim.dto.workout.WorkoutRoutineUpdateRequest;
import com.example.prabhim.service.WorkoutRoutineService;

import jakarta.validation.Valid;

@RestController
@RequestMapping({"/api/v1/workouts", "/api/v1/workout-routines", "/api/v1/workout-programs"})
@CrossOrigin(origins = "*")
public class WorkoutController {

    private final WorkoutRoutineService workoutRoutineService;

    public WorkoutController(WorkoutRoutineService workoutRoutineService) {
        this.workoutRoutineService = workoutRoutineService;
    }

    // 1. Create & Assign Workout Routine
    @PostMapping({"", "/"})
    public ResponseEntity<ApiResponse<WorkoutRoutineResponse>> createWorkoutRoutine(
            @Valid @RequestBody WorkoutRoutineCreateRequest request
    ) {
        WorkoutRoutineResponse created = workoutRoutineService.createWorkoutRoutine(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Workout routine created and assigned successfully", created));
    }

    // 2. List All Workout Routines
    @GetMapping({"", "/"})
    public ResponseEntity<ApiResponse<Page<WorkoutRoutineResponse>>> getAllWorkoutRoutines(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) UUID memberId,
            @RequestParam(required = false) UUID trainerId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<WorkoutRoutineResponse> routines = workoutRoutineService.getAllWorkoutRoutines(
                search, memberId, trainerId, status, pageable
        );
        return ResponseEntity.ok(ApiResponse.success("Workout routines retrieved successfully", routines));
    }

    // 3. Get One Workout Routine by ID (Full Routine)
    @GetMapping({"/{id}", "/{id}/"})
    public ResponseEntity<ApiResponse<WorkoutRoutineResponse>> getWorkoutRoutineById(@PathVariable UUID id) {
        WorkoutRoutineResponse routine = workoutRoutineService.getWorkoutRoutineById(id);
        return ResponseEntity.ok(ApiResponse.success("Workout routine retrieved successfully", routine));
    }

    // 4. Edit / Update Workout Routine
    @PutMapping({"/{id}", "/{id}/"})
    public ResponseEntity<ApiResponse<WorkoutRoutineResponse>> updateWorkoutRoutine(
            @PathVariable UUID id,
            @RequestBody WorkoutRoutineUpdateRequest request
    ) {
        WorkoutRoutineResponse updated = workoutRoutineService.updateWorkoutRoutine(id, request);
        return ResponseEntity.ok(ApiResponse.success("Workout routine updated successfully", updated));
    }

    @PatchMapping({"/{id}", "/{id}/"})
    public ResponseEntity<ApiResponse<WorkoutRoutineResponse>> patchWorkoutRoutine(
            @PathVariable UUID id,
            @RequestBody WorkoutRoutineUpdateRequest request
    ) {
        WorkoutRoutineResponse updated = workoutRoutineService.updateWorkoutRoutine(id, request);
        return ResponseEntity.ok(ApiResponse.success("Workout routine updated successfully", updated));
    }

    // 5. Delete Workout Routine
    @DeleteMapping({"/{id}", "/{id}/"})
    public ResponseEntity<ApiResponse<Void>> deleteWorkoutRoutine(@PathVariable UUID id) {
        workoutRoutineService.deleteWorkoutRoutine(id);
        return ResponseEntity.ok(ApiResponse.success("Workout routine deleted successfully", null));
    }

    // 6. Add Single Exercise Movement to Routine
    @PostMapping({"/{id}/exercises", "/{id}/exercises/"})
    public ResponseEntity<ApiResponse<WorkoutRoutineResponse>> addExercise(
            @PathVariable UUID id,
            @Valid @RequestBody WorkoutExerciseRequest exerciseRequest
    ) {
        WorkoutRoutineResponse updated = workoutRoutineService.addExercise(id, exerciseRequest);
        return ResponseEntity.ok(ApiResponse.success("Exercise added to routine successfully", updated));
    }

    // 7. Remove Single Exercise Movement from Routine
    @DeleteMapping({"/{id}/exercises/{exerciseId}", "/{id}/exercises/{exerciseId}/"})
    public ResponseEntity<ApiResponse<WorkoutRoutineResponse>> removeExercise(
            @PathVariable UUID id,
            @PathVariable UUID exerciseId
    ) {
        WorkoutRoutineResponse updated = workoutRoutineService.removeExercise(id, exerciseId);
        return ResponseEntity.ok(ApiResponse.success("Exercise removed from routine successfully", updated));
    }
}
