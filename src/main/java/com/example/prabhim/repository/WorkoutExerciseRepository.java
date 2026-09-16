package com.example.prabhim.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.prabhim.entity.WorkoutExercise;

@Repository
public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExercise, UUID> {

    List<WorkoutExercise> findByWorkoutRoutineIdOrderByOrderIndexAsc(UUID workoutRoutineId);

    void deleteByWorkoutRoutineId(UUID workoutRoutineId);
}
