package com.example.prabhim.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.prabhim.entity.Membership;
import com.example.prabhim.entity.enums.MembershipStatus;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, UUID> {

    List<Membership> findByMemberIdOrderByCreatedAtDesc(UUID memberId);

    Optional<Membership> findFirstByMemberIdAndStatusOrderByEndDateDesc(UUID memberId, MembershipStatus status);

    @Query("SELECT m FROM Membership m WHERE m.member.id = :memberId ORDER BY m.endDate DESC LIMIT 1")
    Optional<Membership> findLatestMembershipByMemberId(@Param("memberId") UUID memberId);

    long countByStatus(MembershipStatus status);

    @Query("SELECT COUNT(m) FROM Membership m WHERE LOWER(m.planName) = LOWER(:planName) AND m.status = :status")
    long countByPlanNameIgnoreCaseAndStatus(@Param("planName") String planName, @Param("status") MembershipStatus status);

    @Query("SELECT COUNT(m) FROM Membership m WHERE m.status = 'ACTIVE' AND m.endDate BETWEEN :startDate AND :endDate")
    long countExpiringBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    void deleteByMemberId(UUID memberId);
}
