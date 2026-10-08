package com.example.prabhim.dto.lead;

public class LeadStatsResponse {

    private long todaysDue;
    private long weeklyCalls;
    private long thisMonth;
    private long paymentDue;
    private long visitors;
    private long joinedGym;
    private double conversionRate;
    private long totalLeads;
    private long actionRequiredToday;

    public LeadStatsResponse() {
    }

    public LeadStatsResponse(
            long todaysDue,
            long weeklyCalls,
            long thisMonth,
            long paymentDue,
            long visitors,
            long joinedGym,
            double conversionRate,
            long totalLeads,
            long actionRequiredToday) {
        this.todaysDue = todaysDue;
        this.weeklyCalls = weeklyCalls;
        this.thisMonth = thisMonth;
        this.paymentDue = paymentDue;
        this.visitors = visitors;
        this.joinedGym = joinedGym;
        this.conversionRate = conversionRate;
        this.totalLeads = totalLeads;
        this.actionRequiredToday = actionRequiredToday;
    }

    public long getTodaysDue() {
        return todaysDue;
    }

    public void setTodaysDue(long todaysDue) {
        this.todaysDue = todaysDue;
    }

    public long getWeeklyCalls() {
        return weeklyCalls;
    }

    public void setWeeklyCalls(long weeklyCalls) {
        this.weeklyCalls = weeklyCalls;
    }

    public long getThisMonth() {
        return thisMonth;
    }

    public void setThisMonth(long thisMonth) {
        this.thisMonth = thisMonth;
    }

    public long getPaymentDue() {
        return paymentDue;
    }

    public void setPaymentDue(long paymentDue) {
        this.paymentDue = paymentDue;
    }

    public long getVisitors() {
        return visitors;
    }

    public void setVisitors(long visitors) {
        this.visitors = visitors;
    }

    public long getJoinedGym() {
        return joinedGym;
    }

    public void setJoinedGym(long joinedGym) {
        this.joinedGym = joinedGym;
    }

    public double getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(double conversionRate) {
        this.conversionRate = conversionRate;
    }

    public long getTotalLeads() {
        return totalLeads;
    }

    public void setTotalLeads(long totalLeads) {
        this.totalLeads = totalLeads;
    }

    public long getActionRequiredToday() {
        return actionRequiredToday;
    }

    public void setActionRequiredToday(long actionRequiredToday) {
        this.actionRequiredToday = actionRequiredToday;
    }
}
