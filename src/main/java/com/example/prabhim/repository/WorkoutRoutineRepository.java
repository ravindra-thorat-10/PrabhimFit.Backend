package com.example.prabhim.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.prabhim.entity.WorkoutRoutine;

@Repository
public interface WorkoutRoutineRepository extends JpaRepository<WorkoutRoutine, UUID>, JpaSpecificationExecutor<WorkoutRoutine> {

    List<WorkoutRoutine> findByMemberIdOrderByCreatedAtDesc(UUID memberId);

    List<WorkoutRoutine> findByTrainerIdOrderByCreatedAtDesc(UUID trainerId);

    List<WorkoutRoutine> findByStatusIgnoreCase(String status);
}
