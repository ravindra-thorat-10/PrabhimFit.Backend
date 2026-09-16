package com.example.prabhim.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.prabhim.dto.workout.WorkoutExerciseRequest;
import com.example.prabhim.dto.workout.WorkoutRoutineCreateRequest;
import com.example.prabhim.dto.workout.WorkoutRoutineResponse;
import com.example.prabhim.dto.workout.WorkoutRoutineUpdateRequest;

public interface WorkoutRoutineService {

    WorkoutRoutineResponse createWorkoutRoutine(WorkoutRoutineCreateRequest request);

    Page<WorkoutRoutineResponse> getAllWorkoutRoutines(String search, UUID memberId, UUID trainerId, String status, Pageable pageable);

    WorkoutRoutineResponse getWorkoutRoutineById(UUID id);

    WorkoutRoutineResponse updateWorkoutRoutine(UUID id, WorkoutRoutineUpdateRequest request);

    void deleteWorkoutRoutine(UUID id);

    WorkoutRoutineResponse addExercise(UUID routineId, WorkoutExerciseRequest exerciseRequest);

    WorkoutRoutineResponse removeExercise(UUID routineId, UUID exerciseId);
}
