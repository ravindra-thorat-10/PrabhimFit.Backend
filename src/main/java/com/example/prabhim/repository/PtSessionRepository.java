package com.example.prabhim.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.prabhim.entity.PtSession;

@Repository
public interface PtSessionRepository extends JpaRepository<PtSession, UUID> {

    List<PtSession> findByMemberIdOrderBySessionDateDesc(UUID memberId);

    void deleteByMemberId(UUID memberId);
}
