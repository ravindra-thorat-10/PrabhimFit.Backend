package com.example.prabhim.dto.attendance;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

import com.example.prabhim.entity.Attendance;
import com.example.prabhim.entity.Member;

public class AttendanceResponse {

    private UUID id;
    private UUID memberId;
    private String memberName;
    private String memberCode;
    private String memberAvatar;
    private String memberPhone;
    private LocalDateTime checkInTime;
    private String checkInTimeFormatted;
    private LocalDateTime checkOutTime;
    private String checkOutTimeFormatted;
    private boolean checkedOut;
    private String status;
    private String verifiedBy;
    private String facility;
    private String method;
    private LocalDateTime createdAt;

    public AttendanceResponse() {
    }

    public static AttendanceResponse fromEntity(Attendance att) {
        if (att == null) {
            return null;
        }
        AttendanceResponse resp = new AttendanceResponse();
        resp.setId(att.getId());
        if (att.getMember() != null) {
            Member m = att.getMember();
            resp.setMemberId(m.getId());
            resp.setMemberName(m.getFirstName() + " " + (m.getLastName() != null ? m.getLastName() : "").trim());
            resp.setMemberCode(m.getMemberCode());
            resp.setMemberAvatar(m.getAvatar());
            resp.setMemberPhone(m.getPhone());
        }
        resp.setCheckInTime(att.getCheckInTime());
        if (att.getCheckInTime() != null) {
            resp.setCheckInTimeFormatted(formatTime(att.getCheckInTime()));
        }
        resp.setCheckOutTime(att.getCheckOutTime());
        if (att.getCheckOutTime() != null) {
            resp.setCheckOutTimeFormatted(formatTime(att.getCheckOutTime()));
            resp.setCheckedOut(true);
        } else {
            resp.setCheckedOut(false);
        }
        resp.setStatus(att.getStatus() != null ? att.getStatus() : "PRESENT");
        resp.setVerifiedBy(att.getVerifiedBy() != null ? att.getVerifiedBy() : "Marcus Vance");
        resp.setFacility(att.getFacility() != null ? att.getFacility() : "Downtown Flagship - Studio Turnstiles");
        resp.setMethod(att.getMethod() != null ? att.getMethod() : "RFID");
        resp.setCreatedAt(att.getCreatedAt());
        return resp;
    }

    private static String formatTime(LocalDateTime dt) {
        if (dt == null) return "";
        return dt.format(DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH));
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getMemberId() {
        return memberId;
    }

    public void setMemberId(UUID memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public String getMemberAvatar() {
        return memberAvatar;
    }

    public void setMemberAvatar(String memberAvatar) {
        this.memberAvatar = memberAvatar;
    }

    public String getMemberPhone() {
        return memberPhone;
    }

    public void setMemberPhone(String memberPhone) {
        this.memberPhone = memberPhone;
    }

    public LocalDateTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(LocalDateTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getCheckInTimeFormatted() {
        return checkInTimeFormatted;
    }

    public void setCheckInTimeFormatted(String checkInTimeFormatted) {
        this.checkInTimeFormatted = checkInTimeFormatted;
    }

    public LocalDateTime getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(LocalDateTime checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public String getCheckOutTimeFormatted() {
        return checkOutTimeFormatted;
    }

    public void setCheckOutTimeFormatted(String checkOutTimeFormatted) {
        this.checkOutTimeFormatted = checkOutTimeFormatted;
    }

    public boolean isCheckedOut() {
        return checkedOut;
    }

    public void setCheckedOut(boolean checkedOut) {
        this.checkedOut = checkedOut;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(String verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
