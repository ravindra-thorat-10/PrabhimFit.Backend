package com.example.prabhim.controller;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import com.example.prabhim.dto.lead.LeadConvertToMemberRequest;
import com.example.prabhim.dto.lead.LeadCreateRequest;
import com.example.prabhim.dto.lead.LeadPatchRequest;
import com.example.prabhim.dto.lead.LeadResponse;
import com.example.prabhim.dto.lead.LeadStatsResponse;
import com.example.prabhim.dto.lead.LeadUpdateRequest;
import com.example.prabhim.dto.member.MemberResponse;
import com.example.prabhim.dto.member.PageResponse;
import com.example.prabhim.entity.enums.Gender;
import com.example.prabhim.entity.enums.LeadPriority;
import com.example.prabhim.entity.enums.LeadSource;
import com.example.prabhim.entity.enums.LeadStatus;
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

    // 1. POST - Register New Lead / Walk-in Visitor
    @PostMapping({"", "/"})
    public ResponseEntity<ApiResponse<LeadResponse>> createLead(
            @Valid @RequestBody LeadCreateRequest request,
            @AuthenticationPrincipal UUID actorUserId) {
        LeadResponse response = leadService.createLead(request, actorUserId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Lead registered successfully.", response));
    }

    // 2. GET - List All Leads with Search, Filters & Pagination
    @GetMapping({"", "/"})
    public ResponseEntity<ApiResponse<PageResponse<LeadResponse>>> getAllLeads(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Gender gender,
            @RequestParam(required = false) LeadStatus status,
            @RequestParam(required = false) LeadPriority priority,
            @RequestParam(required = false) LeadSource source,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate followUpStartDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate followUpEndDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate joiningStartDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate joiningEndDate,
            @RequestParam(required = false) Boolean actionRequiredToday,
            @RequestParam(required = false) Boolean paymentDueOnly,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        PageResponse<LeadResponse> response = leadService.getLeads(
                search, gender, status, priority, source,
                followUpStartDate, followUpEndDate,
                joiningStartDate, joiningEndDate,
                actionRequiredToday, paymentDueOnly,
                pageable
        );

        return ResponseEntity.ok(ApiResponse.success("Leads retrieved successfully.", response));
    }

    // 3. GET - Pipeline KPIs & Metrics Summary
    @GetMapping({"/stats", "/stats/"})
    public ResponseEntity<ApiResponse<LeadStatsResponse>> getLeadStats() {
        LeadStatsResponse stats = leadService.getLeadStats();
        return ResponseEntity.ok(ApiResponse.success("Lead metrics retrieved successfully.", stats));
    }

    // 4. GET - Get Lead by ID
    @GetMapping({"/{id}", "/{id}/"})
    public ResponseEntity<ApiResponse<LeadResponse>> getLeadById(@PathVariable UUID id) {
        LeadResponse response = leadService.getLeadById(id);
        return ResponseEntity.ok(ApiResponse.success("Lead details retrieved successfully.", response));
    }

    // 5. GET - Get Lead by Code
    @GetMapping({"/code/{leadCode}", "/code/{leadCode}/"})
    public ResponseEntity<ApiResponse<LeadResponse>> getLeadByCode(@PathVariable String leadCode) {
        LeadResponse response = leadService.getLeadByCode(leadCode);
        return ResponseEntity.ok(ApiResponse.success("Lead details retrieved successfully.", response));
    }

    // 6. PUT - Update Lead (Full Edit)
    @PutMapping({"/{id}", "/{id}/"})
    public ResponseEntity<ApiResponse<LeadResponse>> updateLead(
            @PathVariable UUID id,
            @Valid @RequestBody LeadUpdateRequest request,
            @AuthenticationPrincipal UUID actorUserId) {
        LeadResponse response = leadService.updateLead(id, request, actorUserId);
        return ResponseEntity.ok(ApiResponse.success("Lead updated successfully.", response));
    }

    // 7. PATCH - Partial Update Lead
    @PatchMapping({"/{id}", "/{id}/"})
    public ResponseEntity<ApiResponse<LeadResponse>> patchLead(
            @PathVariable UUID id,
            @RequestBody LeadPatchRequest request,
            @AuthenticationPrincipal UUID actorUserId) {
        LeadResponse response = leadService.patchLead(id, request, actorUserId);
        return ResponseEntity.ok(ApiResponse.success("Lead updated successfully.", response));
    }

    // 8. DELETE - Delete Lead
    @DeleteMapping({"/{id}", "/{id}/"})
    public ResponseEntity<ApiResponse<Void>> deleteLead(
            @PathVariable UUID id,
            @AuthenticationPrincipal UUID actorUserId) {
        leadService.deleteLead(id, actorUserId);
        return ResponseEntity.ok(ApiResponse.success("Lead deleted successfully."));
    }

    // 9. POST - Convert Lead into Active Member
    @PostMapping({"/{id}/convert", "/{id}/convert/"})
    public ResponseEntity<ApiResponse<MemberResponse>> convertLeadToMember(
            @PathVariable UUID id,
            @RequestBody(required = false) LeadConvertToMemberRequest request,
            @AuthenticationPrincipal UUID actorUserId) {
        MemberResponse response = leadService.convertLeadToMember(id, request, actorUserId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Lead successfully converted to gym member.", response));
    }

    // 10. GET - Export Leads CSV
    @GetMapping({"/export/csv", "/export/csv/"})
    public ResponseEntity<byte[]> exportLeadsCsv(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Gender gender,
            @RequestParam(required = false) LeadStatus status,
            @RequestParam(required = false) LeadPriority priority,
            @RequestParam(required = false) LeadSource source,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate followUpStartDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate followUpEndDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate joiningStartDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate joiningEndDate,
            @RequestParam(required = false) Boolean actionRequiredToday,
            @RequestParam(required = false) Boolean paymentDueOnly) {

        byte[] csvData = leadService.exportLeadsCsv(
                search, gender, status, priority, source,
                followUpStartDate, followUpEndDate,
                joiningStartDate, joiningEndDate,
                actionRequiredToday, paymentDueOnly
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"leads_export.csv\"")
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .body(csvData);
    }
}
