package com.example.prabhim.service;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.domain.Pageable;

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

public interface LeadService {

    LeadResponse createLead(LeadCreateRequest request, UUID actorUserId);

    PageResponse<LeadResponse> getLeads(
            String search,
            Gender gender,
            LeadStatus status,
            LeadPriority priority,
            LeadSource source,
            LocalDate followUpStartDate,
            LocalDate followUpEndDate,
            LocalDate joiningStartDate,
            LocalDate joiningEndDate,
            Boolean actionRequiredToday,
            Boolean paymentDueOnly,
            Pageable pageable
    );

    LeadResponse getLeadById(UUID id);

    LeadResponse getLeadByCode(String leadCode);

    LeadResponse updateLead(UUID id, LeadUpdateRequest request, UUID actorUserId);

    LeadResponse patchLead(UUID id, LeadPatchRequest request, UUID actorUserId);

    void deleteLead(UUID id, UUID actorUserId);

    LeadStatsResponse getLeadStats();

    MemberResponse convertLeadToMember(UUID id, LeadConvertToMemberRequest request, UUID actorUserId);

    byte[] exportLeadsCsv(
            String search,
            Gender gender,
            LeadStatus status,
            LeadPriority priority,
            LeadSource source,
            LocalDate followUpStartDate,
            LocalDate followUpEndDate,
            LocalDate joiningStartDate,
            LocalDate joiningEndDate,
            Boolean actionRequiredToday,
            Boolean paymentDueOnly
    );
}
