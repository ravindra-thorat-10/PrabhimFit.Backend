package com.example.prabhim.dto.member;

import java.time.LocalDateTime;
import java.util.List;

public class AttendanceSummaryDto {

    private long totalVisits;
    private long visitsThisMonth;
    private LocalDateTime lastCheckIn;
    private List<AttendanceRecordDto> recentRecords;

    public AttendanceSummaryDto() {
    }

    public AttendanceSummaryDto(long totalVisits, long visitsThisMonth, LocalDateTime lastCheckIn, List<AttendanceRecordDto> recentRecords) {
        this.totalVisits = totalVisits;
        this.visitsThisMonth = visitsThisMonth;
        this.lastCheckIn = lastCheckIn;
        this.recentRecords = recentRecords;
    }

    public long getTotalVisits() {
        return totalVisits;
    }

    public void setTotalVisits(long totalVisits) {
        this.totalVisits = totalVisits;
    }

    public long getVisitsThisMonth() {
        return visitsThisMonth;
    }

    public void setVisitsThisMonth(long visitsThisMonth) {
        this.visitsThisMonth = visitsThisMonth;
    }

    public LocalDateTime getLastCheckIn() {
        return lastCheckIn;
    }

    public void setLastCheckIn(LocalDateTime lastCheckIn) {
        this.lastCheckIn = lastCheckIn;
    }

    public List<AttendanceRecordDto> getRecentRecords() {
        return recentRecords;
    }

    public void setRecentRecords(List<AttendanceRecordDto> recentRecords) {
        this.recentRecords = recentRecords;
    }
}
