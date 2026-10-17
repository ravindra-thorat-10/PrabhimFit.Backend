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
    private AttendanceMemberSummary member;

    private LocalDateTime checkInTime;
    private String checkInTimeFormatted;
    private LocalDateTime checkOutTime;
    private String checkOutTimeFormatted;
    private boolean checkedOut;
    private String status;
    private String verifiedBy;
    private String markedBy;
    private String facility;
    private String method;
    private String notes;
    private String date;
    private String sessionDateStr;
    private LocalDateTime createdAt;

    private String tenantId;
    private String creatorEmail;
    private String gymId;

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
            String fullName = m.getFirstName() + " " + (m.getLastName() != null ? m.getLastName() : "").trim();
            resp.setMemberName(fullName);
            resp.setMemberCode(m.getMemberCode());
            resp.setMemberAvatar(m.getAvatar());
            resp.setMemberPhone(m.getPhone());
            resp.setMember(new AttendanceMemberSummary(m.getId(), fullName, m.getMemberCode(), m.getAvatar(), m.getPhone()));
        }
        resp.setCheckInTime(att.getCheckInTime());
        if (att.getCheckInTime() != null) {
            String formattedTime = formatTime(att.getCheckInTime());
            resp.setCheckInTimeFormatted(formattedTime);
            resp.setDate(att.getCheckInTime().toLocalDate().toString());
            resp.setSessionDateStr(att.getCheckInTime().toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        }
        resp.setCheckOutTime(att.getCheckOutTime());
        if (att.getCheckOutTime() != null) {
            resp.setCheckOutTimeFormatted(formatTime(att.getCheckOutTime()));
            resp.setCheckedOut(true);
        } else {
            resp.setCheckedOut(false);
        }
        resp.setStatus(att.getStatus() != null ? att.getStatus() : "PRESENT");
        String verifier = att.getVerifiedBy() != null ? att.getVerifiedBy() : "Marcus Vance";
        resp.setVerifiedBy(verifier);
        resp.setMarkedBy(verifier);
        resp.setFacility(att.getFacility() != null ? att.getFacility() : "Downtown Flagship - Studio Turnstiles");
        resp.setMethod(att.getMethod() != null ? att.getMethod() : "RFID");
        resp.setNotes(att.getNotes());
        resp.setCreatedAt(att.getCreatedAt());

        String email = "";
        try {
            var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getName() != null && !auth.getName().equalsIgnoreCase("anonymousUser")) {
                email = auth.getName();
            }
        } catch (Exception ignored) {}
        resp.setTenantId(email);
        resp.setCreatorEmail(email);
        resp.setGymId(email);

        return resp;
    }

    public static class AttendanceMemberSummary {
        private UUID id;
        private String fullName;
        private String memberCode;
        private String avatar;
        private String phone;

        public AttendanceMemberSummary() {
        }

        public AttendanceMemberSummary(UUID id, String fullName, String memberCode, String avatar, String phone) {
            this.id = id;
            this.fullName = fullName;
            this.memberCode = memberCode;
            this.avatar = avatar;
            this.phone = phone;
        }

        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getMemberCode() {
            return memberCode;
        }

        public void setMemberCode(String memberCode) {
            this.memberCode = memberCode;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
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

    public AttendanceMemberSummary getMember() {
        return member;
    }

    public void setMember(AttendanceMemberSummary member) {
        this.member = member;
    }

    public String getMarkedBy() {
        return markedBy != null ? markedBy : verifiedBy;
    }

    public void setMarkedBy(String markedBy) {
        this.markedBy = markedBy;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSessionDateStr() {
        return sessionDateStr;
    }

    public void setSessionDateStr(String sessionDateStr) {
        this.sessionDateStr = sessionDateStr;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getCreatorEmail() {
        return creatorEmail;
    }

    public void setCreatorEmail(String creatorEmail) {
        this.creatorEmail = creatorEmail;
    }

    public String getGymId() {
        return gymId;
    }

    public void setGymId(String gymId) {
        this.gymId = gymId;
    }
}
