package com.example.prabhim.dto.lead;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.example.prabhim.entity.enums.Gender;
import com.example.prabhim.entity.enums.LeadPaymentStatus;
import com.example.prabhim.entity.enums.LeadPriority;
import com.example.prabhim.entity.enums.LeadSource;
import com.example.prabhim.entity.enums.LeadStatus;
import com.example.prabhim.entity.enums.PaymentModePreference;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class LeadUpdateRequest {

    @NotBlank(message = "Full Name is required")
    @Size(max = 150, message = "Full Name cannot exceed 150 characters")
    @JsonAlias({"name", "candidateName"})
    private String fullName;

    @NotBlank(message = "Mobile / WhatsApp Number is required")
    @Size(max = 25, message = "Phone number cannot exceed 25 characters")
    @JsonAlias({"mobile", "mobileNumber", "whatsAppNumber", "phoneNumber"})
    private String phone;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotNull(message = "Age is required")
    @Min(value = 10, message = "Age must be at least 10")
    private Integer age;

    @JsonAlias({"company", "professionCompany", "occupation"})
    private String profession;

    @JsonAlias({"city", "area", "residentialAreaCity", "address"})
    private String residentialArea;

    @JsonAlias({"emailAddress"})
    private String email;

    @JsonAlias({"emergencyContactNamePhone", "emergencyContactPhone", "emergencyPhone"})
    private String emergencyContact;

    @JsonAlias({"goal", "primaryGoal"})
    private String fitnessGoal;

    @JsonAlias({"physicalProfile", "notes", "profileNotes"})
    private String physicalProfileNotes;

    private UUID planId;

    @JsonAlias({"selectedPlan", "membershipPlan"})
    private String planName;

    @JsonAlias({"standardPriceFee"})
    private BigDecimal standardPrice;

    @JsonAlias({"planRate", "rate", "fee", "amount"})
    private BigDecimal quotedFee;

    @JsonAlias({"token", "initialDeposit", "advancePaid"})
    private BigDecimal advanceToken;

    private BigDecimal pendingPlanFee;

    private LeadPaymentStatus paymentStatus;

    @JsonAlias({"paymentMode", "paymentPreference", "mode"})
    private PaymentModePreference paymentModePreference;

    @NotNull(message = "Follow-up Date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonAlias({"followupDate", "nextFollowUpDate"})
    private LocalDate followUpDate;

    @JsonAlias({"followupTime", "nextFollowUpTime", "time"})
    private String followUpTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonAlias({"joiningDate", "expectedDate"})
    private LocalDate expectedJoiningDate;

    @JsonAlias({"source", "sourceOfInquiry"})
    private LeadSource inquirySource;

    @JsonAlias({"priorityLevel", "pipelinePriorityLevel"})
    private LeadPriority priority;

    private LeadStatus status;

    @JsonAlias({"notesDiscussion", "discussion", "consultationNotes"})
    private String discussionNotes;

    @JsonAlias({"autoMessageWhatsapp", "whatsappAlert"})
    private Boolean whatsappReminder;

    @JsonAlias({"callTask", "addToCallDeck"})
    private Boolean phoneCallTask;

    @JsonAlias({"sendPaymentLink", "upiReminder"})
    private Boolean paymentDuesReminder;

    private UUID assignedToUserId;

    public LeadUpdateRequest() {
    }

    // Getters and Setters

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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
}
