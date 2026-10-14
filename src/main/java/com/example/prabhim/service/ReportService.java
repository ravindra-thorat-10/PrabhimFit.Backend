package com.example.prabhim.service;

import java.time.LocalDate;

import com.example.prabhim.dto.report.StudioAnalyticsReportResponse;
import com.example.prabhim.entity.enums.PaymentStatus;

public interface ReportService {

    StudioAnalyticsReportResponse getStudioAnalyticsReport(LocalDate startDate, LocalDate endDate, PaymentStatus status, String paymentMethod);

    byte[] exportReportByType(String type, LocalDate startDate, LocalDate endDate, PaymentStatus status, String paymentMethod);

    byte[] exportMembersCsv();

    byte[] exportPaymentsCsv(LocalDate startDate, LocalDate endDate, PaymentStatus status, String paymentMethod);

    byte[] exportTrainersCsv();

    byte[] exportAttendanceReportCsv(LocalDate startDate, LocalDate endDate);
}
