package com.example.prabhim.controller;

import java.util.List;
import java.util.UUID;

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
import org.springframework.web.bind.annotation.RestController;

import com.example.prabhim.dto.ApiResponse;
import com.example.prabhim.dto.plan.MembershipPlanCreateRequest;
import com.example.prabhim.dto.plan.MembershipPlanResponse;
import com.example.prabhim.dto.plan.MembershipPlanUpdateRequest;
import com.example.prabhim.service.MembershipPlanService;

import jakarta.validation.Valid;

@RestController
@RequestMapping({"/api/v1/membership-plans", "/api/v1/plans"})
@CrossOrigin(origins = "*")
public class MembershipPlanController {

    private final MembershipPlanService membershipPlanService;

    public MembershipPlanController(MembershipPlanService membershipPlanService) {
        this.membershipPlanService = membershipPlanService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MembershipPlanResponse>>> getAllPlans() {
        List<MembershipPlanResponse> plans = membershipPlanService.getAllPlans();
        return ResponseEntity.ok(ApiResponse.success("Membership plans retrieved successfully", plans));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<MembershipPlanResponse>>> getActivePlans() {
        List<MembershipPlanResponse> plans = membershipPlanService.getActivePlans();
        return ResponseEntity.ok(ApiResponse.success("Active membership plans retrieved successfully", plans));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MembershipPlanResponse>> getPlanById(@PathVariable UUID id) {
        MembershipPlanResponse plan = membershipPlanService.getPlanById(id);
        return ResponseEntity.ok(ApiResponse.success("Membership plan retrieved successfully", plan));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MembershipPlanResponse>> createPlan(
            @Valid @RequestBody MembershipPlanCreateRequest request) {
        MembershipPlanResponse created = membershipPlanService.createPlan(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Membership plan created successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MembershipPlanResponse>> updatePlan(
            @PathVariable UUID id,
            @Valid @RequestBody MembershipPlanUpdateRequest request) {
        MembershipPlanResponse updated = membershipPlanService.updatePlan(id, request);
        return ResponseEntity.ok(ApiResponse.success("Membership plan updated successfully", updated));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<MembershipPlanResponse>> patchPlan(
            @PathVariable UUID id,
            @Valid @RequestBody MembershipPlanUpdateRequest request) {
        MembershipPlanResponse updated = membershipPlanService.updatePlan(id, request);
        return ResponseEntity.ok(ApiResponse.success("Membership plan updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePlan(@PathVariable UUID id) {
        membershipPlanService.deletePlan(id);
        return ResponseEntity.ok(ApiResponse.success("Membership plan deleted successfully", null));
    }
}
