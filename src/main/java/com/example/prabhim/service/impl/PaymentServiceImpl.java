package com.example.prabhim.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.prabhim.dto.payment.InvoiceBillResponse;
import com.example.prabhim.dto.payment.PaymentRecordRequest;
import com.example.prabhim.dto.payment.PaymentResponse;
import com.example.prabhim.dto.payment.PaymentStatsResponse;
import com.example.prabhim.dto.payment.PaymentUpdateRequest;
import com.example.prabhim.entity.Member;
import com.example.prabhim.entity.MembershipPlan;
import com.example.prabhim.entity.Payment;
import com.example.prabhim.entity.enums.PaymentStatus;
import com.example.prabhim.exception.PaymentNotFoundException;
import com.example.prabhim.repository.MemberRepository;
import com.example.prabhim.repository.MembershipPlanRepository;
import com.example.prabhim.repository.PaymentRepository;
import com.example.prabhim.service.PaymentService;

import jakarta.persistence.criteria.Predicate;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final MemberRepository memberRepository;
    private final MembershipPlanRepository membershipPlanRepository;

    public PaymentServiceImpl(
            PaymentRepository paymentRepository,
            MemberRepository memberRepository,
            MembershipPlanRepository membershipPlanRepository
    ) {
        this.paymentRepository = paymentRepository;
        this.memberRepository = memberRepository;
        this.membershipPlanRepository = membershipPlanRepository;
    }

    @Override
    public PaymentResponse recordPayment(PaymentRecordRequest request) {
        Payment payment = new Payment();

        // 1. Amount
        payment.setAmount(request.getAmount());

        // 2. Date
        if (request.getPaymentDate() != null) {
            payment.setPaymentDate(request.getPaymentDate());
        } else {
            payment.setPaymentDate(LocalDateTime.now());
        }

        // 3. Payment Method (normalize, e.g. "UPI (GPay / PhonePe / Paytm)" -> "UPI" or keep as provided)
        if (request.getPaymentMethod() != null && !request.getPaymentMethod().trim().isEmpty()) {
            String method = request.getPaymentMethod().trim();
            if (method.toUpperCase().startsWith("UPI")) {
                payment.setPaymentMethod("UPI");
            } else if (method.toUpperCase().startsWith("CARD")) {
                payment.setPaymentMethod("CARD");
            } else if (method.toUpperCase().startsWith("CASH")) {
                payment.setPaymentMethod("CASH");
            } else if (method.toUpperCase().contains("NET") || method.toUpperCase().contains("BANK")) {
                payment.setPaymentMethod("NET_BANKING");
            } else {
                payment.setPaymentMethod(method);
            }
        } else {
            payment.setPaymentMethod("UPI");
        }

        // 4. Status
        payment.setStatus(request.getStatus() != null ? request.getStatus() : PaymentStatus.PAID);

        // 5. Notes & Reference
        payment.setDescription(request.getDescription());
        payment.setNotes(request.getDescription());
        payment.setTransactionReference(request.getTransactionReference());

        // 6. Associate Member
        resolveAndSetMember(payment, request.getMemberId(), request.getMemberCode(), request.getMemberName());

        // 7. Associate Plan
        resolveAndSetPlan(payment, request.getMembershipPlanId(), request.getPlanName());

        // 8. Generate / Set Invoice Number
        if (request.getInvoiceNumber() != null && !request.getInvoiceNumber().trim().isEmpty()) {
            payment.setInvoiceNumber(request.getInvoiceNumber().trim());
        } else {
            payment.setInvoiceNumber(generateNextInvoiceNumber());
        }

        Payment saved = paymentRepository.save(payment);
        return PaymentResponse.fromEntity(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentResponse> getAllPayments(
            String search,
            UUID memberId,
            UUID planId,
            PaymentStatus status,
            String paymentMethod,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    ) {
        Specification<Payment> spec = buildSpecification(search, memberId, planId, status, paymentMethod, startDate, endDate);
        return paymentRepository.findAll(spec, pageable).map(PaymentResponse::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentById(UUID id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment invoice not found with id: " + id));
        return PaymentResponse.fromEntity(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceBillResponse getInvoiceBill(UUID id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment invoice not found with id: " + id));
        return InvoiceBillResponse.fromEntity(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByMemberId(UUID memberId) {
        return paymentRepository.findByMemberIdOrderByPaymentDateDesc(memberId)
                .stream()
                .map(PaymentResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentResponse updatePayment(UUID id, PaymentUpdateRequest request) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment invoice not found with id: " + id));

        if (request.getAmount() != null) {
            payment.setAmount(request.getAmount());
        }

        if (request.getPaymentDate() != null) {
            payment.setPaymentDate(request.getPaymentDate());
        }

        if (request.getPaymentMethod() != null && !request.getPaymentMethod().trim().isEmpty()) {
            String method = request.getPaymentMethod().trim();
            if (method.toUpperCase().startsWith("UPI")) {
                payment.setPaymentMethod("UPI");
            } else if (method.toUpperCase().startsWith("CARD")) {
                payment.setPaymentMethod("CARD");
            } else if (method.toUpperCase().startsWith("CASH")) {
                payment.setPaymentMethod("CASH");
            } else if (method.toUpperCase().contains("NET") || method.toUpperCase().contains("BANK")) {
                payment.setPaymentMethod("NET_BANKING");
            } else {
                payment.setPaymentMethod(method);
            }
        }

        if (request.getStatus() != null) {
            payment.setStatus(request.getStatus());
        }

        if (request.getDescription() != null) {
            payment.setDescription(request.getDescription().trim());
            payment.setNotes(request.getDescription().trim());
        }

        if (request.getTransactionReference() != null) {
            payment.setTransactionReference(request.getTransactionReference().trim());
        }

        if (request.getInvoiceNumber() != null && !request.getInvoiceNumber().trim().isEmpty()) {
            payment.setInvoiceNumber(request.getInvoiceNumber().trim());
        }

        if (request.getMemberId() != null || request.getMemberCode() != null || request.getMemberName() != null) {
            resolveAndSetMember(payment, request.getMemberId(), request.getMemberCode(), request.getMemberName());
        }

        if (request.getMembershipPlanId() != null || request.getPlanName() != null) {
            resolveAndSetPlan(payment, request.getMembershipPlanId(), request.getPlanName());
        }

        Payment saved = paymentRepository.save(payment);
        return PaymentResponse.fromEntity(saved);
    }

    @Override
    public PaymentResponse updatePaymentStatus(UUID id, PaymentStatus status) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment invoice not found with id: " + id));

        if (status != null) {
            payment.setStatus(status);
        }

        Payment saved = paymentRepository.save(payment);
        return PaymentResponse.fromEntity(saved);
    }

    @Override
    public void deletePayment(UUID id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment invoice not found with id: " + id));
        paymentRepository.delete(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentStatsResponse getPaymentStats() {
        BigDecimal lifetime = paymentRepository.findTotalLifetimeCollections();
        if (lifetime == null) lifetime = BigDecimal.ZERO;

        YearMonth currentMonth = YearMonth.now();
        LocalDateTime monthStart = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime monthEnd = currentMonth.atEndOfMonth().atTime(LocalTime.MAX);

        BigDecimal monthRevenue = paymentRepository.findRevenueBetween(monthStart, monthEnd);
        if (monthRevenue == null) monthRevenue = BigDecimal.ZERO;

        BigDecimal pendingDues = paymentRepository.findPendingUncollectedDues();
        if (pendingDues == null) pendingDues = BigDecimal.ZERO;

        long paidCount = paymentRepository.countByStatus(PaymentStatus.PAID);
        long pendingCount = paymentRepository.countByStatus(PaymentStatus.PENDING);
        long totalInvoices = paymentRepository.count();

        return new PaymentStatsResponse(
                lifetime,
                monthRevenue,
                pendingDues,
                paidCount,
                pendingCount,
                totalInvoices
        );
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] exportPaymentsCsv(
            String search,
            UUID memberId,
            UUID planId,
            PaymentStatus status,
            String paymentMethod,
            LocalDate startDate,
            LocalDate endDate
    ) {
        Specification<Payment> spec = buildSpecification(search, memberId, planId, status, paymentMethod, startDate, endDate);
        List<Payment> payments = paymentRepository.findAll(spec);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PrintWriter writer = new PrintWriter(baos, true, StandardCharsets.UTF_8)) {
            writer.println("Invoice ID,Member Name,Member Code,Membership Plan,Amount,Method,Payment Date,Status,Transaction Notes,Reference");

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

            for (Payment p : payments) {
                PaymentResponse resp = PaymentResponse.fromEntity(p);
                String invoice = escapeCsv(resp.getInvoiceNumber());
                String member = escapeCsv(resp.getMemberName());
                String code = escapeCsv(resp.getMemberCode() != null ? resp.getMemberCode() : "");
                String plan = escapeCsv(resp.getPlanName() != null ? resp.getPlanName() : "");
                String amount = resp.getAmount() != null ? resp.getAmount().toPlainString() : "0";
                String method = escapeCsv(resp.getPaymentMethod());
                String date = p.getPaymentDate() != null ? p.getPaymentDate().format(dtf) : "";
                String stat = resp.getStatus() != null ? resp.getStatus().name() : "";
                String notes = escapeCsv(p.getDescription() != null ? p.getDescription() : "");
                String ref = escapeCsv(p.getTransactionReference() != null ? p.getTransactionReference() : "");

                writer.printf("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s%n",
                        invoice, member, code, plan, amount, method, date, stat, notes, ref);
            }
        }
        return baos.toByteArray();
    }

    private Specification<Payment> buildSpecification(
            String search,
            UUID memberId,
            UUID planId,
            PaymentStatus status,
            String paymentMethod,
            LocalDate startDate,
            LocalDate endDate
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (memberId != null) {
                predicates.add(cb.equal(root.get("member").get("id"), memberId));
            }

            if (planId != null) {
                predicates.add(cb.equal(root.get("membershipPlan").get("id"), planId));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            if (paymentMethod != null && !paymentMethod.trim().isEmpty() && !paymentMethod.equalsIgnoreCase("ALL")) {
                predicates.add(cb.like(cb.upper(root.get("paymentMethod")), "%" + paymentMethod.trim().toUpperCase() + "%"));
            }

            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("paymentDate"), startDate.atStartOfDay()));
            }

            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("paymentDate"), endDate.atTime(LocalTime.MAX)));
            }

            if (search != null && !search.trim().isEmpty()) {
                String searchPattern = "%" + search.trim().toLowerCase() + "%";
                Predicate invoiceMatch = cb.like(cb.lower(root.get("invoiceNumber")), searchPattern);
                Predicate memberNameMatch = cb.like(cb.lower(root.get("memberName")), searchPattern);
                Predicate memberCodeMatch = cb.like(cb.lower(root.get("memberCode")), searchPattern);
                Predicate planNameMatch = cb.like(cb.lower(root.get("planName")), searchPattern);
                Predicate descMatch = cb.like(cb.lower(root.get("description")), searchPattern);
                Predicate refMatch = cb.like(cb.lower(root.get("transactionReference")), searchPattern);

                predicates.add(cb.or(invoiceMatch, memberNameMatch, memberCodeMatch, planNameMatch, descMatch, refMatch));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private void resolveAndSetMember(Payment payment, UUID memberId, String memberCode, String memberName) {
        if (memberId != null) {
            Member member = memberRepository.findById(memberId).orElse(null);
            if (member != null) {
                payment.setMember(member);
                return;
            }
        }
        if (memberCode != null && !memberCode.trim().isEmpty()) {
            Member member = memberRepository.findByMemberCode(memberCode.trim()).orElse(null);
            if (member != null) {
                payment.setMember(member);
                return;
            }
            payment.setMemberCode(memberCode.trim());
        }
        if (memberName != null && !memberName.trim().isEmpty()) {
            payment.setMemberName(memberName.trim());
        }
    }

    private void resolveAndSetPlan(Payment payment, UUID planId, String planName) {
        if (planId != null) {
            MembershipPlan plan = membershipPlanRepository.findById(planId).orElse(null);
            if (plan != null) {
                payment.setMembershipPlan(plan);
                return;
            }
        }
        if (planName != null && !planName.trim().isEmpty()) {
            MembershipPlan plan = membershipPlanRepository.findByName(planName.trim()).orElse(null);
            if (plan != null) {
                payment.setMembershipPlan(plan);
            } else {
                payment.setPlanName(planName.trim());
            }
        }
    }

    private synchronized String generateNextInvoiceNumber() {
        long count = paymentRepository.count();
        long seq = 2001 + count;
        String candidate = "INV-" + seq;
        int safety = 0;
        while (paymentRepository.findByInvoiceNumber(candidate).isPresent() && safety < 1000) {
            seq++;
            candidate = "INV-" + seq;
            safety++;
        }
        return candidate;
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
