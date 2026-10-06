package com.example.prabhim.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.prabhim.dto.ApiResponse;
import com.example.prabhim.dto.report.AttendanceReportResponse;
import com.example.prabhim.dto.report.FinancialReportResponse;
import com.example.prabhim.dto.report.GymOverviewReportResponse;
import com.example.prabhim.dto.report.MembershipReportResponse;
import com.example.prabhim.entity.enums.PaymentStatus;
import com.example.prabhim.service.ReportService;

@RestController
@RequestMapping({ "/api/v1/reports", "/api/v1/analytics" })
@CrossOrigin(origins = "*")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // 1. Gym Overview / KPI Dashboard Report (JSON)
    @GetMapping({ "/overview", "/overview/", "/dashboard", "/dashboard/" })
    public ResponseEntity<ApiResponse<GymOverviewReportResponse>> getGymOverview() {
        GymOverviewReportResponse report = reportService.getGymOverviewReport();
        return ResponseEntity.ok(ApiResponse.success("Gym overview report generated successfully", report));
    }

    // 2. Financial & Revenue Report (JSON)
    @GetMapping({ "/financial", "/financial/", "/revenue", "/revenue/" })
    public ResponseEntity<ApiResponse<FinancialReportResponse>> getFinancialReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) PaymentStatus status,
            @RequestParam(required = false) String paymentMethod) {
        FinancialReportResponse report = reportService.getFinancialReport(startDate, endDate, status, paymentMethod);
        return ResponseEntity.ok(ApiResponse.success("Financial & revenue report generated successfully", report));
    }

    // 3. Attendance Report (JSON)
    @GetMapping({ "/attendance", "/attendance/" })
    public ResponseEntity<ApiResponse<AttendanceReportResponse>> getAttendanceReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        AttendanceReportResponse report = reportService.getAttendanceReport(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success("Attendance report generated successfully", report));
    }

    // 4. Membership & Plan Distribution Report (JSON)
    @GetMapping({ "/memberships", "/memberships/", "/plans", "/plans/" })
    public ResponseEntity<ApiResponse<MembershipReportResponse>> getMembershipReport() {
        MembershipReportResponse report = reportService.getMembershipReport();
        return ResponseEntity.ok(ApiResponse.success("Membership report generated successfully", report));
    }

    // 5. Export Gym Overview Report (CSV)
    @GetMapping({ "/overview/export", "/overview/export/", "/dashboard/export", "/dashboard/export/" })
    public ResponseEntity<byte[]> exportGymOverviewCsv() {
        byte[] csvBytes = reportService.exportGymOverviewCsv();
        String filename = "gym_overview_report_" + LocalDate.now() + ".csv";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .body(csvBytes);
    }

    // 6. Export Financial Report (CSV)
    @GetMapping({ "/financial/export", "/financial/export/", "/revenue/export", "/revenue/export/" })
    public ResponseEntity<byte[]> exportFinancialCsv(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) PaymentStatus status,
            @RequestParam(required = false) String paymentMethod) {
        byte[] csvBytes = reportService.exportFinancialReportCsv(startDate, endDate, status, paymentMethod);
        String filename = "financial_report_" + LocalDate.now() + ".csv";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .body(csvBytes);
    }

    // 7. Export Attendance Report (CSV)
    @GetMapping({ "/attendance/export", "/attendance/export/" })
    public ResponseEntity<byte[]> exportAttendanceCsv(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        byte[] csvBytes = reportService.exportAttendanceReportCsv(startDate, endDate);
        String filename = "attendance_report_" + LocalDate.now() + ".csv";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .body(csvBytes);
    }

    // 8. Export Membership Report (CSV)
    @GetMapping({ "/memberships/export", "/memberships/export/", "/plans/export", "/plans/export/" })
    public ResponseEntity<byte[]> exportMembershipCsv() {
        byte[] csvBytes = reportService.exportMembershipReportCsv();
        String filename = "membership_report_" + LocalDate.now() + ".csv";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .body(csvBytes);
    }
}
