package com.example.prabhim.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import com.example.prabhim.dto.lead.LeadCreateRequest;
import com.example.prabhim.dto.lead.LeadDiscussionRequest;
import com.example.prabhim.dto.lead.LeadOptionsResponse;
import com.example.prabhim.dto.lead.LeadResponse;
import com.example.prabhim.dto.lead.LeadStatsResponse;
import com.example.prabhim.dto.lead.LeadUpdateRequest;
import com.example.prabhim.dto.member.PageResponse;
import com.example.prabhim.exception.LeadNotFoundException;
import com.example.prabhim.service.LeadService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/leads")
@CrossOrigin(origins = "*")
public class LeadController {

    private final LeadService leadService;

    public LeadController(LeadService leadService) {
        this.leadService = leadService;
    }

    private UUID parseUuid(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new LeadNotFoundException("Lead ID cannot be empty.");
        }
        try {
            return UUID.fromString(id.trim());
        } catch (IllegalArgumentException e) {
            throw new LeadNotFoundException("Lead not found with ID: " + id);
        }
    }

    // 1. Create Lead
    @PostMapping({"", "/"})
    public ResponseEntity<ApiResponse<LeadResponse>> createLead(
            @Valid @RequestBody LeadCreateRequest request,
            @AuthenticationPrincipal UUID actorUserId) {

        LeadResponse created = leadService.createLead(request, actorUserId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Lead registered successfully.", created));
    }

    // 2. Get Leads (returns flat array by default for frontend compatibility, or paginated if page param is passed)
    @GetMapping({"", "/"})
    public ResponseEntity<ApiResponse<?>> getLeads(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String assignedStaff,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Integer page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @AuthenticationPrincipal UUID actorUserId) {

        if (page != null) {
            Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            Pageable pageable = PageRequest.of(page, size, sort);
            PageResponse<LeadResponse> paged = leadService.getLeadsPaged(
                    actorUserId, search, status, source, priority, assignedStaff, gender, startDate, endDate, pageable
            );
            return ResponseEntity.ok(ApiResponse.success("Leads retrieved successfully.", paged));
        }

        List<LeadResponse> list = leadService.getAllLeads(
                actorUserId, search, status, source, priority, assignedStaff, gender, startDate, endDate, sortBy, sortDir
        );
        return ResponseEntity.ok(ApiResponse.success("Leads retrieved successfully.", list));
    }

    // 3. Get Lead by ID
    @GetMapping({"/{id}", "/{id}/"})
    public ResponseEntity<ApiResponse<LeadResponse>> getLeadById(
            @PathVariable String id,
            @AuthenticationPrincipal UUID actorUserId) {

        UUID uuid = parseUuid(id);
        LeadResponse lead = leadService.getLeadById(uuid, actorUserId);
        return ResponseEntity.ok(ApiResponse.success("Lead retrieved successfully.", lead));
    }

    // 4. Update Lead (PUT)
    @PutMapping({"/{id}", "/{id}/"})
    public ResponseEntity<ApiResponse<LeadResponse>> updateLead(
            @PathVariable String id,
            @Valid @RequestBody LeadUpdateRequest request,
            @AuthenticationPrincipal UUID actorUserId) {

        UUID uuid = parseUuid(id);
        LeadResponse updated = leadService.updateLead(uuid, request, actorUserId);
        return ResponseEntity.ok(ApiResponse.success("Lead updated successfully.", updated));
    }

    // 5. Patch Lead (PATCH)
    @PatchMapping({"/{id}", "/{id}/"})
    public ResponseEntity<ApiResponse<LeadResponse>> patchLead(
            @PathVariable String id,
            @RequestBody LeadUpdateRequest request,
            @AuthenticationPrincipal UUID actorUserId) {

        UUID uuid = parseUuid(id);
        LeadResponse updated = leadService.updateLead(uuid, request, actorUserId);
        return ResponseEntity.ok(ApiResponse.success("Lead updated successfully.", updated));
    }

    // 6. Delete Lead
    @DeleteMapping({"/{id}", "/{id}/"})
    public ResponseEntity<ApiResponse<Void>> deleteLead(
            @PathVariable String id,
            @AuthenticationPrincipal UUID actorUserId) {

        UUID uuid = parseUuid(id);
        leadService.deleteLead(uuid, actorUserId);
        return ResponseEntity.ok(ApiResponse.success("Lead deleted successfully.", null));
    }

    // 7. Log Discussion
    @PostMapping({"/{id}/discussions", "/{id}/discussions/"})
    public ResponseEntity<ApiResponse<LeadResponse>> addDiscussion(
            @PathVariable String id,
            @Valid @RequestBody LeadDiscussionRequest request,
            @AuthenticationPrincipal UUID actorUserId) {

        UUID uuid = parseUuid(id);
        LeadResponse updated = leadService.addDiscussion(uuid, request, actorUserId);
        return ResponseEntity.ok(ApiResponse.success("Discussion logged successfully.", updated));
    }

    // 8. Convert Lead to Active Member
    @PostMapping({"/{id}/convert", "/{id}/convert/"})
    public ResponseEntity<ApiResponse<LeadResponse>> convertToMember(
            @PathVariable String id,
            @AuthenticationPrincipal UUID actorUserId) {

        UUID uuid = parseUuid(id);
        LeadResponse converted = leadService.convertToMember(uuid, actorUserId);
        return ResponseEntity.ok(ApiResponse.success("Lead converted to member successfully.", converted));
    }

    // 9. Pipeline Metrics & Stats
    @GetMapping({"/stats", "/stats/"})
    public ResponseEntity<ApiResponse<LeadStatsResponse>> getStats(
            @AuthenticationPrincipal UUID actorUserId) {

        LeadStatsResponse stats = leadService.getStats(actorUserId);
        return ResponseEntity.ok(ApiResponse.success("Lead stats retrieved successfully.", stats));
    }

    // 10. Dropdown Reference Options
    @GetMapping({"/options", "/options/"})
    public ResponseEntity<ApiResponse<LeadOptionsResponse>> getOptions() {
        LeadOptionsResponse options = leadService.getOptions();
        return ResponseEntity.ok(ApiResponse.success("Lead options retrieved successfully.", options));
    }
}
