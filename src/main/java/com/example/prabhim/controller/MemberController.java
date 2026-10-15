package com.example.prabhim.controller;

import java.time.LocalDate;
import java.util.List;
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
import com.example.prabhim.dto.member.AssignTrainerRequest;
import com.example.prabhim.dto.member.MemberCreateRequest;
import com.example.prabhim.dto.member.MemberPatchRequest;
import com.example.prabhim.dto.member.MemberProfileResponse;
import com.example.prabhim.dto.member.MemberRenewalRequest;
import com.example.prabhim.dto.member.MemberResponse;
import com.example.prabhim.dto.member.MemberStatsResponse;
import com.example.prabhim.dto.member.MemberUpdateRequest;
import com.example.prabhim.dto.member.PageResponse;
import com.example.prabhim.dto.member.QuickCheckInRequest;
import com.example.prabhim.dto.member.QuickCheckInResponse;
import com.example.prabhim.dto.member.TrainerSummaryDto;
import com.example.prabhim.entity.enums.Gender;
import com.example.prabhim.entity.enums.MemberStatus;
import com.example.prabhim.entity.enums.MembershipStatus;
import com.example.prabhim.service.MemberService;

import jakarta.validation.Valid;

@RestController
@RequestMapping({"/api/v1/members", "/api/v1/member"})
@CrossOrigin(origins = "*")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 1. Add Member
    @PostMapping({"", "/"})
    public ResponseEntity<ApiResponse<MemberResponse>> addMember(
            @Valid @RequestBody MemberCreateRequest request,
            @AuthenticationPrincipal UUID actorUserId) {
        MemberResponse response = memberService.createMember(request, actorUserId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Member created successfully.", response));
    }

    // 2. List All Members
    @GetMapping({"", "/"})
    public ResponseEntity<ApiResponse<PageResponse<MemberResponse>>> getAllMembers(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) MemberStatus status,
            @RequestParam(required = false) UUID trainerId,
            @RequestParam(required = false) String membershipPlan,
            @RequestParam(required = false) String plan,
            @RequestParam(required = false) MembershipStatus membershipStatus,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Gender gender,
            @RequestParam(required = false) String creatorEmail,
            @RequestParam(required = false) String gymId,
            @RequestParam(required = false) String tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        String effectivePlan = (membershipPlan != null && !membershipPlan.trim().isEmpty())
                ? membershipPlan
                : plan;

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        PageResponse<MemberResponse> response = memberService.getMembers(
                search, status, trainerId, effectivePlan, membershipStatus, startDate, endDate, gender, pageable
        );

        return ResponseEntity.ok(ApiResponse.success("Members retrieved successfully.", response));
    }

    // 3. Member Dashboard Stats
    @GetMapping({"/stats", "/stats/"})
    public ResponseEntity<ApiResponse<MemberStatsResponse>> getMemberStats() {
        MemberStatsResponse response = memberService.getMemberStats();
        return ResponseEntity.ok(ApiResponse.success("Member stats retrieved successfully.", response));
    }

    // 4. List Trainers Summary
    @GetMapping({"/trainers", "/trainers/"})
    public ResponseEntity<ApiResponse<List<TrainerSummaryDto>>> getTrainers() {
        List<TrainerSummaryDto> response = memberService.getTrainers();
        return ResponseEntity.ok(ApiResponse.success("Trainers retrieved successfully.", response));
    }

    // 5. CSV Export
    @GetMapping({"/export", "/export/"})
    public ResponseEntity<byte[]> exportMembersCsv() {
        byte[] csvBytes = memberService.exportMembersCsv();
        String filename = "PrabhimFit_Members_" + LocalDate.now() + ".csv";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .body(csvBytes);
    }

    // 6. Get One Member Using ID
    @GetMapping({"/{id}", "/{id}/"})
    public ResponseEntity<ApiResponse<MemberResponse>> getMemberById(@PathVariable UUID id) {
        MemberResponse response = memberService.getMemberById(id);
        return ResponseEntity.ok(ApiResponse.success("Member retrieved successfully.", response));
    }

    // 7. Get Full 360 Member Profile
    @GetMapping({"/{id}/profile", "/{id}/profile/"})
    public ResponseEntity<ApiResponse<MemberProfileResponse>> getMemberProfile(@PathVariable UUID id) {
        MemberProfileResponse response = memberService.getMemberProfile(id);
        return ResponseEntity.ok(ApiResponse.success("Member profile retrieved successfully.", response));
    }

    // 8. Edit Member
    @PutMapping({"/{id}", "/{id}/"})
    public ResponseEntity<ApiResponse<MemberResponse>> editMember(
            @PathVariable UUID id,
            @Valid @RequestBody MemberUpdateRequest request,
            @AuthenticationPrincipal UUID actorUserId) {
        MemberResponse response = memberService.updateMember(id, request, actorUserId);
        return ResponseEntity.ok(ApiResponse.success("Member updated successfully.", response));
    }

    // 9. Patch Member
    @PatchMapping({"/{id}", "/{id}/"})
    public ResponseEntity<ApiResponse<MemberResponse>> patchMember(
            @PathVariable UUID id,
            @Valid @RequestBody MemberPatchRequest request,
            @AuthenticationPrincipal UUID actorUserId) {
        MemberResponse response = memberService.patchMember(id, request, actorUserId);
        return ResponseEntity.ok(ApiResponse.success("Member updated successfully.", response));
    }

    // 10. Assign Trainer
    @PutMapping({"/{id}/assign-trainer", "/{id}/assign-trainer/"})
    public ResponseEntity<ApiResponse<MemberResponse>> assignTrainer(
            @PathVariable UUID id,
            @RequestBody(required = false) AssignTrainerRequest request,
            @AuthenticationPrincipal UUID actorUserId) {
        UUID trainerId = request != null ? request.getTrainerId() : null;
        MemberResponse response = memberService.assignTrainer(id, trainerId, actorUserId);
        return ResponseEntity.ok(ApiResponse.success("Trainer assigned successfully.", response));
    }

    // 11. Renew Membership
    @PostMapping({"/{id}/renew", "/{id}/renew/"})
    public ResponseEntity<ApiResponse<MemberResponse>> renewMembership(
            @PathVariable UUID id,
            @Valid @RequestBody(required = false) MemberRenewalRequest request,
            @AuthenticationPrincipal UUID actorUserId) {
        MemberResponse response = memberService.renewMembership(id, request, actorUserId);
        return ResponseEntity.ok(ApiResponse.success("Membership renewed successfully.", response));
    }

    // 12. Quick Check-In
    @PostMapping({"/{id}/check-in", "/{id}/check-in/"})
    public ResponseEntity<ApiResponse<QuickCheckInResponse>> quickCheckIn(
            @PathVariable UUID id,
            @RequestBody(required = false) QuickCheckInRequest request,
            @AuthenticationPrincipal UUID actorUserId) {
        QuickCheckInResponse response = memberService.quickCheckIn(id, request, actorUserId);
        return ResponseEntity.ok(ApiResponse.success("Check-in recorded successfully.", response));
    }

    // 13. Delete Member
    @DeleteMapping({"/{id}", "/{id}/"})
    public ResponseEntity<ApiResponse<Void>> deleteMember(
            @PathVariable UUID id,
            @AuthenticationPrincipal UUID actorUserId) {
        memberService.deleteMember(id, actorUserId);
        return ResponseEntity.ok(ApiResponse.success("Member deleted successfully."));
    }
}
