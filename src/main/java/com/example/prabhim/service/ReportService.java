package com.example.prabhim.service;

import java.time.LocalDate;

import com.example.prabhim.dto.report.AttendanceReportResponse;
import com.example.prabhim.dto.report.FinancialReportResponse;
import com.example.prabhim.dto.report.GymOverviewReportResponse;
import com.example.prabhim.dto.report.MembershipReportResponse;
import com.example.prabhim.entity.enums.PaymentStatus;

public interface ReportService {

    GymOverviewReportResponse getGymOverviewReport();

    FinancialReportResponse getFinancialReport(LocalDate startDate, LocalDate endDate, PaymentStatus status, String paymentMethod);

    AttendanceReportResponse getAttendanceReport(LocalDate startDate, LocalDate endDate);

    MembershipReportResponse getMembershipReport();

    byte[] exportGymOverviewCsv();

    byte[] exportFinancialReportCsv(LocalDate startDate, LocalDate endDate, PaymentStatus status, String paymentMethod);

    byte[] exportAttendanceReportCsv(LocalDate startDate, LocalDate endDate);

    byte[] exportMembershipReportCsv();
}
