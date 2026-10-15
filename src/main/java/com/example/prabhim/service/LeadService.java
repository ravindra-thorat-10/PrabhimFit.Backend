package com.example.prabhim.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;

import com.example.prabhim.dto.lead.LeadCreateRequest;
import com.example.prabhim.dto.lead.LeadDiscussionRequest;
import com.example.prabhim.dto.lead.LeadOptionsResponse;
import com.example.prabhim.dto.lead.LeadResponse;
import com.example.prabhim.dto.lead.LeadStatsResponse;
import com.example.prabhim.dto.lead.LeadUpdateRequest;
import com.example.prabhim.dto.member.PageResponse;

public interface LeadService {

    LeadResponse createLead(LeadCreateRequest request, UUID actorUserId);

    List<LeadResponse> getAllLeads(
            UUID actorUserId,
            String search,
            String status,
            String source,
            String priority,
            String assignedStaff,
            String gender,
            LocalDate startDate,
            LocalDate endDate,
            String sortBy,
            String sortDir
    );

    PageResponse<LeadResponse> getLeadsPaged(
            UUID actorUserId,
            String search,
            String status,
            String source,
            String priority,
            String assignedStaff,
            String gender,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    );

    LeadResponse getLeadById(UUID id, UUID actorUserId);

    LeadResponse updateLead(UUID id, LeadUpdateRequest request, UUID actorUserId);

    void deleteLead(UUID id, UUID actorUserId);

    LeadResponse addDiscussion(UUID id, LeadDiscussionRequest request, UUID actorUserId);

    LeadResponse convertToMember(UUID id, UUID actorUserId);

    LeadStatsResponse getStats(UUID actorUserId);

    LeadOptionsResponse getOptions();
}
