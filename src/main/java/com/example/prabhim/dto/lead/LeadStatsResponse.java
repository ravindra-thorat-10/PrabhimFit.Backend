package com.example.prabhim.dto.lead;

import java.math.BigDecimal;

public class LeadStatsResponse {

    private long totalLeads;
    private long newLeads;
    private long followUpLeads;
    private long trialLeads;
    private long convertedLeads;
    private long paymentPendingLeads;
    private long lostLeads;

    private BigDecimal totalQuotedRevenue = BigDecimal.ZERO;
    private BigDecimal totalDepositsCollected = BigDecimal.ZERO;
    private BigDecimal totalBalanceDue = BigDecimal.ZERO;

    public LeadStatsResponse() {
    }

    public long getTotalLeads() {
        return totalLeads;
    }

    public void setTotalLeads(long totalLeads) {
        this.totalLeads = totalLeads;
    }

    public long getNewLeads() {
        return newLeads;
    }

    public void setNewLeads(long newLeads) {
        this.newLeads = newLeads;
    }

    public long getFollowUpLeads() {
        return followUpLeads;
    }

    public void setFollowUpLeads(long followUpLeads) {
        this.followUpLeads = followUpLeads;
    }

    public long getTrialLeads() {
        return trialLeads;
    }

    public void setTrialLeads(long trialLeads) {
        this.trialLeads = trialLeads;
    }

    public long getConvertedLeads() {
        return convertedLeads;
    }

    public void setConvertedLeads(long convertedLeads) {
        this.convertedLeads = convertedLeads;
    }

    public long getPaymentPendingLeads() {
        return paymentPendingLeads;
    }

    public void setPaymentPendingLeads(long paymentPendingLeads) {
        this.paymentPendingLeads = paymentPendingLeads;
    }

    public long getLostLeads() {
        return lostLeads;
    }

    public void setLostLeads(long lostLeads) {
        this.lostLeads = lostLeads;
    }

    public BigDecimal getTotalQuotedRevenue() {
        return totalQuotedRevenue;
    }

    public void setTotalQuotedRevenue(BigDecimal totalQuotedRevenue) {
        this.totalQuotedRevenue = totalQuotedRevenue;
    }

    public BigDecimal getTotalDepositsCollected() {
        return totalDepositsCollected;
    }

    public void setTotalDepositsCollected(BigDecimal totalDepositsCollected) {
        this.totalDepositsCollected = totalDepositsCollected;
    }

    public BigDecimal getTotalBalanceDue() {
        return totalBalanceDue;
    }

    public void setTotalBalanceDue(BigDecimal totalBalanceDue) {
        this.totalBalanceDue = totalBalanceDue;
    }
}
