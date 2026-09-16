package com.example.prabhim.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.prabhim.entity.DietPlan;

@Repository
public interface DietPlanRepository extends JpaRepository<DietPlan, UUID> {

    List<DietPlan> findByMemberIdOrderByCreatedAtDesc(UUID memberId);

    Optional<DietPlan> findFirstByMemberIdAndStatusOrderByCreatedAtDesc(UUID memberId, String status);

    void deleteByMemberId(UUID memberId);
}
