package com.example.prabhim.dto.member;

public class MemberStatsResponse {

    private long totalRecords;
    private long activeSubscriptions;
    private Double activeGrowthPercentage;
    private long expiring7Days;
    private Double expiringGrowthPercentage;
    private long temporaryHolds;
    private Double holdsPercentage;
    private long lapsedMemberships;

    public MemberStatsResponse() {
    }

    public MemberStatsResponse(long totalRecords, long activeSubscriptions, Double activeGrowthPercentage,
                               long expiring7Days, Double expiringGrowthPercentage,
                               long temporaryHolds, Double holdsPercentage, long lapsedMemberships) {
        this.totalRecords = totalRecords;
        this.activeSubscriptions = activeSubscriptions;
        this.activeGrowthPercentage = activeGrowthPercentage;
        this.expiring7Days = expiring7Days;
        this.expiringGrowthPercentage = expiringGrowthPercentage;
        this.temporaryHolds = temporaryHolds;
        this.holdsPercentage = holdsPercentage;
        this.lapsedMemberships = lapsedMemberships;
    }

    public long getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(long totalRecords) {
        this.totalRecords = totalRecords;
    }

    public long getActiveSubscriptions() {
        return activeSubscriptions;
    }

    public void setActiveSubscriptions(long activeSubscriptions) {
        this.activeSubscriptions = activeSubscriptions;
    }

    public Double getActiveGrowthPercentage() {
        return activeGrowthPercentage;
    }

    public void setActiveGrowthPercentage(Double activeGrowthPercentage) {
        this.activeGrowthPercentage = activeGrowthPercentage;
    }

    public long getExpiring7Days() {
        return expiring7Days;
    }

    public void setExpiring7Days(long expiring7Days) {
        this.expiring7Days = expiring7Days;
    }

    public Double getExpiringGrowthPercentage() {
        return expiringGrowthPercentage;
    }

    public void setExpiringGrowthPercentage(Double expiringGrowthPercentage) {
        this.expiringGrowthPercentage = expiringGrowthPercentage;
    }

    public long getTemporaryHolds() {
        return temporaryHolds;
    }

    public void setTemporaryHolds(long temporaryHolds) {
        this.temporaryHolds = temporaryHolds;
    }

    public Double getHoldsPercentage() {
        return holdsPercentage;
    }

    public void setHoldsPercentage(Double holdsPercentage) {
        this.holdsPercentage = holdsPercentage;
    }

    public long getLapsedMemberships() {
        return lapsedMemberships;
    }

    public void setLapsedMemberships(long lapsedMemberships) {
        this.lapsedMemberships = lapsedMemberships;
    }
}
