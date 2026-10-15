package com.example.prabhim.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "leads")
public class Lead {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @org.hibernate.annotations.OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
    private User user;

    @Column(name = "creator_email")
    private String creatorEmail;

    @Column(name = "tenant_id")
    private String tenantId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    private String email;

    private String gender = "Male";

    private Integer age = 25;

    @Column(name = "date_of_birth")
    private LocalDate dob;

    private String profession;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(name = "emergency_contact")
    private String emergencyContact;

    private String height;

    private String weight;

    @Column(name = "diet_preference")
    private String dietPreference = "Non-Vegetarian";

    @Column(name = "workout_time_preference")
    private String workoutTimePreference = "Evening (6:00 PM - 8:00 PM)";

    @Column(name = "payment_mode_preference")
    private String paymentModePreference = "UPI";

    @Column(nullable = false)
    private String status = "NEW_INQUIRY";

    @Column(nullable = false)
    private String source = "Walk-in";

    @Column(name = "sub_source")
    private String subSource;

    @Column(name = "fitness_goal")
    private String fitnessGoal = "General Fitness";

    @Column(name = "target_plan")
    private String targetPlan = "Monthly Basic Pass";

    private String budget;

    @Column(name = "quoted_amount", precision = 12, scale = 2)
    private BigDecimal quotedAmount = BigDecimal.ZERO;

    @Column(name = "deposit_paid", precision = 12, scale = 2)
    private BigDecimal depositPaid = BigDecimal.ZERO;

    @Column(name = "balance_due", precision = 12, scale = 2)
    private BigDecimal balanceDue = BigDecimal.ZERO;

    @Column(name = "follow_up_date")
    private LocalDate followUpDate;

    @Column(name = "follow_up_time")
    private String followUpTime = "11:00 AM";

    @Column(name = "next_action")
    private String nextAction;

    @Column(name = "next_action_time")
    private String nextActionTime;

    @Column(name = "expected_joining_date")
    private LocalDate expectedJoiningDate;

    @Column(nullable = false)
    private String priority = "HIGH";

    @Column(name = "assigned_staff")
    private String assignedStaff = "Trainer/Staff";

    @Column(name = "interested_pt")
    private Boolean interestedPt = false;

    @Column(name = "interested_weight_loss")
    private Boolean interestedWeightLoss = false;

    @Column(name = "interested_group_class")
    private Boolean interestedGroupClass = false;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "initial_discussion_notes", columnDefinition = "TEXT")
    private String initialDiscussionNotes;

    // Visitor Info columns
    @Column(name = "visit_date")
    private LocalDate visitDate;

    @Column(name = "trial_slot")
    private String trialSlot = "06:00 PM - 07:00 PM";

    @Column(name = "trial_completed")
    private Boolean trialCompleted = false;

    @Column(name = "visitor_interested_pt")
    private Boolean interestedInPt = false;

    @Column(name = "body_fat_percentage")
    private String bodyFatPercentage;

    @Column(name = "health_conditions")
    private String healthConditions = "None";

    @Column(name = "visitor_notes", columnDefinition = "TEXT")
    private String visitorNotes;

    // Reminders columns
    @Column(name = "call_reminder")
    private Boolean callReminder = true;

    @Column(name = "call_time")
    private String callTime = "11:00 AM";

    @Column(name = "message_reminder")
    private Boolean messageReminder = true;

    @Column(name = "payment_reminder")
    private Boolean paymentReminder = false;

    @Column(name = "payment_due_date")
    private String paymentDueDate;

    @OneToMany(mappedBy = "lead", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("date DESC")
    private List<LeadDiscussion> discussions = new ArrayList<>();

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
        if (this.status == null || this.status.trim().isEmpty()) {
            this.status = "NEW_INQUIRY";
        }
        if (this.priority == null || this.priority.trim().isEmpty()) {
            this.priority = "HIGH";
        }
        if (this.source == null || this.source.trim().isEmpty()) {
            this.source = "Walk-in";
        }
        if (this.followUpDate == null) {
            this.followUpDate = LocalDate.now();
        }
        if (this.visitDate == null) {
            this.visitDate = LocalDate.now();
        }
        recalculateFinancials();
        computeDefaultNextAction();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        recalculateFinancials();
        computeDefaultNextAction();
    }

    public void recalculateFinancials() {
        if (this.quotedAmount == null) {
            this.quotedAmount = BigDecimal.ZERO;
        }
        if (this.depositPaid == null) {
            this.depositPaid = BigDecimal.ZERO;
        }
        BigDecimal diff = this.quotedAmount.subtract(this.depositPaid);
        this.balanceDue = diff.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : diff;
    }

    public void computeDefaultNextAction() {
        if (this.nextAction == null || this.nextAction.trim().isEmpty()) {
            if ("CONVERTED".equalsIgnoreCase(this.status)) {
                this.nextAction = "Enrolled Member";
            } else if ("PAYMENT_PENDING".equalsIgnoreCase(this.status)) {
                this.nextAction = "Payment Due";
            } else if (Boolean.TRUE.equals(this.trialCompleted) || "TRIAL_VISITED".equalsIgnoreCase(this.status)) {
                this.nextAction = "Trial Completed";
            } else if ("CONTACTED".equalsIgnoreCase(this.status)) {
                this.nextAction = "Call Scheduled";
            } else {
                this.nextAction = "Follow-up Scheduled";
            }
        }
        if (this.nextActionTime == null || this.nextActionTime.trim().isEmpty()) {
            this.nextActionTime = this.followUpTime != null ? this.followUpTime : "5:30 PM";
        }
    }

    public void addDiscussion(LeadDiscussion discussion) {
        if (discussion != null) {
            discussion.setLead(this);
            this.discussions.add(0, discussion);
        }
    }

    // Getters and Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            this.creatorEmail = user.getEmail();
            this.tenantId = user.getEmail();
        }
    }

    public String getCreatorEmail() {
        return creatorEmail;
    }

    public void setCreatorEmail(String creatorEmail) {
        this.creatorEmail = creatorEmail;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDietPreference() {
        return dietPreference;
    }

    public void setDietPreference(String dietPreference) {
        this.dietPreference = dietPreference;
    }

    public String getWorkoutTimePreference() {
        return workoutTimePreference;
    }

    public void setWorkoutTimePreference(String workoutTimePreference) {
        this.workoutTimePreference = workoutTimePreference;
    }

    public String getPaymentModePreference() {
        return paymentModePreference;
    }

    public void setPaymentModePreference(String paymentModePreference) {
        this.paymentModePreference = paymentModePreference;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSubSource() {
        return subSource;
    }

    public void setSubSource(String subSource) {
        this.subSource = subSource;
    }

    public String getFitnessGoal() {
        return fitnessGoal;
    }

    public void setFitnessGoal(String fitnessGoal) {
        this.fitnessGoal = fitnessGoal;
    }

    public String getTargetPlan() {
        return targetPlan;
    }

    public void setTargetPlan(String targetPlan) {
        this.targetPlan = targetPlan;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public BigDecimal getQuotedAmount() {
        return quotedAmount;
    }

    public void setQuotedAmount(BigDecimal quotedAmount) {
        this.quotedAmount = quotedAmount;
        recalculateFinancials();
    }

    public BigDecimal getDepositPaid() {
        return depositPaid;
    }

    public void setDepositPaid(BigDecimal depositPaid) {
        this.depositPaid = depositPaid;
        recalculateFinancials();
    }

    public BigDecimal getBalanceDue() {
        return balanceDue;
    }

    public void setBalanceDue(BigDecimal balanceDue) {
        this.balanceDue = balanceDue;
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

    public String getNextAction() {
        return nextAction;
    }

    public void setNextAction(String nextAction) {
        this.nextAction = nextAction;
    }

    public String getNextActionTime() {
        return nextActionTime;
    }

    public void setNextActionTime(String nextActionTime) {
        this.nextActionTime = nextActionTime;
    }

    public LocalDate getExpectedJoiningDate() {
        return expectedJoiningDate;
    }

    public void setExpectedJoiningDate(LocalDate expectedJoiningDate) {
        this.expectedJoiningDate = expectedJoiningDate;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getAssignedStaff() {
        return assignedStaff;
    }

    public void setAssignedStaff(String assignedStaff) {
        this.assignedStaff = assignedStaff;
    }

    public Boolean getInterestedPt() {
        return interestedPt;
    }

    public void setInterestedPt(Boolean interestedPt) {
        this.interestedPt = interestedPt;
    }

    public Boolean getInterestedWeightLoss() {
        return interestedWeightLoss;
    }

    public void setInterestedWeightLoss(Boolean interestedWeightLoss) {
        this.interestedWeightLoss = interestedWeightLoss;
    }

    public Boolean getInterestedGroupClass() {
        return interestedGroupClass;
    }

    public void setInterestedGroupClass(Boolean interestedGroupClass) {
        this.interestedGroupClass = interestedGroupClass;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getInitialDiscussionNotes() {
        return initialDiscussionNotes;
    }

    public void setInitialDiscussionNotes(String initialDiscussionNotes) {
        this.initialDiscussionNotes = initialDiscussionNotes;
    }

    public LocalDate getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(LocalDate visitDate) {
        this.visitDate = visitDate;
    }

    public String getTrialSlot() {
        return trialSlot;
    }

    public void setTrialSlot(String trialSlot) {
        this.trialSlot = trialSlot;
    }

    public Boolean getTrialCompleted() {
        return trialCompleted;
    }

    public void setTrialCompleted(Boolean trialCompleted) {
        this.trialCompleted = trialCompleted;
    }

    public Boolean getInterestedInPt() {
        return interestedInPt;
    }

    public void setInterestedInPt(Boolean interestedInPt) {
        this.interestedInPt = interestedInPt;
    }

    public String getBodyFatPercentage() {
        return bodyFatPercentage;
    }

    public void setBodyFatPercentage(String bodyFatPercentage) {
        this.bodyFatPercentage = bodyFatPercentage;
    }

    public String getHealthConditions() {
        return healthConditions;
    }

    public void setHealthConditions(String healthConditions) {
        this.healthConditions = healthConditions;
    }

    public String getVisitorNotes() {
        return visitorNotes;
    }

    public void setVisitorNotes(String visitorNotes) {
        this.visitorNotes = visitorNotes;
    }

    public Boolean getCallReminder() {
        return callReminder;
    }

    public void setCallReminder(Boolean callReminder) {
        this.callReminder = callReminder;
    }

    public String getCallTime() {
        return callTime;
    }

    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }

    public Boolean getMessageReminder() {
        return messageReminder;
    }

    public void setMessageReminder(Boolean messageReminder) {
        this.messageReminder = messageReminder;
    }

    public Boolean getPaymentReminder() {
        return paymentReminder;
    }

    public void setPaymentReminder(Boolean paymentReminder) {
        this.paymentReminder = paymentReminder;
    }

    public String getPaymentDueDate() {
        return paymentDueDate;
    }

    public void setPaymentDueDate(String paymentDueDate) {
        this.paymentDueDate = paymentDueDate;
    }

    public List<LeadDiscussion> getDiscussions() {
        return discussions;
    }

    public void setDiscussions(List<LeadDiscussion> discussions) {
        this.discussions = discussions;
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
