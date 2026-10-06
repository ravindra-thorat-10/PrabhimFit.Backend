package com.example.prabhim.dto.report;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public class MembershipReportResponse {

    private Long totalActiveMemberships;
    private Long totalExpiredMemberships;
    private Long expiringWithin7Days;
    private Long expiringWithin30Days;

    private List<PlanBreakdownMetric> planBreakdown;
    private List<ExpiringMembershipItem> expiringSoonMembers;

    public MembershipReportResponse() {
    }

    public static class PlanBreakdownMetric {
        private String planName;
        private String planType;
        private Long activeCount;
        private BigDecimal totalRevenue;
        private String totalRevenueDisplay;

        public PlanBreakdownMetric() {
        }

        public PlanBreakdownMetric(String planName, String planType, Long activeCount, BigDecimal totalRevenue) {
            this.planName = planName;
            this.planType = planType;
            this.activeCount = activeCount != null ? activeCount : 0L;
            this.totalRevenue = totalRevenue != null ? totalRevenue : BigDecimal.ZERO;
            this.totalRevenueDisplay = formatCurrency(this.totalRevenue);
        }

        public String getPlanName() {
            return planName;
        }

        public void setPlanName(String planName) {
            this.planName = planName;
        }

        public String getPlanType() {
            return planType;
        }

        public void setPlanType(String planType) {
            this.planType = planType;
        }

        public Long getActiveCount() {
            return activeCount;
        }

        public void setActiveCount(Long activeCount) {
            this.activeCount = activeCount;
        }

        public BigDecimal getTotalRevenue() {
            return totalRevenue;
        }

        public void setTotalRevenue(BigDecimal totalRevenue) {
            this.totalRevenue = totalRevenue;
            this.totalRevenueDisplay = formatCurrency(totalRevenue);
        }

        public String getTotalRevenueDisplay() {
            return totalRevenueDisplay;
        }

        public void setTotalRevenueDisplay(String totalRevenueDisplay) {
            this.totalRevenueDisplay = totalRevenueDisplay;
        }
    }

    public static class ExpiringMembershipItem {
        private String memberCode;
        private String memberName;
        private String planName;
        private LocalDate endDate;
        private Long daysRemaining;
        private String phone;

        public ExpiringMembershipItem() {
        }

        public ExpiringMembershipItem(String memberCode, String memberName, String planName, LocalDate endDate, Long daysRemaining, String phone) {
            this.memberCode = memberCode;
            this.memberName = memberName;
            this.planName = planName;
            this.endDate = endDate;
            this.daysRemaining = daysRemaining;
            this.phone = phone;
        }

        public String getMemberCode() {
            return memberCode;
        }

        public void setMemberCode(String memberCode) {
            this.memberCode = memberCode;
        }

        public String getMemberName() {
            return memberName;
        }

        public void setMemberName(String memberName) {
            this.memberName = memberName;
        }

        public String getPlanName() {
            return planName;
        }

        public void setPlanName(String planName) {
            this.planName = planName;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public void setEndDate(LocalDate endDate) {
            this.endDate = endDate;
        }

        public Long getDaysRemaining() {
            return daysRemaining;
        }

        public void setDaysRemaining(Long daysRemaining) {
            this.daysRemaining = daysRemaining;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }

    public Long getTotalActiveMemberships() {
        return totalActiveMemberships;
    }

    public void setTotalActiveMemberships(Long totalActiveMemberships) {
        this.totalActiveMemberships = totalActiveMemberships;
    }

    public Long getTotalExpiredMemberships() {
        return totalExpiredMemberships;
    }

    public void setTotalExpiredMemberships(Long totalExpiredMemberships) {
        this.totalExpiredMemberships = totalExpiredMemberships;
    }

    public Long getExpiringWithin7Days() {
        return expiringWithin7Days;
    }

    public void setExpiringWithin7Days(Long expiringWithin7Days) {
        this.expiringWithin7Days = expiringWithin7Days;
    }

    public Long getExpiringWithin30Days() {
        return expiringWithin30Days;
    }

    public void setExpiringWithin30Days(Long expiringWithin30Days) {
        this.expiringWithin30Days = expiringWithin30Days;
    }

    public List<PlanBreakdownMetric> getPlanBreakdown() {
        return planBreakdown;
    }

    public void setPlanBreakdown(List<PlanBreakdownMetric> planBreakdown) {
        this.planBreakdown = planBreakdown;
    }

    public List<ExpiringMembershipItem> getExpiringSoonMembers() {
        return expiringSoonMembers;
    }

    public void setExpiringSoonMembers(List<ExpiringMembershipItem> expiringSoonMembers) {
        this.expiringSoonMembers = expiringSoonMembers;
    }

    private static String formatCurrency(BigDecimal amount) {
        if (amount == null) amount = BigDecimal.ZERO;
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        return nf.format(amount).replace("INR", "₹").trim();
    }
}
