package com.example.prabhim.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.example.prabhim.entity.enums.Gender;
import com.example.prabhim.entity.enums.LeadPaymentStatus;
import com.example.prabhim.entity.enums.LeadPriority;
import com.example.prabhim.entity.enums.LeadSource;
import com.example.prabhim.entity.enums.LeadStatus;
import com.example.prabhim.entity.enums.PaymentModePreference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "leads")
public class Lead {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "lead_code", unique = true, nullable = false)
    private String leadCode;

    // --- Section 1: Personal & Demographic Details ---
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(nullable = false)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender = Gender.MALE;

    @Column(nullable = false)
    private Integer age = 25;

    @Column(name = "profession")
    private String profession;

    @Column(name = "residential_area")
    private String residentialArea;

    @Column(name = "email")
    private String email;

    @Column(name = "emergency_contact")
    private String emergencyContact;

    // --- Section 2: Fitness Goal & Physical Profile ---
    @Column(name = "fitness_goal")
    private String fitnessGoal;

    @Column(name = "physical_profile_notes", columnDefinition = "TEXT")
    private String physicalProfileNotes;

    // --- Section 3: Membership Plan & Commercials / Fees ---
    @Column(name = "plan_id")
    private UUID planId;

    @Column(name = "plan_name")
    private String planName;

    @Column(name = "standard_price", precision = 10, scale = 2)
    private BigDecimal standardPrice = BigDecimal.ZERO;

    @Column(name = "quoted_fee", precision = 10, scale = 2, nullable = false)
    private BigDecimal quotedFee = BigDecimal.ZERO;

    @Column(name = "advance_token", precision = 10, scale = 2)
    private BigDecimal advanceToken = BigDecimal.ZERO;

    @Column(name = "pending_plan_fee", precision = 10, scale = 2)
    private BigDecimal pendingPlanFee = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private LeadPaymentStatus paymentStatus = LeadPaymentStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_mode_preference")
    private PaymentModePreference paymentModePreference = PaymentModePreference.UPI;

    // --- Section 4: Follow-up Schedule & Conversion Pipeline ---
    @Column(name = "follow_up_date", nullable = false)
    private LocalDate followUpDate;

    @Column(name = "follow_up_time")
    private String followUpTime = "05:00 PM";

    @Column(name = "expected_joining_date")
    private LocalDate expectedJoiningDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "inquiry_source", nullable = false)
    private LeadSource inquirySource = LeadSource.WALK_IN;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private LeadPriority priority = LeadPriority.HIGH;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private LeadStatus status = LeadStatus.NEW;

    // --- Section 5: Initial Discussion Notes & Reminders ---
    @Column(name = "discussion_notes", columnDefinition = "TEXT")
    private String discussionNotes;

    @Column(name = "whatsapp_reminder")
    private Boolean whatsappReminder = true;

    @Column(name = "phone_call_task")
    private Boolean phoneCallTask = true;

    @Column(name = "payment_dues_reminder")
    private Boolean paymentDuesReminder = false;

    // --- System & Audit Information ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to_user_id")
    private User assignedTo;

    @Column(name = "converted_member_id")
    private UUID convertedMemberId;

    @Column(name = "converted_at")
    private LocalDateTime convertedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Lead() {
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.updatedAt == null) {
            this.updatedAt = LocalDateTime.now();
        }
        if (this.followUpDate == null) {
            this.followUpDate = LocalDate.now();
        }
        if (this.expectedJoiningDate == null) {
            this.expectedJoiningDate = LocalDate.now().plusDays(3);
        }
        if (this.status == null) {
            this.status = LeadStatus.NEW;
        }
        if (this.priority == null) {
            this.priority = LeadPriority.HIGH;
        }
        if (this.inquirySource == null) {
            this.inquirySource = LeadSource.WALK_IN;
        }
        if (this.gender == null) {
            this.gender = Gender.MALE;
        }
        if (this.paymentModePreference == null) {
            this.paymentModePreference = PaymentModePreference.UPI;
        }
        if (this.whatsappReminder == null) {
            this.whatsappReminder = true;
        }
        if (this.phoneCallTask == null) {
            this.phoneCallTask = true;
        }
        if (this.paymentDuesReminder == null) {
            this.paymentDuesReminder = false;
        }
        splitNamesIfEmpty();
        recalculateCommercials();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        splitNamesIfEmpty();
        recalculateCommercials();
    }

    public void recalculateCommercials() {
        if (this.quotedFee == null) {
            this.quotedFee = BigDecimal.ZERO;
        }
        if (this.advanceToken == null) {
            this.advanceToken = BigDecimal.ZERO;
        }
        if (this.standardPrice == null) {
            this.standardPrice = this.quotedFee;
        }

        BigDecimal pending = this.quotedFee.subtract(this.advanceToken);
        this.pendingPlanFee = pending.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : pending;

        if (this.quotedFee.compareTo(BigDecimal.ZERO) <= 0 || this.pendingPlanFee.compareTo(BigDecimal.ZERO) == 0) {
            this.paymentStatus = LeadPaymentStatus.PAID;
        } else if (this.advanceToken.compareTo(BigDecimal.ZERO) > 0) {
            this.paymentStatus = LeadPaymentStatus.PARTIAL;
        } else {
            this.paymentStatus = LeadPaymentStatus.PENDING;
        }
    }

    private void splitNamesIfEmpty() {
        if (this.fullName != null && !this.fullName.trim().isEmpty()) {
            String trimmed = this.fullName.trim();
            int spaceIdx = trimmed.indexOf(" ");
            if (this.firstName == null || this.firstName.trim().isEmpty()) {
                this.firstName = spaceIdx > 0 ? trimmed.substring(0, spaceIdx) : trimmed;
            }
            if (this.lastName == null || this.lastName.trim().isEmpty()) {
                this.lastName = spaceIdx > 0 ? trimmed.substring(spaceIdx + 1).trim() : "";
            }
        }
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

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
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
