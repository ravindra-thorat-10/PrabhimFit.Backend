package com.example.prabhim.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.prabhim.dto.diet.DietProtocolCreateRequest;
import com.example.prabhim.dto.diet.DietProtocolResponse;
import com.example.prabhim.dto.diet.DietProtocolUpdateRequest;
import com.example.prabhim.dto.diet.DietStatsResponse;

public interface DietPlanService {

    DietProtocolResponse createDietPlan(DietProtocolCreateRequest request);

    Page<DietProtocolResponse> getAllDietPlans(
            String search,
            UUID memberId,
            UUID trainerId,
            String status,
            Pageable pageable
    );

    DietProtocolResponse getDietPlanById(UUID id);

    List<DietProtocolResponse> getDietPlansByMemberId(UUID memberId);

    DietProtocolResponse getActiveDietPlanByMemberId(UUID memberId);

    DietProtocolResponse updateDietPlan(UUID id, DietProtocolUpdateRequest request);

    DietProtocolResponse updateDietPlanStatus(UUID id, String status);

    void deleteDietPlan(UUID id);

    DietStatsResponse getDietStats();
}
