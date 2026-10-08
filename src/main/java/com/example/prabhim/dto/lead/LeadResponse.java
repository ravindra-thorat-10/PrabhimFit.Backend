package com.example.prabhim.dto.lead;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.example.prabhim.entity.Lead;
import com.example.prabhim.entity.enums.Gender;
import com.example.prabhim.entity.enums.LeadPaymentStatus;
import com.example.prabhim.entity.enums.LeadPriority;
import com.example.prabhim.entity.enums.LeadSource;
import com.example.prabhim.entity.enums.LeadStatus;
import com.example.prabhim.entity.enums.PaymentModePreference;

public class LeadResponse {

    private UUID id;
    private String leadCode;

    // Section 1: Personal & Demographic Details
    private String fullName;
    private String firstName;
    private String lastName;
    private String phone;
    private Gender gender;
    private Integer age;
    private String profession;
    private String residentialArea;
    private String email;
    private String emergencyContact;

    // Section 2: Fitness Goal & Physical Profile
    private String fitnessGoal;
    private String physicalProfileNotes;

    // Section 3: Membership Plan & Commercials / Fees
    private UUID planId;
    private String planName;
    private BigDecimal standardPrice;
    private BigDecimal quotedFee;
    private BigDecimal advanceToken;
    private BigDecimal pendingPlanFee;
    private LeadPaymentStatus paymentStatus;
    private PaymentModePreference paymentModePreference;

    // Section 4: Follow-up Schedule & Conversion Pipeline
    private LocalDate followUpDate;
    private String followUpTime;
    private LocalDate expectedJoiningDate;
    private LeadSource inquirySource;
    private LeadPriority priority;
    private LeadStatus status;

    // Section 5: Initial Discussion Notes & Reminders
    private String discussionNotes;
    private Boolean whatsappReminder;
    private Boolean phoneCallTask;
    private Boolean paymentDuesReminder;

    // System & Helpers
    private UUID assignedToUserId;
    private String assignedToName;
    private UUID convertedMemberId;
    private LocalDateTime convertedAt;
    private Boolean actionRequiredToday;
    private Boolean paymentPending;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public LeadResponse() {
    }

    public static LeadResponse fromEntity(Lead lead) {
        if (lead == null) {
            return null;
        }

        LeadResponse dto = new LeadResponse();
        dto.setId(lead.getId());
        dto.setLeadCode(lead.getLeadCode());
        dto.setFullName(lead.getFullName());
        dto.setFirstName(lead.getFirstName());
        dto.setLastName(lead.getLastName());
        dto.setPhone(lead.getPhone());
        dto.setGender(lead.getGender());
        dto.setAge(lead.getAge());
        dto.setProfession(lead.getProfession());
        dto.setResidentialArea(lead.getResidentialArea());
        dto.setEmail(lead.getEmail());
        dto.setEmergencyContact(lead.getEmergencyContact());

        dto.setFitnessGoal(lead.getFitnessGoal());
        dto.setPhysicalProfileNotes(lead.getPhysicalProfileNotes());

        dto.setPlanId(lead.getPlanId());
        dto.setPlanName(lead.getPlanName());
        dto.setStandardPrice(lead.getStandardPrice());
        dto.setQuotedFee(lead.getQuotedFee());
        dto.setAdvanceToken(lead.getAdvanceToken());
        dto.setPendingPlanFee(lead.getPendingPlanFee());
        dto.setPaymentStatus(lead.getPaymentStatus());
        dto.setPaymentModePreference(lead.getPaymentModePreference());

        dto.setFollowUpDate(lead.getFollowUpDate());
        dto.setFollowUpTime(lead.getFollowUpTime());
        dto.setExpectedJoiningDate(lead.getExpectedJoiningDate());
        dto.setInquirySource(lead.getInquirySource());
        dto.setPriority(lead.getPriority());
        dto.setStatus(lead.getStatus());

        dto.setDiscussionNotes(lead.getDiscussionNotes());
        dto.setWhatsappReminder(lead.getWhatsappReminder());
        dto.setPhoneCallTask(lead.getPhoneCallTask());
        dto.setPaymentDuesReminder(lead.getPaymentDuesReminder());

        if (lead.getAssignedTo() != null) {
            dto.setAssignedToUserId(lead.getAssignedTo().getId());
            String fName = lead.getAssignedTo().getFirstName() != null ? lead.getAssignedTo().getFirstName() : "";
            String lName = lead.getAssignedTo().getLastName() != null ? lead.getAssignedTo().getLastName() : "";
            dto.setAssignedToName((fName + " " + lName).trim());
        }

        dto.setConvertedMemberId(lead.getConvertedMemberId());
        dto.setConvertedAt(lead.getConvertedAt());

        LocalDate today = LocalDate.now();
        boolean isDue = lead.getFollowUpDate() != null && !lead.getFollowUpDate().isAfter(today);
        boolean isClosed = lead.getStatus() == LeadStatus.WON || lead.getStatus() == LeadStatus.CONVERTED || lead.getStatus() == LeadStatus.LOST;
        dto.setActionRequiredToday(isDue && !isClosed);

        dto.setPaymentPending(lead.getPendingPlanFee() != null && lead.getPendingPlanFee().compareTo(BigDecimal.ZERO) > 0);

        dto.setCreatedAt(lead.getCreatedAt());
        dto.setUpdatedAt(lead.getUpdatedAt());

        return dto;
    }

    // Getters and Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLeadCode() {
        return leadCode;
    }

    public void setLeadCode(String leadCode) {
        this.leadCode = leadCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getResidentialArea() {
        return residentialArea;
    }

    public void setResidentialArea(String residentialArea) {
        this.residentialArea = residentialArea;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getFitnessGoal() {
        return fitnessGoal;
    }

    public void setFitnessGoal(String fitnessGoal) {
        this.fitnessGoal = fitnessGoal;
    }

    public String getPhysicalProfileNotes() {
        return physicalProfileNotes;
    }

    public void setPhysicalProfileNotes(String physicalProfileNotes) {
        this.physicalProfileNotes = physicalProfileNotes;
    }

    public UUID getPlanId() {
        return planId;
    }

    public void setPlanId(UUID planId) {
        this.planId = planId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public BigDecimal getStandardPrice() {
        return standardPrice;
    }

    public void setStandardPrice(BigDecimal standardPrice) {
        this.standardPrice = standardPrice;
    }

    public BigDecimal getQuotedFee() {
        return quotedFee;
    }

    public void setQuotedFee(BigDecimal quotedFee) {
        this.quotedFee = quotedFee;
    }

    public BigDecimal getAdvanceToken() {
        return advanceToken;
    }

    public void setAdvanceToken(BigDecimal advanceToken) {
        this.advanceToken = advanceToken;
    }

    public BigDecimal getPendingPlanFee() {
        return pendingPlanFee;
    }

    public void setPendingPlanFee(BigDecimal pendingPlanFee) {
        this.pendingPlanFee = pendingPlanFee;
    }

    public LeadPaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(LeadPaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public PaymentModePreference getPaymentModePreference() {
        return paymentModePreference;
    }

    public void setPaymentModePreference(PaymentModePreference paymentModePreference) {
        this.paymentModePreference = paymentModePreference;
    }

    public LocalDate getFollowUpDate() {
        return followUpDate;
    }

    public void setFollowUpDate(LocalDate followUpDate) {
        this.followUpDate = followUpDate;
    }

    public String getFollowUpTime() {
        return followUpTime;
    }

    public void setFollowUpTime(String followUpTime) {
        this.followUpTime = followUpTime;
    }

    public LocalDate getExpectedJoiningDate() {
        return expectedJoiningDate;
    }

    public void setExpectedJoiningDate(LocalDate expectedJoiningDate) {
        this.expectedJoiningDate = expectedJoiningDate;
    }

    public LeadSource getInquirySource() {
        return inquirySource;
    }

    public void setInquirySource(LeadSource inquirySource) {
        this.inquirySource = inquirySource;
    }

    public LeadPriority getPriority() {
        return priority;
    }

    public void setPriority(LeadPriority priority) {
        this.priority = priority;
    }

    public LeadStatus getStatus() {
        return status;
    }

    public void setStatus(LeadStatus status) {
        this.status = status;
    }

    public String getDiscussionNotes() {
        return discussionNotes;
    }

    public void setDiscussionNotes(String discussionNotes) {
        this.discussionNotes = discussionNotes;
    }

    public Boolean getWhatsappReminder() {
        return whatsappReminder;
    }

    public void setWhatsappReminder(Boolean whatsappReminder) {
        this.whatsappReminder = whatsappReminder;
    }

    public Boolean getPhoneCallTask() {
        return phoneCallTask;
    }

    public void setPhoneCallTask(Boolean phoneCallTask) {
        this.phoneCallTask = phoneCallTask;
    }

    public Boolean getPaymentDuesReminder() {
        return paymentDuesReminder;
    }

    public void setPaymentDuesReminder(Boolean paymentDuesReminder) {
        this.paymentDuesReminder = paymentDuesReminder;
    }

    public UUID getAssignedToUserId() {
        return assignedToUserId;
    }

    public void setAssignedToUserId(UUID assignedToUserId) {
        this.assignedToUserId = assignedToUserId;
    }

    public String getAssignedToName() {
        return assignedToName;
    }

    public void setAssignedToName(String assignedToName) {
        this.assignedToName = assignedToName;
    }

    public UUID getConvertedMemberId() {
        return convertedMemberId;
    }

    public void setConvertedMemberId(UUID convertedMemberId) {
        this.convertedMemberId = convertedMemberId;
    }

    public LocalDateTime getConvertedAt() {
        return convertedAt;
    }

    public void setConvertedAt(LocalDateTime convertedAt) {
        this.convertedAt = convertedAt;
    }

    public Boolean getActionRequiredToday() {
        return actionRequiredToday;
    }

    public void setActionRequiredToday(Boolean actionRequiredToday) {
        this.actionRequiredToday = actionRequiredToday;
    }

    public Boolean getPaymentPending() {
        return paymentPending;
    }

    public void setPaymentPending(Boolean paymentPending) {
        this.paymentPending = paymentPending;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
