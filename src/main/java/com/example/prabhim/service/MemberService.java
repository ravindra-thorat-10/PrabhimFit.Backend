package com.example.prabhim.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;

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

public interface MemberService {

    MemberResponse createMember(MemberCreateRequest request, UUID actorUserId);

    PageResponse<MemberResponse> getMembers(
            String search,
            MemberStatus status,
            UUID trainerId,
            String membershipPlan,
            MembershipStatus membershipStatus,
            LocalDate startDate,
            LocalDate endDate,
            Gender gender,
            Pageable pageable);

    MemberResponse getMemberById(UUID id);

    MemberResponse updateMember(UUID id, MemberUpdateRequest request, UUID actorUserId);

    MemberResponse patchMember(UUID id, MemberPatchRequest request, UUID actorUserId);

    void deleteMember(UUID id, UUID actorUserId);

    MemberProfileResponse getMemberProfile(UUID id);

    MemberStatsResponse getMemberStats();

    QuickCheckInResponse quickCheckIn(UUID memberId, QuickCheckInRequest request, UUID actorUserId);

    MemberResponse renewMembership(UUID memberId, MemberRenewalRequest request, UUID actorUserId);

    List<TrainerSummaryDto> getTrainers();

    byte[] exportMembersCsv();
}
