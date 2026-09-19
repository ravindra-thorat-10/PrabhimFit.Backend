package com.example.prabhim.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.prabhim.dto.ApiResponse;
import com.example.prabhim.dto.payment.InvoiceBillResponse;
import com.example.prabhim.dto.payment.PaymentRecordRequest;
import com.example.prabhim.dto.payment.PaymentResponse;
import com.example.prabhim.dto.payment.PaymentStatsResponse;
import com.example.prabhim.dto.payment.PaymentUpdateRequest;
import com.example.prabhim.entity.enums.PaymentStatus;
import com.example.prabhim.service.PaymentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping({"/api/v1/payments", "/api/v1/invoices", "/api/v1/billing"})
@CrossOrigin(origins = "*")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // 1. Record Fee Payment (Modal Form)
    @PostMapping({"", "/"})
    public ResponseEntity<ApiResponse<PaymentResponse>> recordPayment(
            @Valid @RequestBody PaymentRecordRequest request
    ) {
        PaymentResponse response = paymentService.recordPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Fee payment voucher recorded successfully", response));
    }

    // 2. List All Payments & Invoices (Dashboard Table with search, filters, pagination)
    @GetMapping({"", "/"})
    public ResponseEntity<ApiResponse<Page<PaymentResponse>>> getAllPayments(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) UUID memberId,
            @RequestParam(required = false) UUID planId,
            @RequestParam(required = false) PaymentStatus status,
            @RequestParam(required = false) String paymentMethod,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "paymentDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<PaymentResponse> payments = paymentService.getAllPayments(
                search, memberId, planId, status, paymentMethod, startDate, endDate, pageable
        );
        return ResponseEntity.ok(ApiResponse.success("Payments retrieved successfully", payments));
    }

    // 3. Get Summary Statistics for KPI Cards
    @GetMapping({"/stats", "/stats/", "/summary", "/summary/"})
    public ResponseEntity<ApiResponse<PaymentStatsResponse>> getPaymentStats() {
        PaymentStatsResponse stats = paymentService.getPaymentStats();
        return ResponseEntity.ok(ApiResponse.success("Payment statistics retrieved successfully", stats));
    }

    // 4. Get Payment Record by ID
    @GetMapping({"/{id}", "/{id}/"})
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentById(@PathVariable UUID id) {
        PaymentResponse payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(ApiResponse.success("Payment retrieved successfully", payment));
    }

    // 5. Get Digital Invoice / Bill Receipt (Bill Action)
    @GetMapping({"/{id}/bill", "/{id}/bill/", "/{id}/invoice", "/{id}/invoice/", "/{id}/receipt", "/{id}/receipt/"})
    public ResponseEntity<ApiResponse<InvoiceBillResponse>> getInvoiceBill(@PathVariable UUID id) {
        InvoiceBillResponse bill = paymentService.getInvoiceBill(id);
        return ResponseEntity.ok(ApiResponse.success("Digital bill receipt retrieved successfully", bill));
    }

    // 6. Export Payments to CSV (Export CSV Button)
    @GetMapping({"/export-csv", "/export-csv/", "/export", "/export/"})
    public ResponseEntity<byte[]> exportPaymentsCsv(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) UUID memberId,
            @RequestParam(required = false) UUID planId,
            @RequestParam(required = false) PaymentStatus status,
            @RequestParam(required = false) String paymentMethod,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        byte[] csvBytes = paymentService.exportPaymentsCsv(
                search, memberId, planId, status, paymentMethod, startDate, endDate
        );
        String filename = "payments_report_" + LocalDate.now() + ".csv";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .body(csvBytes);
    }

    // 7. Get Payments by Member ID
    @GetMapping({"/member/{memberId}", "/member/{memberId}/"})
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getPaymentsByMemberId(@PathVariable UUID memberId) {
        List<PaymentResponse> payments = paymentService.getPaymentsByMemberId(memberId);
        return ResponseEntity.ok(ApiResponse.success("Member payments retrieved successfully", payments));
    }

    // 8. Update Payment Voucher (Full Update)
    @PutMapping({"/{id}", "/{id}/"})
    public ResponseEntity<ApiResponse<PaymentResponse>> updatePayment(
            @PathVariable UUID id,
            @RequestBody PaymentUpdateRequest request
    ) {
        PaymentResponse updated = paymentService.updatePayment(id, request);
        return ResponseEntity.ok(ApiResponse.success("Payment updated successfully", updated));
    }

    // 9. Patch Payment Voucher (Partial Update)
    @PatchMapping({"/{id}", "/{id}/"})
    public ResponseEntity<ApiResponse<PaymentResponse>> patchPayment(
            @PathVariable UUID id,
            @RequestBody PaymentUpdateRequest request
    ) {
        PaymentResponse updated = paymentService.updatePayment(id, request);
        return ResponseEntity.ok(ApiResponse.success("Payment updated successfully", updated));
    }

    // 10. Update Payment Status (e.g. PAID, PENDING, FAILED, REFUNDED)
    @PatchMapping({"/{id}/status", "/{id}/status/"})
    public ResponseEntity<ApiResponse<PaymentResponse>> updatePaymentStatus(
            @PathVariable UUID id,
            @RequestParam PaymentStatus status
    ) {
        PaymentResponse updated = paymentService.updatePaymentStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success("Payment status updated successfully", updated));
    }

    // 11. Delete Payment Record
    @DeleteMapping({"/{id}", "/{id}/"})
    public ResponseEntity<ApiResponse<Void>> deletePayment(@PathVariable UUID id) {
        paymentService.deletePayment(id);
        return ResponseEntity.ok(ApiResponse.success("Payment deleted successfully", null));
    }
}
