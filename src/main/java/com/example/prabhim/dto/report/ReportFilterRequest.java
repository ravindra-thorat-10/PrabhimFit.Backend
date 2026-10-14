package com.example.prabhim.dto.report;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.prabhim.entity.enums.PaymentStatus;

public class ReportFilterRequest {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    private String type;
    private String reportType;
    private PaymentStatus status;
    private String paymentMethod;
    private String search;

    public ReportFilterRequest() {
    }

    public ReportFilterRequest(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getType() {
        return type != null ? type : reportType;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReportType() {
        return reportType != null ? reportType : type;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
