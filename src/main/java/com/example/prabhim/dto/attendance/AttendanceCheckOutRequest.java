package com.example.prabhim.dto.attendance;

import java.time.LocalDateTime;

public class AttendanceCheckOutRequest {

    private String checkOutTime;
    private LocalDateTime checkOutDateTime;

    public AttendanceCheckOutRequest() {
    }

    public AttendanceCheckOutRequest(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public LocalDateTime getCheckOutDateTime() {
        return checkOutDateTime;
    }

    public void setCheckOutDateTime(LocalDateTime checkOutDateTime) {
        this.checkOutDateTime = checkOutDateTime;
    }
}
