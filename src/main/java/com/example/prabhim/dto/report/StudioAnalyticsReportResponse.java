package com.example.prabhim.dto.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class StudioAnalyticsReportResponse {

    private MembersMetric members;
    private RevenueMetric revenue;
    private AttendanceMetric attendance;
    private List<TrainerRosterItem> trainers = new ArrayList<>();

    public StudioAnalyticsReportResponse() {
    }

    public StudioAnalyticsReportResponse(MembersMetric members, RevenueMetric revenue, AttendanceMetric attendance, List<TrainerRosterItem> trainers) {
        this.members = members;
        this.revenue = revenue;
        this.attendance = attendance;
        this.trainers = trainers != null ? trainers : new ArrayList<>();
    }

    // 1. Members Metric
    public static class MembersMetric {
        private Long total = 0L;
        private Long active = 0L;
        private Long expired = 0L;
        private Long pending = 0L;
        private Double retentionRate = 100.0;

        public MembersMetric() {
        }

        public MembersMetric(Long total, Long active, Long expired, Long pending, Double retentionRate) {
            this.total = total != null ? total : 0L;
            this.active = active != null ? active : 0L;
            this.expired = expired != null ? expired : 0L;
            this.pending = pending != null ? pending : 0L;
            this.retentionRate = retentionRate != null ? retentionRate : 100.0;
        }

        public Long getTotal() {
            return total;
        }

        public void setTotal(Long total) {
            this.total = total;
        }

        public Long getActive() {
            return active;
        }

        public void setActive(Long active) {
            this.active = active;
        }

        public Long getExpired() {
            return expired;
        }

        public void setExpired(Long expired) {
            this.expired = expired;
        }

        public Long getPending() {
            return pending;
        }

        public void setPending(Long pending) {
            this.pending = pending;
        }

        public Double getRetentionRate() {
            return retentionRate;
        }

        public void setRetentionRate(Double retentionRate) {
            this.retentionRate = retentionRate;
        }
    }

    // 2. Revenue Metric
    public static class RevenueMetric {
        private BigDecimal total = BigDecimal.ZERO;
        private List<MonthlyTrendItem> monthlyTrend = new ArrayList<>();

        public RevenueMetric() {
        }

        public RevenueMetric(BigDecimal total, List<MonthlyTrendItem> monthlyTrend) {
            this.total = total != null ? total : BigDecimal.ZERO;
            this.monthlyTrend = monthlyTrend != null ? monthlyTrend : new ArrayList<>();
        }

        public BigDecimal getTotal() {
            return total;
        }

        public void setTotal(BigDecimal total) {
            this.total = total;
        }

        public List<MonthlyTrendItem> getMonthlyTrend() {
            return monthlyTrend;
        }

        public void setMonthlyTrend(List<MonthlyTrendItem> monthlyTrend) {
            this.monthlyTrend = monthlyTrend;
        }
    }

    public static class MonthlyTrendItem {
        private String month;
        private BigDecimal revenue = BigDecimal.ZERO;

        public MonthlyTrendItem() {
        }

        public MonthlyTrendItem(String month, BigDecimal revenue) {
            this.month = month;
            this.revenue = revenue != null ? revenue : BigDecimal.ZERO;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public BigDecimal getRevenue() {
            return revenue;
        }

        public void setRevenue(BigDecimal revenue) {
            this.revenue = revenue;
        }
    }

    // 3. Attendance Metric
    public static class AttendanceMetric {
        private Long averageDaily = 0L;
        private String peakHour = "—";
        private List<DailyTrendItem> dailyTrend = new ArrayList<>();

        public AttendanceMetric() {
        }

        public AttendanceMetric(Long averageDaily, String peakHour, List<DailyTrendItem> dailyTrend) {
            this.averageDaily = averageDaily != null ? averageDaily : 0L;
            this.peakHour = peakHour != null ? peakHour : "—";
            this.dailyTrend = dailyTrend != null ? dailyTrend : new ArrayList<>();
        }

        public Long getAverageDaily() {
            return averageDaily;
        }

        public void setAverageDaily(Long averageDaily) {
            this.averageDaily = averageDaily;
        }

        public String getPeakHour() {
            return peakHour;
        }

        public void setPeakHour(String peakHour) {
            this.peakHour = peakHour;
        }

        public List<DailyTrendItem> getDailyTrend() {
            return dailyTrend;
        }

        public void setDailyTrend(List<DailyTrendItem> dailyTrend) {
            this.dailyTrend = dailyTrend;
        }
    }

    public static class DailyTrendItem {
        private String date;
        private Long attendance = 0L;

        public DailyTrendItem() {
        }

        public DailyTrendItem(String date, Long attendance) {
            this.date = date;
            this.attendance = attendance != null ? attendance : 0L;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Long getAttendance() {
            return attendance;
        }

        public void setAttendance(Long attendance) {
            this.attendance = attendance;
        }
    }

    // 4. Trainer Roster Item
    public static class TrainerRosterItem {
        private String id;
        private String name;
        private String specialization;
        private Integer assignedMembers = 0;
        private BigDecimal salary = BigDecimal.ZERO;

        public TrainerRosterItem() {
        }

        public TrainerRosterItem(String id, String name, String specialization, Integer assignedMembers, BigDecimal salary) {
            this.id = id;
            this.name = name;
            this.specialization = specialization;
            this.assignedMembers = assignedMembers != null ? assignedMembers : 0;
            this.salary = salary != null ? salary : BigDecimal.ZERO;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSpecialization() {
            return specialization;
        }

        public void setSpecialization(String specialization) {
            this.specialization = specialization;
        }

        public Integer getAssignedMembers() {
            return assignedMembers;
        }

        public void setAssignedMembers(Integer assignedMembers) {
            this.assignedMembers = assignedMembers;
        }

        public BigDecimal getSalary() {
            return salary;
        }

        public void setSalary(BigDecimal salary) {
            this.salary = salary;
        }
    }

    // Getters and Setters
    public MembersMetric getMembers() {
        return members;
    }

    public void setMembers(MembersMetric members) {
        this.members = members;
    }

    public RevenueMetric getRevenue() {
        return revenue;
    }

    public void setRevenue(RevenueMetric revenue) {
        this.revenue = revenue;
    }

    public AttendanceMetric getAttendance() {
        return attendance;
    }

    public void setAttendance(AttendanceMetric attendance) {
        this.attendance = attendance;
    }

    public List<TrainerRosterItem> getTrainers() {
        return trainers;
    }

    public void setTrainers(List<TrainerRosterItem> trainers) {
        this.trainers = trainers;
    }
}
