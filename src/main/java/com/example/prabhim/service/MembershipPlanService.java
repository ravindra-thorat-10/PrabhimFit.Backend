package com.example.prabhim.service;

import java.util.List;
import java.util.UUID;

import com.example.prabhim.dto.plan.MembershipPlanCreateRequest;
import com.example.prabhim.dto.plan.MembershipPlanResponse;
import com.example.prabhim.dto.plan.MembershipPlanUpdateRequest;

public interface MembershipPlanService {

    List<MembershipPlanResponse> getAllPlans();

    List<MembershipPlanResponse> getActivePlans();

    MembershipPlanResponse getPlanById(UUID id);

    MembershipPlanResponse createPlan(MembershipPlanCreateRequest request);

    MembershipPlanResponse updatePlan(UUID id, MembershipPlanUpdateRequest request);

    void deletePlan(UUID id);

    void seedDefaultPlansIfEmpty();
}
