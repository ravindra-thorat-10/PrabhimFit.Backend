package com.example.prabhim.dto.lead;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.example.prabhim.entity.Lead;
import com.example.prabhim.entity.LeadDiscussion;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LeadResponse {

    private String id;

    @JsonProperty("_id")
    private String mongoCompatId;

    private String name;
    private String phone;
    private String email;
    private String gender;
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
    private String status;
    private String source;
    private String subSource;
    private String fitnessGoal;
    private String targetPlan;
    private String budget;
    private BigDecimal quotedAmount;
    private BigDecimal depositPaid;
    private BigDecimal balanceDue;
    private String followUpDate;
    private String followUpTime;
    private String nextAction;
    private String nextActionTime;
    private String expectedJoiningDate;
    private String priority;
    private String assignedStaff;

    @JsonProperty("interestedPT")
    private Boolean interestedPt;

    private Boolean interestedWeightLoss;
    private Boolean interestedGroupClass;
    private String notes;
    private String initialDiscussionNotes;

    private VisitorInfoDto visitorInfo;
    private RemindersDto reminders;
    private List<DiscussionDto> discussions = new ArrayList<>();

    // Multi-tenant isolation stamps
    private String tenantId;
    private String creatorEmail;
    private String createdBy;
    private String gymId;
    private String adminEmail;
    private UUID userId;

    private String createdAt;
    private String updatedAt;

    public LeadResponse() {
    }

    public static LeadResponse fromEntity(Lead lead) {
        if (lead == null) return null;

        LeadResponse dto = new LeadResponse();
        String idStr = lead.getId() != null ? lead.getId().toString() : "";
        dto.setId(idStr);
        dto.setMongoCompatId(idStr);
        dto.setName(lead.getName());
        dto.setPhone(lead.getPhone());
        dto.setEmail(lead.getEmail());
        dto.setGender(lead.getGender() != null ? lead.getGender() : "Male");
        dto.setAge(lead.getAge() != null ? lead.getAge() : 25);
        dto.setDob(lead.getDob() != null ? lead.getDob().toString() : null);
        dto.setProfession(lead.getProfession());
        dto.setAddress(lead.getAddress());
        dto.setEmergencyContact(lead.getEmergencyContact());
        dto.setHeight(lead.getHeight());
        dto.setWeight(lead.getWeight());
        dto.setDietPreference(lead.getDietPreference());
        dto.setWorkoutTimePreference(lead.getWorkoutTimePreference());
        dto.setPaymentModePreference(lead.getPaymentModePreference());
        dto.setStatus(lead.getStatus());
        dto.setSource(lead.getSource());
        dto.setSubSource(lead.getSubSource());
        dto.setFitnessGoal(lead.getFitnessGoal());
        dto.setTargetPlan(lead.getTargetPlan());
        dto.setBudget(lead.getBudget());
        dto.setQuotedAmount(lead.getQuotedAmount());
        dto.setDepositPaid(lead.getDepositPaid());
        dto.setBalanceDue(lead.getBalanceDue());
        dto.setFollowUpDate(lead.getFollowUpDate() != null ? lead.getFollowUpDate().toString() : null);
        dto.setFollowUpTime(lead.getFollowUpTime());
        dto.setNextAction(lead.getNextAction());
        dto.setNextActionTime(lead.getNextActionTime());
        dto.setExpectedJoiningDate(lead.getExpectedJoiningDate() != null ? lead.getExpectedJoiningDate().toString() : null);
        dto.setPriority(lead.getPriority());
        dto.setAssignedStaff(lead.getAssignedStaff());
        dto.setInterestedPt(lead.getInterestedPt());
        dto.setInterestedWeightLoss(lead.getInterestedWeightLoss());
        dto.setInterestedGroupClass(lead.getInterestedGroupClass());
        dto.setNotes(lead.getNotes());
        dto.setInitialDiscussionNotes(lead.getInitialDiscussionNotes());

        // Visitor info
        VisitorInfoDto vi = new VisitorInfoDto();
        vi.setVisitDate(lead.getVisitDate() != null ? lead.getVisitDate().toString() : null);
        vi.setTrialSlot(lead.getTrialSlot());
        vi.setTrialCompleted(lead.getTrialCompleted());
        vi.setInterestedInPT(lead.getInterestedInPt());
        vi.setBodyFatPercentage(lead.getBodyFatPercentage());
        vi.setHealthConditions(lead.getHealthConditions());
        vi.setNotes(lead.getVisitorNotes());
        dto.setVisitorInfo(vi);

        // Reminders
        RemindersDto rem = new RemindersDto();
        rem.setCallReminder(lead.getCallReminder());
        rem.setCallTime(lead.getCallTime());
        rem.setMessageReminder(lead.getMessageReminder());
        rem.setPaymentReminder(lead.getPaymentReminder());
        rem.setPaymentDueDate(lead.getPaymentDueDate());
        dto.setReminders(rem);

        // Discussions
        List<DiscussionDto> discDtos = new ArrayList<>();
        if (lead.getDiscussions() != null) {
            for (LeadDiscussion d : lead.getDiscussions()) {
                discDtos.add(new DiscussionDto(
                        d.getId() != null ? d.getId().toString() : "",
                        d.getDate() != null ? d.getDate().toString() : "",
                        d.getChannel(),
                        d.getAuthor(),
                        d.getNotes(),
                        d.getNextAction(),
                        d.getExpectedJoinDate()
                ));
            }
        }
        dto.setDiscussions(discDtos);

        // Tenancy
        String email = lead.getCreatorEmail() != null ? lead.getCreatorEmail() : (lead.getUser() != null ? lead.getUser().getEmail() : "");
        dto.setTenantId(email);
        dto.setCreatorEmail(email);
        dto.setCreatedBy(email);
        dto.setGymId(email);
        dto.setAdminEmail(email);
        dto.setUserId(lead.getUser() != null ? lead.getUser().getId() : null);

        // Timestamps
        if (lead.getCreatedAt() != null) {
            dto.setCreatedAt(lead.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
        if (lead.getUpdatedAt() != null) {
            dto.setUpdatedAt(lead.getUpdatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }

        return dto;
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMongoCompatId() {
        return mongoCompatId;
    }

    public void setMongoCompatId(String mongoCompatId) {
        this.mongoCompatId = mongoCompatId;
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

    public BigDecimal getBalanceDue() {
        return balanceDue;
    }

    public void setBalanceDue(BigDecimal balanceDue) {
        this.balanceDue = balanceDue;
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

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
