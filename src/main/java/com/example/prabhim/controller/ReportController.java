package com.example.prabhim.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.prabhim.dto.ApiResponse;
import com.example.prabhim.dto.report.ReportFilterRequest;
import com.example.prabhim.dto.report.StudioAnalyticsReportResponse;
import com.example.prabhim.entity.enums.PaymentStatus;
import com.example.prabhim.service.ReportService;

@RestController
@RequestMapping("/api/v1/reports")
@CrossOrigin(origins = "*")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // 1. Get Studio Analytics & Executive Report (GET)
    @GetMapping({ "", "/" })
    public ResponseEntity<ApiResponse<StudioAnalyticsReportResponse>> getStudioAnalytics(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) PaymentStatus status,
            @RequestParam(required = false) String paymentMethod) {

        StudioAnalyticsReportResponse report = reportService.getStudioAnalyticsReport(startDate, endDate, status, paymentMethod);
        return ResponseEntity.ok(ApiResponse.success("Studio analytics report generated successfully", report));
    }

    // 2. Filter Studio Analytics & Executive Report (POST with Request Body)
    @PostMapping({ "", "/", "/filter", "/filter/" })
    public ResponseEntity<ApiResponse<StudioAnalyticsReportResponse>> filterStudioAnalytics(
            @RequestBody(required = false) ReportFilterRequest request) {

        LocalDate startDate = request != null ? request.getStartDate() : null;
        LocalDate endDate = request != null ? request.getEndDate() : null;
        PaymentStatus status = request != null ? request.getStatus() : null;
        String paymentMethod = request != null ? request.getPaymentMethod() : null;

        StudioAnalyticsReportResponse report = reportService.getStudioAnalyticsReport(startDate, endDate, status, paymentMethod);
        return ResponseEntity.ok(ApiResponse.success("Studio analytics report filtered successfully", report));
    }

    // 3. Download CSV Report by Type (GET) - matches frontend api.get('/reports/export/' + type)
    @GetMapping({ "/export/{type}", "/export/{type}/" })
    public ResponseEntity<byte[]> exportReportByType(
            @PathVariable String type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) PaymentStatus status,
            @RequestParam(required = false) String paymentMethod) {

        byte[] csvBytes = reportService.exportReportByType(type, startDate, endDate, status, paymentMethod);
        String filename = "PrabhimFit_" + type.substring(0, 1).toUpperCase() + type.substring(1) + "_Report_" + LocalDate.now() + ".csv";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .body(csvBytes);
    }

    // 4. Download CSV Report with Filter Body (POST)
    @PostMapping({ "/export", "/export/", "/export/{type}", "/export/{type}/" })
    public ResponseEntity<byte[]> exportReportByPost(
            @PathVariable(required = false) String type,
            @RequestBody(required = false) ReportFilterRequest request) {

        String reportType = type != null ? type : (request != null && request.getType() != null ? request.getType() : "members");
        LocalDate startDate = request != null ? request.getStartDate() : null;
        LocalDate endDate = request != null ? request.getEndDate() : null;
        PaymentStatus status = request != null ? request.getStatus() : null;
        String paymentMethod = request != null ? request.getPaymentMethod() : null;

        byte[] csvBytes = reportService.exportReportByType(reportType, startDate, endDate, status, paymentMethod);
        String filename = "PrabhimFit_" + reportType.substring(0, 1).toUpperCase() + reportType.substring(1) + "_Report_" + LocalDate.now() + ".csv";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .body(csvBytes);
    }
}
