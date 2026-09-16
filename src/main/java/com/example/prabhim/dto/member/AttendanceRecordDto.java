package com.example.prabhim.dto.member;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.prabhim.entity.Attendance;

public class AttendanceRecordDto {

    private UUID id;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private String facility;
    private String status;

    public AttendanceRecordDto() {
    }

    public static AttendanceRecordDto fromEntity(Attendance attendance) {
        if (attendance == null) {
            return null;
        }
        AttendanceRecordDto dto = new AttendanceRecordDto();
        dto.setId(attendance.getId());
        dto.setCheckInTime(attendance.getCheckInTime());
        dto.setCheckOutTime(attendance.getCheckOutTime());
        dto.setFacility(attendance.getFacility());
        dto.setStatus(attendance.getStatus());
        return dto;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(LocalDateTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    public LocalDateTime getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(LocalDateTime checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
