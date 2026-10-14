package com.example.prabhim.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.prabhim.entity.LeadDiscussion;

@Repository
public interface LeadDiscussionRepository extends JpaRepository<LeadDiscussion, UUID> {

    List<LeadDiscussion> findByLeadIdOrderByDateDesc(UUID leadId);

    void deleteByLeadId(UUID leadId);
}
