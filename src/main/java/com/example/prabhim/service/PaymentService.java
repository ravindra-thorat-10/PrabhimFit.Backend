package com.example.prabhim.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.prabhim.dto.payment.InvoiceBillResponse;
import com.example.prabhim.dto.payment.PaymentRecordRequest;
import com.example.prabhim.dto.payment.PaymentResponse;
import com.example.prabhim.dto.payment.PaymentStatsResponse;
import com.example.prabhim.dto.payment.PaymentUpdateRequest;
import com.example.prabhim.entity.enums.PaymentStatus;

public interface PaymentService {

    PaymentResponse recordPayment(PaymentRecordRequest request);

    Page<PaymentResponse> getAllPayments(
            String search,
            UUID memberId,
            UUID planId,
            PaymentStatus status,
            String paymentMethod,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    );

    PaymentResponse getPaymentById(UUID id);

    InvoiceBillResponse getInvoiceBill(UUID id);

    List<PaymentResponse> getPaymentsByMemberId(UUID memberId);

    PaymentResponse updatePayment(UUID id, PaymentUpdateRequest request);

    PaymentResponse updatePaymentStatus(UUID id, PaymentStatus status);

    void deletePayment(UUID id);

    PaymentStatsResponse getPaymentStats();

    byte[] exportPaymentsCsv(
            String search,
            UUID memberId,
            UUID planId,
            PaymentStatus status,
            String paymentMethod,
            LocalDate startDate,
            LocalDate endDate
    );
}
