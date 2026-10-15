package com.example.prabhim.dto.lead;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;

public class LeadCreateRequest {

    private String id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Phone number is required")
    private String phone;

    private String email;

    private String gender = "Male";

    private Integer age;

    private String dob;

    private String profession;

    private String address;

    private String emergencyContact;

    private String height;

    private String weight;

    private String dietPreference;

    private String workoutTimePreference;

    private String paymentModePreference;

    private String status = "NEW_INQUIRY";

    private String source = "Walk-in";

    private String subSource;

    private String fitnessGoal = "General Fitness";

    private String targetPlan = "Monthly Basic Pass";

    private String budget;

    private BigDecimal quotedAmount;

    private BigDecimal depositPaid;

    private String followUpDate;

    private String followUpTime;

    private String nextAction;

    private String nextActionTime;

    private String expectedJoiningDate;

    private String priority = "HIGH";

    private String assignedStaff;

    @JsonProperty("interestedPT")
    private Boolean interestedPt;

    private Boolean interestedWeightLoss;

    private Boolean interestedGroupClass;

    private String notes;

    private String initialDiscussionNotes;

    private VisitorInfoDto visitorInfo;

    private RemindersDto reminders;

    private DiscussionDto initialDiscussion;

    private List<DiscussionDto> discussions;

    private String tenantId;
    private String creatorEmail;
    private String createdBy;
    private String gymId;
    private String adminEmail;

    public LeadCreateRequest() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
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
    }

    public BigDecimal getDepositPaid() {
        return depositPaid;
    }

    public void setDepositPaid(BigDecimal depositPaid) {
        this.depositPaid = depositPaid;
    }

    public String getFollowUpDate() {
        return followUpDate;
    }

    public void setFollowUpDate(String followUpDate) {
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

    public String getExpectedJoiningDate() {
        return expectedJoiningDate;
    }

    public void setExpectedJoiningDate(String expectedJoiningDate) {
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

    public VisitorInfoDto getVisitorInfo() {
        return visitorInfo;
    }

    public void setVisitorInfo(VisitorInfoDto visitorInfo) {
        this.visitorInfo = visitorInfo;
    }

    public RemindersDto getReminders() {
        return reminders;
    }

    public void setReminders(RemindersDto reminders) {
        this.reminders = reminders;
    }

    public DiscussionDto getInitialDiscussion() {
        return initialDiscussion;
    }

    public void setInitialDiscussion(DiscussionDto initialDiscussion) {
        this.initialDiscussion = initialDiscussion;
    }

    public List<DiscussionDto> getDiscussions() {
        return discussions;
    }

    public void setDiscussions(List<DiscussionDto> discussions) {
        this.discussions = discussions;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getGymId() {
        return gymId;
    }

    public void setGymId(String gymId) {
        this.gymId = gymId;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }
}
