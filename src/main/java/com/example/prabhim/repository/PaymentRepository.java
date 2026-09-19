package com.example.prabhim.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.prabhim.entity.Payment;
import com.example.prabhim.entity.enums.PaymentStatus;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID>, JpaSpecificationExecutor<Payment> {

    List<Payment> findByMemberIdOrderByPaymentDateDesc(UUID memberId);

    List<Payment> findByStatusOrderByPaymentDateDesc(PaymentStatus status);

    long countByStatus(PaymentStatus status);

    Optional<Payment> findByInvoiceNumber(String invoiceNumber);

    Optional<Payment> findTopByOrderByCreatedAtDesc();

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.status = com.example.prabhim.entity.enums.PaymentStatus.PAID")
    BigDecimal findTotalLifetimeCollections();

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.status = com.example.prabhim.entity.enums.PaymentStatus.PAID AND p.paymentDate >= :start AND p.paymentDate <= :end")
    BigDecimal findRevenueBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.status = com.example.prabhim.entity.enums.PaymentStatus.PENDING")
    BigDecimal findPendingUncollectedDues();

    void deleteByMemberId(UUID memberId);
}

