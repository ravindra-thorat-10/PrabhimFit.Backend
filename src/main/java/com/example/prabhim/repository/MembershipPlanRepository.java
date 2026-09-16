package com.example.prabhim.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.prabhim.entity.MembershipPlan;

@Repository
public interface MembershipPlanRepository extends JpaRepository<MembershipPlan, UUID> {

    Optional<MembershipPlan> findByName(String name);

    Optional<MembershipPlan> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

    List<MembershipPlan> findAllByOrderByPriceAsc();

    List<MembershipPlan> findByStatusOrderByPriceAsc(String status);
}
