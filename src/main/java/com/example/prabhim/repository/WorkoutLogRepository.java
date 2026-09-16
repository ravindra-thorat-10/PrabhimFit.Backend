package com.example.prabhim.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.prabhim.entity.WorkoutLog;

@Repository
public interface WorkoutLogRepository extends JpaRepository<WorkoutLog, UUID> {

    List<WorkoutLog> findByMemberIdOrderByWorkoutDateDesc(UUID memberId);

    void deleteByMemberId(UUID memberId);
}
