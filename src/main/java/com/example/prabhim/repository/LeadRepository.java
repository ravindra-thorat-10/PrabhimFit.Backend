package com.example.prabhim.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.prabhim.entity.Lead;
import com.example.prabhim.entity.enums.LeadSource;
import com.example.prabhim.entity.enums.LeadStatus;

@Repository
public interface LeadRepository extends JpaRepository<Lead, UUID>, JpaSpecificationExecutor<Lead> {

    Optional<Lead> findByLeadCode(String leadCode);

    boolean existsByLeadCode(String leadCode);

    boolean existsByPhone(String phone);

    Optional<Lead> findTopByOrderByCreatedAtDesc();

    @Query("SELECT COUNT(l) FROM Lead l WHERE l.followUpDate = :date AND l.status NOT IN :excludedStatuses")
    long countDueOnDate(LocalDate date, Collection<LeadStatus> excludedStatuses);

    @Query("SELECT COUNT(l) FROM Lead l WHERE l.followUpDate BETWEEN :start AND :end AND l.status NOT IN :excludedStatuses")
    long countDueBetween(LocalDate start, LocalDate end, Collection<LeadStatus> excludedStatuses);

    @Query("SELECT COUNT(l) FROM Lead l WHERE l.createdAt BETWEEN :start AND :end")
    long countCreatedBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT COUNT(l) FROM Lead l WHERE l.pendingPlanFee > :amount AND l.status NOT IN :excludedStatuses")
    long countPendingPaymentDues(BigDecimal amount, Collection<LeadStatus> excludedStatuses);

    @Query("SELECT COUNT(l) FROM Lead l WHERE l.inquirySource = :source")
    long countByInquirySource(LeadSource source);

    @Query("SELECT COUNT(l) FROM Lead l WHERE l.status = :status")
    long countByStatus(LeadStatus status);

    @Query("SELECT COUNT(l) FROM Lead l WHERE l.status IN :statuses")
    long countByStatusIn(Collection<LeadStatus> statuses);

    @Query("SELECT COUNT(l) FROM Lead l WHERE (l.followUpDate <= :date OR l.status = 'ACTION_REQUIRED') AND l.status NOT IN :excludedStatuses")
    long countActionRequiredToday(LocalDate date, Collection<LeadStatus> excludedStatuses);
}
