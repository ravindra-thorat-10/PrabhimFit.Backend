package com.example.prabhim.dto.payment;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class PaymentStatsResponse {

    private BigDecimal totalLifetimeCollections;
    private String totalLifetimeCollectionsDisplay;

    private BigDecimal thisMonthRevenue;
    private String thisMonthRevenueDisplay;

    private BigDecimal pendingUncollectedDues;
    private String pendingUncollectedDuesDisplay;

    private Long totalPaidInvoices;
    private Long totalPendingInvoices;
    private Long totalInvoices;

    public PaymentStatsResponse() {
    }

    public PaymentStatsResponse(BigDecimal totalLifetimeCollections,
                                BigDecimal thisMonthRevenue,
                                BigDecimal pendingUncollectedDues,
                                Long totalPaidInvoices,
                                Long totalPendingInvoices,
                                Long totalInvoices) {
        this.totalLifetimeCollections = totalLifetimeCollections != null ? totalLifetimeCollections : BigDecimal.ZERO;
        this.thisMonthRevenue = thisMonthRevenue != null ? thisMonthRevenue : BigDecimal.ZERO;
        this.pendingUncollectedDues = pendingUncollectedDues != null ? pendingUncollectedDues : BigDecimal.ZERO;
        this.totalPaidInvoices = totalPaidInvoices != null ? totalPaidInvoices : 0L;
        this.totalPendingInvoices = totalPendingInvoices != null ? totalPendingInvoices : 0L;
        this.totalInvoices = totalInvoices != null ? totalInvoices : 0L;

        NumberFormat indiaFormat = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        this.totalLifetimeCollectionsDisplay = indiaFormat.format(this.totalLifetimeCollections).replace("INR", "₹").trim();
        this.thisMonthRevenueDisplay = indiaFormat.format(this.thisMonthRevenue).replace("INR", "₹").trim();
        this.pendingUncollectedDuesDisplay = indiaFormat.format(this.pendingUncollectedDues).replace("INR", "₹").trim();
    }

    public BigDecimal getTotalLifetimeCollections() {
        return totalLifetimeCollections;
    }

    public void setTotalLifetimeCollections(BigDecimal totalLifetimeCollections) {
        this.totalLifetimeCollections = totalLifetimeCollections;
    }

    public String getTotalLifetimeCollectionsDisplay() {
        return totalLifetimeCollectionsDisplay;
    }

    public void setTotalLifetimeCollectionsDisplay(String totalLifetimeCollectionsDisplay) {
        this.totalLifetimeCollectionsDisplay = totalLifetimeCollectionsDisplay;
    }

    public BigDecimal getThisMonthRevenue() {
        return thisMonthRevenue;
    }

    public void setThisMonthRevenue(BigDecimal thisMonthRevenue) {
        this.thisMonthRevenue = thisMonthRevenue;
    }

    public String getThisMonthRevenueDisplay() {
        return thisMonthRevenueDisplay;
    }

    public void setThisMonthRevenueDisplay(String thisMonthRevenueDisplay) {
        this.thisMonthRevenueDisplay = thisMonthRevenueDisplay;
    }

    public BigDecimal getPendingUncollectedDues() {
        return pendingUncollectedDues;
    }

    public void setPendingUncollectedDues(BigDecimal pendingUncollectedDues) {
        this.pendingUncollectedDues = pendingUncollectedDues;
    }

    public String getPendingUncollectedDuesDisplay() {
        return pendingUncollectedDuesDisplay;
    }

    public void setPendingUncollectedDuesDisplay(String pendingUncollectedDuesDisplay) {
        this.pendingUncollectedDuesDisplay = pendingUncollectedDuesDisplay;
    }

    public Long getTotalPaidInvoices() {
        return totalPaidInvoices;
    }

    public void setTotalPaidInvoices(Long totalPaidInvoices) {
        this.totalPaidInvoices = totalPaidInvoices;
    }

    public Long getTotalPendingInvoices() {
        return totalPendingInvoices;
    }

    public void setTotalPendingInvoices(Long totalPendingInvoices) {
        this.totalPendingInvoices = totalPendingInvoices;
    }

    public Long getTotalInvoices() {
        return totalInvoices;
    }

    public void setTotalInvoices(Long totalInvoices) {
        this.totalInvoices = totalInvoices;
    }
}
