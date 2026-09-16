package com.example.prabhim.dto.trainer;

import java.math.BigDecimal;

public class TrainerStatsResponse {

    private long totalTrainers;
    private long activeTrainers;
    private long onLeaveTrainers;
    private long inactiveTrainers;
    private BigDecimal totalMonthlyPayroll;
    private String totalMonthlyPayrollFormatted;
    private double averageExperienceYears;
    private String mostPopularSpecialization;

    public TrainerStatsResponse() {
    }

    public long getTotalTrainers() {
        return totalTrainers;
    }

    public void setTotalTrainers(long totalTrainers) {
        this.totalTrainers = totalTrainers;
    }

    public long getActiveTrainers() {
        return activeTrainers;
    }

    public void setActiveTrainers(long activeTrainers) {
        this.activeTrainers = activeTrainers;
    }

    public long getOnLeaveTrainers() {
        return onLeaveTrainers;
    }

    public void setOnLeaveTrainers(long onLeaveTrainers) {
        this.onLeaveTrainers = onLeaveTrainers;
    }

    public long getInactiveTrainers() {
        return inactiveTrainers;
    }

    public void setInactiveTrainers(long inactiveTrainers) {
        this.inactiveTrainers = inactiveTrainers;
    }

    public BigDecimal getTotalMonthlyPayroll() {
        return totalMonthlyPayroll;
    }

    public void setTotalMonthlyPayroll(BigDecimal totalMonthlyPayroll) {
        this.totalMonthlyPayroll = totalMonthlyPayroll;
        if (totalMonthlyPayroll != null) {
            this.totalMonthlyPayrollFormatted = formatInr(totalMonthlyPayroll);
        }
    }

    public static String formatInr(BigDecimal amount) {
        if (amount == null) return "₹0";
        long val = amount.longValue();
        if (val < 1000) return "₹" + val;
        String s = String.valueOf(val);
        String lastThree = s.substring(s.length() - 3);
        String remaining = s.substring(0, s.length() - 3);
        StringBuilder sb = new StringBuilder();
        while (remaining.length() > 2) {
            sb.insert(0, "," + remaining.substring(remaining.length() - 2));
            remaining = remaining.substring(0, remaining.length() - 2);
        }
        sb.insert(0, remaining);
        return "₹" + sb.toString() + "," + lastThree;
    }

    public String getTotalMonthlyPayrollFormatted() {
        return totalMonthlyPayrollFormatted;
    }

    public void setTotalMonthlyPayrollFormatted(String totalMonthlyPayrollFormatted) {
        this.totalMonthlyPayrollFormatted = totalMonthlyPayrollFormatted;
    }

    public double getAverageExperienceYears() {
        return averageExperienceYears;
    }

    public void setAverageExperienceYears(double averageExperienceYears) {
        this.averageExperienceYears = averageExperienceYears;
    }

    public String getMostPopularSpecialization() {
        return mostPopularSpecialization;
    }

    public void setMostPopularSpecialization(String mostPopularSpecialization) {
        this.mostPopularSpecialization = mostPopularSpecialization;
    }
}
