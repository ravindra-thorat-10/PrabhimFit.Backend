package com.example.prabhim.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.prabhim.entity.DietPlan;

@Repository
public interface DietPlanRepository extends JpaRepository<DietPlan, UUID>, JpaSpecificationExecutor<DietPlan> {

    List<DietPlan> findByMemberIdOrderByCreatedAtDesc(UUID memberId);

    Optional<DietPlan> findFirstByMemberIdAndStatusOrderByCreatedAtDesc(UUID memberId, String status);

    List<DietPlan> findByTrainerIdOrderByCreatedAtDesc(UUID trainerId);

    List<DietPlan> findByStatusIgnoreCaseOrderByCreatedAtDesc(String status);

    long countByStatusIgnoreCase(String status);

    @Query("SELECT AVG(d.dailyCalories) FROM DietPlan d WHERE d.dailyCalories IS NOT NULL")
    Double findAverageCalories();

    @Query("SELECT AVG(d.dailyWaterTarget) FROM DietPlan d WHERE d.dailyWaterTarget IS NOT NULL")
    Double findAverageWaterTarget();

    void deleteByMemberId(UUID memberId);
}

