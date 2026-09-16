package com.example.prabhim.dto.attendance;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

public class AttendanceMarkRequest {

    private UUID memberId;
    private String memberIdStr;
    private String memberCode;
    private String memberName;

    private LocalDate sessionDate;
    private String sessionDateStr;

    private String checkInTime;
    private String checkOutTime;

    private String status = "PRESENT";
    private String facility;
    private String verifiedBy;

    public AttendanceMarkRequest() {
    }

    public UUID resolveMemberId() {
        if (memberId != null) {
            return memberId;
        }
        if (memberIdStr != null && !memberIdStr.trim().isEmpty()) {
            try {
                return UUID.fromString(memberIdStr.trim());
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    public LocalDate resolveSessionDate() {
        if (sessionDate != null) {
            return sessionDate;
        }
        if (sessionDateStr != null && !sessionDateStr.trim().isEmpty()) {
            String str = sessionDateStr.trim();
            try {
                if (str.contains("-")) {
                    String[] parts = str.split("-");
                    if (parts[0].length() == 4) {
                        return LocalDate.parse(str, DateTimeFormatter.ISO_LOCAL_DATE);
                    } else if (parts[2].length() == 4) {
                        return LocalDate.parse(str, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                    }
                }
            } catch (Exception ignored) {
            }
        }
        return LocalDate.now();
    }

    public LocalDateTime resolveCheckInTime(LocalDate date) {
        if (checkInTime == null || checkInTime.trim().isEmpty()) {
            return LocalDateTime.now();
        }
        LocalTime time = parseTimeString(checkInTime.trim());
        return time != null ? LocalDateTime.of(date, time) : LocalDateTime.now();
    }

    public LocalDateTime resolveCheckOutTime(LocalDate date) {
        if (checkOutTime == null || checkOutTime.trim().isEmpty()) {
            return null;
        }
        LocalTime time = parseTimeString(checkOutTime.trim());
        return time != null ? LocalDateTime.of(date, time) : null;
    }

    private LocalTime parseTimeString(String s) {
        String clean = s.trim().toUpperCase(Locale.ROOT);
        try {
            if (clean.contains("AM") || clean.contains("PM")) {
                // e.g. "07:14 PM" or "7:14 PM"
                clean = clean.replace("  ", " ");
                try {
                    return LocalTime.parse(clean, DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH));
                } catch (Exception e) {
                    return LocalTime.parse(clean, DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH));
                }
            } else if (clean.contains(":")) {
                // e.g. "19:14" or "07:14"
                return LocalTime.parse(clean, DateTimeFormatter.ofPattern("HH:mm"));
            }
        } catch (Exception ignored) {
        }
        return LocalTime.now();
    }

    public UUID getMemberId() {
        return memberId;
    }

    public void setMemberId(UUID memberId) {
        this.memberId = memberId;
    }

    public String getMemberIdStr() {
        return memberIdStr;
    }

    public void setMemberIdStr(String memberIdStr) {
        this.memberIdStr = memberIdStr;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public LocalDate getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(LocalDate sessionDate) {
        this.sessionDate = sessionDate;
    }

    public String getSessionDateStr() {
        return sessionDateStr;
    }

    public void setSessionDateStr(String sessionDateStr) {
        this.sessionDateStr = sessionDateStr;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public String getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(String verifiedBy) {
        this.verifiedBy = verifiedBy;
    }
}
