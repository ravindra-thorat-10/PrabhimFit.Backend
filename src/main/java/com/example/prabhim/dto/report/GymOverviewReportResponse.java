package com.example.prabhim.dto.report;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class GymOverviewReportResponse {

    // Member KPI
    private Long totalMembers;
    private Long activeMembers;
    private Long inactiveMembers;
    private Long expiredMembers;
    private Long newMembersThisMonth;
    private Double retentionRatePercentage;

    // Revenue KPI
    private BigDecimal totalLifetimeRevenue;
    private String totalLifetimeRevenueDisplay;
    private BigDecimal thisMonthRevenue;
    private String thisMonthRevenueDisplay;
    private BigDecimal pendingDues;
    private String pendingDuesDisplay;
    private Long paidInvoicesCount;
    private Long pendingInvoicesCount;

    // Attendance KPI
    private Long todayCheckIns;
    private Long thisMonthCheckIns;
    private Long onTimeCheckInsToday;
    private Long lateCheckInsToday;

    // Trainer KPI
    private Long totalTrainers;
    private Long activeTrainers;
    private BigDecimal totalMonthlyPayroll;
    private String totalMonthlyPayrollDisplay;
    private Double averageMembersPerTrainer;

    // Plan Distribution
    private List<PlanDistributionItem> planDistribution;

    public GymOverviewReportResponse() {
    }

    public static class PlanDistributionItem {
        private String planName;
        private Long memberCount;
        private Double percentage;

        public PlanDistributionItem() {
        }

        public PlanDistributionItem(String planName, Long memberCount, Double percentage) {
            this.planName = planName;
            this.memberCount = memberCount;
            this.percentage = percentage;
        }

        public String getPlanName() {
            return planName;
        }

        public void setPlanName(String planName) {
            this.planName = planName;
        }

        public Long getMemberCount() {
            return memberCount;
        }

        public void setMemberCount(Long memberCount) {
            this.memberCount = memberCount;
        }

        public Double getPercentage() {
            return percentage;
        }

        public void setPercentage(Double percentage) {
            this.percentage = percentage;
        }
    }

    public Long getTotalMembers() {
        return totalMembers;
    }

    public void setTotalMembers(Long totalMembers) {
        this.totalMembers = totalMembers;
    }

    public Long getActiveMembers() {
        return activeMembers;
    }

    public void setActiveMembers(Long activeMembers) {
        this.activeMembers = activeMembers;
    }

    public Long getInactiveMembers() {
        return inactiveMembers;
    }

    public void setInactiveMembers(Long inactiveMembers) {
        this.inactiveMembers = inactiveMembers;
    }

    public Long getExpiredMembers() {
        return expiredMembers;
    }

    public void setExpiredMembers(Long expiredMembers) {
        this.expiredMembers = expiredMembers;
    }

    public Long getNewMembersThisMonth() {
        return newMembersThisMonth;
    }

    public void setNewMembersThisMonth(Long newMembersThisMonth) {
        this.newMembersThisMonth = newMembersThisMonth;
    }

    public Double getRetentionRatePercentage() {
        return retentionRatePercentage;
    }

    public void setRetentionRatePercentage(Double retentionRatePercentage) {
        this.retentionRatePercentage = retentionRatePercentage;
    }

    public BigDecimal getTotalLifetimeRevenue() {
        return totalLifetimeRevenue;
    }

    public void setTotalLifetimeRevenue(BigDecimal totalLifetimeRevenue) {
        this.totalLifetimeRevenue = totalLifetimeRevenue;
        this.totalLifetimeRevenueDisplay = formatCurrency(totalLifetimeRevenue);
    }

    public String getTotalLifetimeRevenueDisplay() {
        return totalLifetimeRevenueDisplay;
    }

    public void setTotalLifetimeRevenueDisplay(String totalLifetimeRevenueDisplay) {
        this.totalLifetimeRevenueDisplay = totalLifetimeRevenueDisplay;
    }

    public BigDecimal getThisMonthRevenue() {
        return thisMonthRevenue;
    }

    public void setThisMonthRevenue(BigDecimal thisMonthRevenue) {
        this.thisMonthRevenue = thisMonthRevenue;
        this.thisMonthRevenueDisplay = formatCurrency(thisMonthRevenue);
    }

    public String getThisMonthRevenueDisplay() {
        return thisMonthRevenueDisplay;
    }

    public void setThisMonthRevenueDisplay(String thisMonthRevenueDisplay) {
        this.thisMonthRevenueDisplay = thisMonthRevenueDisplay;
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

    public Long getPaidInvoicesCount() {
        return paidInvoicesCount;
    }

    public void setPaidInvoicesCount(Long paidInvoicesCount) {
        this.paidInvoicesCount = paidInvoicesCount;
    }

    public Long getPendingInvoicesCount() {
        return pendingInvoicesCount;
    }

    public void setPendingInvoicesCount(Long pendingInvoicesCount) {
        this.pendingInvoicesCount = pendingInvoicesCount;
    }

    public Long getTodayCheckIns() {
        return todayCheckIns;
    }

    public void setTodayCheckIns(Long todayCheckIns) {
        this.todayCheckIns = todayCheckIns;
    }

    public Long getThisMonthCheckIns() {
        return thisMonthCheckIns;
    }

    public void setThisMonthCheckIns(Long thisMonthCheckIns) {
        this.thisMonthCheckIns = thisMonthCheckIns;
    }

    public Long getOnTimeCheckInsToday() {
        return onTimeCheckInsToday;
    }

    public void setOnTimeCheckInsToday(Long onTimeCheckInsToday) {
        this.onTimeCheckInsToday = onTimeCheckInsToday;
    }

    public Long getLateCheckInsToday() {
        return lateCheckInsToday;
    }

    public void setLateCheckInsToday(Long lateCheckInsToday) {
        this.lateCheckInsToday = lateCheckInsToday;
    }

    public Long getTotalTrainers() {
        return totalTrainers;
    }

    public void setTotalTrainers(Long totalTrainers) {
        this.totalTrainers = totalTrainers;
    }

    public Long getActiveTrainers() {
        return activeTrainers;
    }

    public void setActiveTrainers(Long activeTrainers) {
        this.activeTrainers = activeTrainers;
    }

    public BigDecimal getTotalMonthlyPayroll() {
        return totalMonthlyPayroll;
    }

    public void setTotalMonthlyPayroll(BigDecimal totalMonthlyPayroll) {
        this.totalMonthlyPayroll = totalMonthlyPayroll;
        this.totalMonthlyPayrollDisplay = formatCurrency(totalMonthlyPayroll);
    }

    public String getTotalMonthlyPayrollDisplay() {
        return totalMonthlyPayrollDisplay;
    }

    public void setTotalMonthlyPayrollDisplay(String totalMonthlyPayrollDisplay) {
        this.totalMonthlyPayrollDisplay = totalMonthlyPayrollDisplay;
    }

    public Double getAverageMembersPerTrainer() {
        return averageMembersPerTrainer;
    }

    public void setAverageMembersPerTrainer(Double averageMembersPerTrainer) {
        this.averageMembersPerTrainer = averageMembersPerTrainer;
    }

    public List<PlanDistributionItem> getPlanDistribution() {
        return planDistribution;
    }

    public void setPlanDistribution(List<PlanDistributionItem> planDistribution) {
        this.planDistribution = planDistribution;
    }

    private static String formatCurrency(BigDecimal amount) {
        if (amount == null) amount = BigDecimal.ZERO;
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        return nf.format(amount).replace("INR", "₹").trim();
    }
}
