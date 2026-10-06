package com.example.prabhim.dto.report;

import java.time.LocalDate;
import java.util.List;

public class AttendanceReportResponse {

    private Long totalCheckIns;
    private Long onTimeCheckIns;
    private Long lateCheckIns;
    private Double averageDailyCheckIns;

    private List<DailyAttendanceMetric> dailyTrends;
    private List<HourlyAttendanceMetric> peakHours;

    public AttendanceReportResponse() {
    }

    public static class DailyAttendanceMetric {
        private LocalDate date;
        private String dayOfWeek;
        private Long checkInCount;
        private Long onTimeCount;
        private Long lateCount;

        public DailyAttendanceMetric() {
        }

        public DailyAttendanceMetric(LocalDate date, String dayOfWeek, Long checkInCount, Long onTimeCount, Long lateCount) {
            this.date = date;
            this.dayOfWeek = dayOfWeek;
            this.checkInCount = checkInCount != null ? checkInCount : 0L;
            this.onTimeCount = onTimeCount != null ? onTimeCount : 0L;
            this.lateCount = lateCount != null ? lateCount : 0L;
        }

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public String getDayOfWeek() {
            return dayOfWeek;
        }

        public void setDayOfWeek(String dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
        }

        public Long getCheckInCount() {
            return checkInCount;
        }

        public void setCheckInCount(Long checkInCount) {
            this.checkInCount = checkInCount;
        }

        public Long getOnTimeCount() {
            return onTimeCount;
        }

        public void setOnTimeCount(Long onTimeCount) {
            this.onTimeCount = onTimeCount;
        }

        public Long getLateCount() {
            return lateCount;
        }

        public void setLateCount(Long lateCount) {
            this.lateCount = lateCount;
        }
    }

    public static class HourlyAttendanceMetric {
        private String timeSlot;
        private Long count;

        public HourlyAttendanceMetric() {
        }

        public HourlyAttendanceMetric(String timeSlot, Long count) {
            this.timeSlot = timeSlot;
            this.count = count != null ? count : 0L;
        }

        public String getTimeSlot() {
            return timeSlot;
        }

        public void setTimeSlot(String timeSlot) {
            this.timeSlot = timeSlot;
        }

        public Long getCount() {
            return count;
        }

        public void setCount(Long count) {
            this.count = count;
        }
    }

    public Long getTotalCheckIns() {
        return totalCheckIns;
    }

    public void setTotalCheckIns(Long totalCheckIns) {
        this.totalCheckIns = totalCheckIns;
    }

    public Long getOnTimeCheckIns() {
        return onTimeCheckIns;
    }

    public void setOnTimeCheckIns(Long onTimeCheckIns) {
        this.onTimeCheckIns = onTimeCheckIns;
    }

    public Long getLateCheckIns() {
        return lateCheckIns;
    }

    public void setLateCheckIns(Long lateCheckIns) {
        this.lateCheckIns = lateCheckIns;
    }

    public Double getAverageDailyCheckIns() {
        return averageDailyCheckIns;
    }

    public void setAverageDailyCheckIns(Double averageDailyCheckIns) {
        this.averageDailyCheckIns = averageDailyCheckIns;
    }

    public List<DailyAttendanceMetric> getDailyTrends() {
        return dailyTrends;
    }

    public void setDailyTrends(List<DailyAttendanceMetric> dailyTrends) {
        this.dailyTrends = dailyTrends;
    }

    public List<HourlyAttendanceMetric> getPeakHours() {
        return peakHours;
    }

    public void setPeakHours(List<HourlyAttendanceMetric> peakHours) {
        this.peakHours = peakHours;
    }
}
