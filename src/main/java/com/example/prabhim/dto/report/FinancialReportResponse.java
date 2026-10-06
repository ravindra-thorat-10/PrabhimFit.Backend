package com.example.prabhim.dto.report;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import com.example.prabhim.dto.payment.PaymentResponse;

public class FinancialReportResponse {

    // Totals & KPI
    private BigDecimal totalCollections;
    private String totalCollectionsDisplay;

    private BigDecimal pendingDues;
    private String pendingDuesDisplay;

    private BigDecimal refundedAmount;
    private String refundedAmountDisplay;

    private BigDecimal failedAmount;
    private String failedAmountDisplay;

    private Long totalTransactions;
    private BigDecimal averageTransactionValue;
    private String averageTransactionValueDisplay;

    // Breakdowns
    private List<PaymentMethodMetric> paymentMethodBreakdown;
    private List<PaymentStatusMetric> statusBreakdown;
    private List<MonthlyRevenueMetric> monthlyTrends;
    private List<PaymentResponse> recentTransactions;

    public FinancialReportResponse() {
    }

    public static class PaymentMethodMetric {
        private String method;
        private BigDecimal amount;
        private String amountDisplay;
        private Long count;
        private Double percentage;

        public PaymentMethodMetric() {
        }

        public PaymentMethodMetric(String method, BigDecimal amount, Long count, Double percentage) {
            this.method = method;
            this.amount = amount != null ? amount : BigDecimal.ZERO;
            this.amountDisplay = formatCurrency(this.amount);
            this.count = count != null ? count : 0L;
            this.percentage = percentage != null ? BigDecimal.valueOf(percentage).setScale(2, RoundingMode.HALF_UP).doubleValue() : 0.0;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
            this.amountDisplay = formatCurrency(amount);
        }

        public String getAmountDisplay() {
            return amountDisplay;
        }

        public void setAmountDisplay(String amountDisplay) {
            this.amountDisplay = amountDisplay;
        }

        public Long getCount() {
            return count;
        }

        public void setCount(Long count) {
            this.count = count;
        }

        public Double getPercentage() {
            return percentage;
        }

        public void setPercentage(Double percentage) {
            this.percentage = percentage;
        }
    }

    public static class PaymentStatusMetric {
        private String status;
        private BigDecimal amount;
        private String amountDisplay;
        private Long count;

        public PaymentStatusMetric() {
        }

        public PaymentStatusMetric(String status, BigDecimal amount, Long count) {
            this.status = status;
            this.amount = amount != null ? amount : BigDecimal.ZERO;
            this.amountDisplay = formatCurrency(this.amount);
            this.count = count != null ? count : 0L;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
            this.amountDisplay = formatCurrency(amount);
        }

        public String getAmountDisplay() {
            return amountDisplay;
        }

        public void setAmountDisplay(String amountDisplay) {
            this.amountDisplay = amountDisplay;
        }

        public Long getCount() {
            return count;
        }

        public void setCount(Long count) {
            this.count = count;
        }
    }

    public static class MonthlyRevenueMetric {
        private String period;
        private String periodLabel;
        private BigDecimal amount;
        private String amountDisplay;
        private Long transactionCount;

        public MonthlyRevenueMetric() {
        }

        public MonthlyRevenueMetric(String period, String periodLabel, BigDecimal amount, Long transactionCount) {
            this.period = period;
            this.periodLabel = periodLabel;
            this.amount = amount != null ? amount : BigDecimal.ZERO;
            this.amountDisplay = formatCurrency(this.amount);
            this.transactionCount = transactionCount != null ? transactionCount : 0L;
        }

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public String getPeriodLabel() {
            return periodLabel;
        }

        public void setPeriodLabel(String periodLabel) {
            this.periodLabel = periodLabel;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
            this.amountDisplay = formatCurrency(amount);
        }

        public String getAmountDisplay() {
            return amountDisplay;
        }

        public void setAmountDisplay(String amountDisplay) {
            this.amountDisplay = amountDisplay;
        }

        public Long getTransactionCount() {
            return transactionCount;
        }

        public void setTransactionCount(Long transactionCount) {
            this.transactionCount = transactionCount;
        }
    }

    public BigDecimal getTotalCollections() {
        return totalCollections;
    }

    public void setTotalCollections(BigDecimal totalCollections) {
        this.totalCollections = totalCollections;
        this.totalCollectionsDisplay = formatCurrency(totalCollections);
    }

    public String getTotalCollectionsDisplay() {
        return totalCollectionsDisplay;
    }

    public void setTotalCollectionsDisplay(String totalCollectionsDisplay) {
        this.totalCollectionsDisplay = totalCollectionsDisplay;
    }

    public BigDecimal getPendingDues() {
        return pendingDues;
    }

    public void setPendingDues(BigDecimal pendingDues) {
        this.pendingDues = pendingDues;
        this.pendingDuesDisplay = formatCurrency(pendingDues);
    }

    public String getPendingDuesDisplay() {
        return pendingDuesDisplay;
    }

    public void setPendingDuesDisplay(String pendingDuesDisplay) {
        this.pendingDuesDisplay = pendingDuesDisplay;
    }

    public BigDecimal getRefundedAmount() {
        return refundedAmount;
    }

    public void setRefundedAmount(BigDecimal refundedAmount) {
        this.refundedAmount = refundedAmount;
        this.refundedAmountDisplay = formatCurrency(refundedAmount);
    }

    public String getRefundedAmountDisplay() {
        return refundedAmountDisplay;
    }

    public void setRefundedAmountDisplay(String refundedAmountDisplay) {
        this.refundedAmountDisplay = refundedAmountDisplay;
    }

    public BigDecimal getFailedAmount() {
        return failedAmount;
    }

    public void setFailedAmount(BigDecimal failedAmount) {
        this.failedAmount = failedAmount;
        this.failedAmountDisplay = formatCurrency(failedAmount);
    }

    public String getFailedAmountDisplay() {
        return failedAmountDisplay;
    }

    public void setFailedAmountDisplay(String failedAmountDisplay) {
        this.failedAmountDisplay = failedAmountDisplay;
    }

    public Long getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(Long totalTransactions) {
        this.totalTransactions = totalTransactions;
    }

    public BigDecimal getAverageTransactionValue() {
        return averageTransactionValue;
    }

    public void setAverageTransactionValue(BigDecimal averageTransactionValue) {
        this.averageTransactionValue = averageTransactionValue;
        this.averageTransactionValueDisplay = formatCurrency(averageTransactionValue);
    }

    public String getAverageTransactionValueDisplay() {
        return averageTransactionValueDisplay;
    }

    public void setAverageTransactionValueDisplay(String averageTransactionValueDisplay) {
        this.averageTransactionValueDisplay = averageTransactionValueDisplay;
    }

    public List<PaymentMethodMetric> getPaymentMethodBreakdown() {
        return paymentMethodBreakdown;
    }

    public void setPaymentMethodBreakdown(List<PaymentMethodMetric> paymentMethodBreakdown) {
        this.paymentMethodBreakdown = paymentMethodBreakdown;
    }

    public List<PaymentStatusMetric> getStatusBreakdown() {
        return statusBreakdown;
    }

    public void setStatusBreakdown(List<PaymentStatusMetric> statusBreakdown) {
        this.statusBreakdown = statusBreakdown;
    }

    public List<MonthlyRevenueMetric> getMonthlyTrends() {
        return monthlyTrends;
    }

    public void setMonthlyTrends(List<MonthlyRevenueMetric> monthlyTrends) {
        this.monthlyTrends = monthlyTrends;
    }

    public List<PaymentResponse> getRecentTransactions() {
        return recentTransactions;
    }

    public void setRecentTransactions(List<PaymentResponse> recentTransactions) {
        this.recentTransactions = recentTransactions;
    }

    private static String formatCurrency(BigDecimal amount) {
        if (amount == null) amount = BigDecimal.ZERO;
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        return nf.format(amount).replace("INR", "₹").trim();
    }
}
