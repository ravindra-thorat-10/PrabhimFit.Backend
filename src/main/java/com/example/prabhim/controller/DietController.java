package com.example.prabhim.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.prabhim.dto.ApiResponse;
import com.example.prabhim.dto.diet.DietProtocolCreateRequest;
import com.example.prabhim.dto.diet.DietProtocolResponse;
import com.example.prabhim.dto.diet.DietProtocolUpdateRequest;
import com.example.prabhim.dto.diet.DietStatsResponse;
import com.example.prabhim.service.DietPlanService;

import jakarta.validation.Valid;

@RestController
@RequestMapping({"/api/v1/diets", "/api/v1/diet-plans", "/api/v1/nutritional-protocols"})
@CrossOrigin(origins = "*")
public class DietController {

    private final DietPlanService dietPlanService;

    public DietController(DietPlanService dietPlanService) {
        this.dietPlanService = dietPlanService;
    }

    // 1. Formulate & Prescribe Diet Protocol
    @PostMapping({"", "/"})
    public ResponseEntity<ApiResponse<DietProtocolResponse>> createDietPlan(
            @Valid @RequestBody DietProtocolCreateRequest request
    ) {
        DietProtocolResponse created = dietPlanService.createDietPlan(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Nutritional protocol created and prescribed successfully", created));
    }

    // 2. List All Diet Protocols (Search, Filter, Pagination)
    @GetMapping({"", "/"})
    public ResponseEntity<ApiResponse<Page<DietProtocolResponse>>> getAllDietPlans(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) UUID memberId,
            @RequestParam(required = false) UUID trainerId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<DietProtocolResponse> dietPlans = dietPlanService.getAllDietPlans(
                search, memberId, trainerId, status, pageable
        );
        return ResponseEntity.ok(ApiResponse.success("Nutritional protocols retrieved successfully", dietPlans));
    }

    // 3. Get Summary Statistics for Diets
    @GetMapping({"/stats", "/stats/", "/summary", "/summary/"})
    public ResponseEntity<ApiResponse<DietStatsResponse>> getDietStats() {
        DietStatsResponse stats = dietPlanService.getDietStats();
        return ResponseEntity.ok(ApiResponse.success("Diet statistics retrieved successfully", stats));
    }

    // 4. Get Diet Protocol by ID
    @GetMapping({"/{id}", "/{id}/"})
    public ResponseEntity<ApiResponse<DietProtocolResponse>> getDietPlanById(@PathVariable UUID id) {
        DietProtocolResponse dietPlan = dietPlanService.getDietPlanById(id);
        return ResponseEntity.ok(ApiResponse.success("Nutritional protocol retrieved successfully", dietPlan));
    }

    // 5. Get All Diet Protocols for an Athlete / Member
    @GetMapping({"/member/{memberId}", "/member/{memberId}/"})
    public ResponseEntity<ApiResponse<List<DietProtocolResponse>>> getDietPlansByMemberId(@PathVariable UUID memberId) {
        List<DietProtocolResponse> dietPlans = dietPlanService.getDietPlansByMemberId(memberId);
        return ResponseEntity.ok(ApiResponse.success("Member diet protocols retrieved successfully", dietPlans));
    }

    // 6. Get Active Diet Protocol for an Athlete / Member
    @GetMapping({"/member/{memberId}/active", "/member/{memberId}/active/"})
    public ResponseEntity<ApiResponse<DietProtocolResponse>> getActiveDietPlanByMemberId(@PathVariable UUID memberId) {
        DietProtocolResponse dietPlan = dietPlanService.getActiveDietPlanByMemberId(memberId);
        return ResponseEntity.ok(ApiResponse.success("Active member diet protocol retrieved successfully", dietPlan));
    }

    // 7. Update Diet Protocol (Full Update)
    @PutMapping({"/{id}", "/{id}/"})
    public ResponseEntity<ApiResponse<DietProtocolResponse>> updateDietPlan(
            @PathVariable UUID id,
            @RequestBody DietProtocolUpdateRequest request
    ) {
        DietProtocolResponse updated = dietPlanService.updateDietPlan(id, request);
        return ResponseEntity.ok(ApiResponse.success("Nutritional protocol updated successfully", updated));
    }

    // 8. Patch Diet Protocol (Partial Update)
    @PatchMapping({"/{id}", "/{id}/"})
    public ResponseEntity<ApiResponse<DietProtocolResponse>> patchDietPlan(
            @PathVariable UUID id,
            @RequestBody DietProtocolUpdateRequest request
    ) {
        DietProtocolResponse updated = dietPlanService.updateDietPlan(id, request);
        return ResponseEntity.ok(ApiResponse.success("Nutritional protocol updated successfully", updated));
    }

    // 9. Update Status (e.g., ACTIVE, INACTIVE, COMPLETED)
    @PatchMapping({"/{id}/status", "/{id}/status/"})
    public ResponseEntity<ApiResponse<DietProtocolResponse>> updateStatus(
            @PathVariable UUID id,
            @RequestParam String status
    ) {
        DietProtocolResponse updated = dietPlanService.updateDietPlanStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success("Diet protocol status updated successfully", updated));
    }

    // 10. Delete Diet Protocol
    @DeleteMapping({"/{id}", "/{id}/"})
    public ResponseEntity<ApiResponse<Void>> deleteDietPlan(@PathVariable UUID id) {
        dietPlanService.deleteDietPlan(id);
        return ResponseEntity.ok(ApiResponse.success("Nutritional protocol deleted successfully", null));
    }
}
