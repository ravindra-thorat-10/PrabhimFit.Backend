package com.example.prabhim.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.prabhim.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    List<Payment> findByMemberIdOrderByPaymentDateDesc(UUID memberId);

    void deleteByMemberId(UUID memberId);
}
