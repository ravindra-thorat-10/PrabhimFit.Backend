package com.example.prabhim.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.prabhim.dto.lead.DiscussionDto;
import com.example.prabhim.dto.lead.LeadCreateRequest;
import com.example.prabhim.dto.lead.LeadDiscussionRequest;
import com.example.prabhim.dto.lead.LeadOptionsResponse;
import com.example.prabhim.dto.lead.LeadResponse;
import com.example.prabhim.dto.lead.LeadStatsResponse;
import com.example.prabhim.dto.lead.LeadUpdateRequest;
import com.example.prabhim.dto.lead.RemindersDto;
import com.example.prabhim.dto.lead.VisitorInfoDto;
import com.example.prabhim.dto.member.PageResponse;
import com.example.prabhim.entity.Lead;
import com.example.prabhim.entity.LeadDiscussion;
import com.example.prabhim.entity.User;
import com.example.prabhim.exception.LeadNotFoundException;
import com.example.prabhim.exception.UserNotFoundException;
import com.example.prabhim.repository.LeadDiscussionRepository;
import com.example.prabhim.repository.LeadRepository;
import com.example.prabhim.repository.UserRepository;
import com.example.prabhim.service.LeadService;
import com.example.prabhim.specification.LeadSpecification;

@Service
@Transactional
public class LeadServiceImpl implements LeadService {

    private final LeadRepository leadRepository;
    private final LeadDiscussionRepository leadDiscussionRepository;
    private final UserRepository userRepository;

    public LeadServiceImpl(LeadRepository leadRepository,
                           LeadDiscussionRepository leadDiscussionRepository,
                           UserRepository userRepository) {
        this.leadRepository = leadRepository;
        this.leadDiscussionRepository = leadDiscussionRepository;
        this.userRepository = userRepository;
    }

    private User validateAndGetActorUser(UUID actorUserId) {
        if (actorUserId == null) {
            throw new UserNotFoundException("Authenticated user ID cannot be null.");
        }
        return userRepository.findById(actorUserId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + actorUserId));
    }

    private LocalDate parseLocalDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        String trimmed = dateStr.trim();
        if (trimmed.length() >= 10) {
            try {
                return LocalDate.parse(trimmed.substring(0, 10));
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    @Override
    public LeadResponse createLead(LeadCreateRequest request, UUID actorUserId) {
        User user = validateAndGetActorUser(actorUserId);

        Lead lead = new Lead();
        lead.setUser(user);

        // Check if an existing valid UUID was supplied, otherwise JPA generates a new UUID
        if (request.getId() != null && !request.getId().trim().isEmpty()) {
            try {
                lead.setId(UUID.fromString(request.getId().trim()));
            } catch (IllegalArgumentException ignored) {
                // Client passed non-UUID ID (e.g. "lead_12345"); let JPA generate standard UUID
            }
        }

        lead.setName(request.getName() != null ? request.getName().trim() : "New Visitor");
        lead.setPhone(request.getPhone() != null ? request.getPhone().trim() : "");
        lead.setEmail(request.getEmail() != null && !request.getEmail().trim().isEmpty() ? request.getEmail().trim().toLowerCase() : null);
        lead.setGender(request.getGender() != null ? request.getGender() : "Male");
        lead.setAge(request.getAge() != null ? request.getAge() : 25);
        lead.setDob(parseLocalDate(request.getDob()));
        lead.setProfession(request.getProfession());
        lead.setAddress(request.getAddress());
        lead.setEmergencyContact(request.getEmergencyContact());
        lead.setHeight(request.getHeight());
        lead.setWeight(request.getWeight());
        lead.setDietPreference(request.getDietPreference() != null ? request.getDietPreference() : "Non-Vegetarian");
        lead.setWorkoutTimePreference(request.getWorkoutTimePreference() != null ? request.getWorkoutTimePreference() : "Evening (6:00 PM - 8:00 PM)");
        lead.setPaymentModePreference(request.getPaymentModePreference() != null ? request.getPaymentModePreference() : "UPI");
        lead.setStatus(request.getStatus() != null && !request.getStatus().trim().isEmpty() ? request.getStatus().trim() : "NEW_INQUIRY");
        lead.setSource(request.getSource() != null && !request.getSource().trim().isEmpty() ? request.getSource().trim() : "Walk-in");
        lead.setSubSource(request.getSubSource() != null ? request.getSubSource() : lead.getSource());
        lead.setFitnessGoal(request.getFitnessGoal() != null ? request.getFitnessGoal() : "General Fitness");
        lead.setTargetPlan(request.getTargetPlan() != null ? request.getTargetPlan() : "Monthly Basic Pass");
        lead.setBudget(request.getBudget());

        BigDecimal quoted = request.getQuotedAmount() != null ? request.getQuotedAmount() : BigDecimal.ZERO;
        BigDecimal deposit = request.getDepositPaid() != null ? request.getDepositPaid() : BigDecimal.ZERO;
        lead.setQuotedAmount(quoted);
        lead.setDepositPaid(deposit);

        lead.setFollowUpDate(parseLocalDate(request.getFollowUpDate()) != null ? parseLocalDate(request.getFollowUpDate()) : LocalDate.now());
        lead.setFollowUpTime(request.getFollowUpTime() != null ? request.getFollowUpTime() : "11:00 AM");
        lead.setNextAction(request.getNextAction());
        lead.setNextActionTime(request.getNextActionTime() != null ? request.getNextActionTime() : lead.getFollowUpTime());
        lead.setExpectedJoiningDate(parseLocalDate(request.getExpectedJoiningDate()) != null ? parseLocalDate(request.getExpectedJoiningDate()) : LocalDate.now().plusDays(3));
        lead.setPriority(request.getPriority() != null ? request.getPriority() : "HIGH");
        lead.setAssignedStaff(request.getAssignedStaff() != null ? request.getAssignedStaff() : "Trainer/Staff");

        lead.setInterestedPt(Boolean.TRUE.equals(request.getInterestedPt()));
        lead.setInterestedWeightLoss(Boolean.TRUE.equals(request.getInterestedWeightLoss()));
        lead.setInterestedGroupClass(Boolean.TRUE.equals(request.getInterestedGroupClass()));

        lead.setNotes(request.getNotes());
        lead.setInitialDiscussionNotes(request.getInitialDiscussionNotes());

        // Visitor Info
        if (request.getVisitorInfo() != null) {
            VisitorInfoDto vi = request.getVisitorInfo();
            lead.setVisitDate(parseLocalDate(vi.getVisitDate()) != null ? parseLocalDate(vi.getVisitDate()) : LocalDate.now());
            lead.setTrialSlot(vi.getTrialSlot() != null ? vi.getTrialSlot() : "06:00 PM - 07:00 PM");
            lead.setTrialCompleted(Boolean.TRUE.equals(vi.getTrialCompleted()));
            lead.setInterestedInPt(Boolean.TRUE.equals(vi.getInterestedInPT()));
            lead.setBodyFatPercentage(vi.getBodyFatPercentage());
            lead.setHealthConditions(vi.getHealthConditions() != null ? vi.getHealthConditions() : "None");
            lead.setVisitorNotes(vi.getNotes());
        }

        // Reminders
        if (request.getReminders() != null) {
            RemindersDto rem = request.getReminders();
            lead.setCallReminder(rem.getCallReminder() == null || rem.getCallReminder());
            lead.setCallTime(rem.getCallTime() != null ? rem.getCallTime() : lead.getFollowUpTime());
            lead.setMessageReminder(rem.getMessageReminder() == null || rem.getMessageReminder());
            lead.setPaymentReminder(Boolean.TRUE.equals(rem.getPaymentReminder()));
            lead.setPaymentDueDate(rem.getPaymentDueDate());
        }

        // Initial discussion or discussions array
        List<LeadDiscussion> discussionsToPersist = new ArrayList<>();
        if (request.getInitialDiscussion() != null) {
            DiscussionDto d = request.getInitialDiscussion();
            LeadDiscussion disc = new LeadDiscussion(
                    lead,
                    LocalDateTime.now(),
                    d.getChannel() != null ? d.getChannel() : "Walk-in Visit",
                    d.getAuthor() != null ? d.getAuthor() : "Admin Desk",
                    d.getNotes() != null ? d.getNotes() : "First inquiry at reception desk.",
                    d.getNextAction() != null ? d.getNextAction() : "Scheduled follow-up call.",
                    d.getExpectedJoinDate()
            );
            discussionsToPersist.add(disc);
        } else if (request.getDiscussions() != null && !request.getDiscussions().isEmpty()) {
            for (DiscussionDto d : request.getDiscussions()) {
                LeadDiscussion disc = new LeadDiscussion(
                        lead,
                        LocalDateTime.now(),
                        d.getChannel() != null ? d.getChannel() : "Walk-in Visit",
                        d.getAuthor() != null ? d.getAuthor() : "Admin Desk",
                        d.getNotes() != null ? d.getNotes() : "Inquiry logged.",
                        d.getNextAction(),
                        d.getExpectedJoinDate()
                );
                discussionsToPersist.add(disc);
            }
        }
        lead.setDiscussions(discussionsToPersist);

        Lead saved = leadRepository.save(lead);
        return LeadResponse.fromEntity(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeadResponse> getAllLeads(
            UUID actorUserId,
            String search,
            String status,
            String source,
            String priority,
            String assignedStaff,
            String gender,
            LocalDate startDate,
            LocalDate endDate,
            String sortBy,
            String sortDir) {

        validateAndGetActorUser(actorUserId);

        Sort.Direction direction = "asc".equalsIgnoreCase(sortDir) ? Sort.Direction.ASC : Sort.Direction.DESC;
        String sortProperty = (sortBy != null && !sortBy.trim().isEmpty()) ? sortBy : "createdAt";
        Sort sort = Sort.by(direction, sortProperty);

        Specification<Lead> spec = LeadSpecification.filter(
                actorUserId, search, status, source, priority, assignedStaff, gender, startDate, endDate
        );

        List<Lead> leads = leadRepository.findAll(spec, sort);
        return leads.stream().map(LeadResponse::fromEntity).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<LeadResponse> getLeadsPaged(
            UUID actorUserId,
            String search,
            String status,
            String source,
            String priority,
            String assignedStaff,
            String gender,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable) {

        validateAndGetActorUser(actorUserId);

        Specification<Lead> spec = LeadSpecification.filter(
                actorUserId, search, status, source, priority, assignedStaff, gender, startDate, endDate
        );

        Page<Lead> page = leadRepository.findAll(spec, pageable);
        List<LeadResponse> dtos = page.getContent().stream().map(LeadResponse::fromEntity).collect(Collectors.toList());
        return PageResponse.fromPage(page, dtos);
    }

    @Override
    @Transactional(readOnly = true)
    public LeadResponse getLeadById(UUID id, UUID actorUserId) {
        validateAndGetActorUser(actorUserId);

        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new LeadNotFoundException("Lead not found with ID: " + id));

        if (!lead.getUser().getId().equals(actorUserId)) {
            throw new AccessDeniedException("Access denied: You do not have permission to access this lead.");
        }

        return LeadResponse.fromEntity(lead);
    }

    @Override
    public LeadResponse updateLead(UUID id, LeadUpdateRequest request, UUID actorUserId) {
        validateAndGetActorUser(actorUserId);

        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new LeadNotFoundException("Lead not found with ID: " + id));

        if (!lead.getUser().getId().equals(actorUserId)) {
            throw new AccessDeniedException("Access denied: You do not have permission to update this lead.");
        }

        if (request.getName() != null) lead.setName(request.getName().trim());
        if (request.getPhone() != null) lead.setPhone(request.getPhone().trim());
        if (request.getEmail() != null) lead.setEmail(request.getEmail().trim().toLowerCase());
        if (request.getGender() != null) lead.setGender(request.getGender());
        if (request.getAge() != null) lead.setAge(request.getAge());
        if (request.getDob() != null) lead.setDob(parseLocalDate(request.getDob()));
        if (request.getProfession() != null) lead.setProfession(request.getProfession());
        if (request.getAddress() != null) lead.setAddress(request.getAddress());
        if (request.getEmergencyContact() != null) lead.setEmergencyContact(request.getEmergencyContact());
        if (request.getHeight() != null) lead.setHeight(request.getHeight());
        if (request.getWeight() != null) lead.setWeight(request.getWeight());
        if (request.getDietPreference() != null) lead.setDietPreference(request.getDietPreference());
        if (request.getWorkoutTimePreference() != null) lead.setWorkoutTimePreference(request.getWorkoutTimePreference());
        if (request.getPaymentModePreference() != null) lead.setPaymentModePreference(request.getPaymentModePreference());
        if (request.getStatus() != null && !request.getStatus().trim().isEmpty()) lead.setStatus(request.getStatus().trim());
        if (request.getSource() != null) lead.setSource(request.getSource());
        if (request.getSubSource() != null) lead.setSubSource(request.getSubSource());
        if (request.getFitnessGoal() != null) lead.setFitnessGoal(request.getFitnessGoal());
        if (request.getTargetPlan() != null) lead.setTargetPlan(request.getTargetPlan());
        if (request.getBudget() != null) lead.setBudget(request.getBudget());

        if (request.getQuotedAmount() != null) lead.setQuotedAmount(request.getQuotedAmount());
        if (request.getDepositPaid() != null) lead.setDepositPaid(request.getDepositPaid());

        if (request.getFollowUpDate() != null) lead.setFollowUpDate(parseLocalDate(request.getFollowUpDate()));
        if (request.getFollowUpTime() != null) lead.setFollowUpTime(request.getFollowUpTime());
        if (request.getNextAction() != null) lead.setNextAction(request.getNextAction());
        if (request.getNextActionTime() != null) lead.setNextActionTime(request.getNextActionTime());
        if (request.getExpectedJoiningDate() != null) lead.setExpectedJoiningDate(parseLocalDate(request.getExpectedJoiningDate()));
        if (request.getPriority() != null) lead.setPriority(request.getPriority());
        if (request.getAssignedStaff() != null) lead.setAssignedStaff(request.getAssignedStaff());

        if (request.getInterestedPt() != null) lead.setInterestedPt(request.getInterestedPt());
        if (request.getInterestedWeightLoss() != null) lead.setInterestedWeightLoss(request.getInterestedWeightLoss());
        if (request.getInterestedGroupClass() != null) lead.setInterestedGroupClass(request.getInterestedGroupClass());

        if (request.getNotes() != null) lead.setNotes(request.getNotes());
        if (request.getInitialDiscussionNotes() != null) lead.setInitialDiscussionNotes(request.getInitialDiscussionNotes());

        if (request.getVisitorInfo() != null) {
            VisitorInfoDto vi = request.getVisitorInfo();
            if (vi.getVisitDate() != null) lead.setVisitDate(parseLocalDate(vi.getVisitDate()));
            if (vi.getTrialSlot() != null) lead.setTrialSlot(vi.getTrialSlot());
            if (vi.getTrialCompleted() != null) lead.setTrialCompleted(vi.getTrialCompleted());
            if (vi.getInterestedInPT() != null) lead.setInterestedInPt(vi.getInterestedInPT());
            if (vi.getBodyFatPercentage() != null) lead.setBodyFatPercentage(vi.getBodyFatPercentage());
            if (vi.getHealthConditions() != null) lead.setHealthConditions(vi.getHealthConditions());
            if (vi.getNotes() != null) lead.setVisitorNotes(vi.getNotes());
        }

        if (request.getReminders() != null) {
            RemindersDto rem = request.getReminders();
            if (rem.getCallReminder() != null) lead.setCallReminder(rem.getCallReminder());
            if (rem.getCallTime() != null) lead.setCallTime(rem.getCallTime());
            if (rem.getMessageReminder() != null) lead.setMessageReminder(rem.getMessageReminder());
            if (rem.getPaymentReminder() != null) lead.setPaymentReminder(rem.getPaymentReminder());
            if (rem.getPaymentDueDate() != null) lead.setPaymentDueDate(rem.getPaymentDueDate());
        }

        if (request.getDiscussions() != null) {
            lead.getDiscussions().clear();
            for (DiscussionDto d : request.getDiscussions()) {
                LeadDiscussion disc = new LeadDiscussion(
                        lead,
                        LocalDateTime.now(),
                        d.getChannel() != null ? d.getChannel() : "Phone Call",
                        d.getAuthor() != null ? d.getAuthor() : "Admin Desk",
                        d.getNotes(),
                        d.getNextAction(),
                        d.getExpectedJoinDate()
                );
                lead.getDiscussions().add(disc);
            }
        }

        Lead saved = leadRepository.save(lead);
        return LeadResponse.fromEntity(saved);
    }

    @Override
    public void deleteLead(UUID id, UUID actorUserId) {
        validateAndGetActorUser(actorUserId);

        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new LeadNotFoundException("Lead not found with ID: " + id));

        if (!lead.getUser().getId().equals(actorUserId)) {
            throw new AccessDeniedException("Access denied: You do not have permission to delete this lead.");
        }

        leadRepository.delete(lead);
    }

    @Override
    public LeadResponse addDiscussion(UUID id, LeadDiscussionRequest request, UUID actorUserId) {
        validateAndGetActorUser(actorUserId);

        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new LeadNotFoundException("Lead not found with ID: " + id));

        if (!lead.getUser().getId().equals(actorUserId)) {
            throw new AccessDeniedException("Access denied: You do not have permission to log discussions for this lead.");
        }

        LeadDiscussion disc = new LeadDiscussion(
                lead,
                LocalDateTime.now(),
                request.getChannel() != null ? request.getChannel() : "Phone Call",
                request.getAuthor() != null ? request.getAuthor() : "Admin Desk",
                request.getNotes(),
                request.getNextAction(),
                request.getExpectedJoinDate()
        );
        lead.addDiscussion(disc);

        if (request.getNewStatus() != null && !request.getNewStatus().trim().isEmpty()) {
            lead.setStatus(request.getNewStatus().trim());
        }
        if (request.getNextFollowUpDate() != null && !request.getNextFollowUpDate().trim().isEmpty()) {
            lead.setFollowUpDate(parseLocalDate(request.getNextFollowUpDate()));
        }
        if (request.getNextFollowUpTime() != null && !request.getNextFollowUpTime().trim().isEmpty()) {
            lead.setFollowUpTime(request.getNextFollowUpTime());
        }
        if (request.getNextAction() != null && !request.getNextAction().trim().isEmpty()) {
            lead.setNextAction(request.getNextAction());
        }
        if (request.getExpectedJoinDate() != null && !request.getExpectedJoinDate().trim().isEmpty()) {
            lead.setExpectedJoiningDate(parseLocalDate(request.getExpectedJoinDate()));
        }

        Lead saved = leadRepository.save(lead);
        return LeadResponse.fromEntity(saved);
    }

    @Override
    public LeadResponse convertToMember(UUID id, UUID actorUserId) {
        validateAndGetActorUser(actorUserId);

        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new LeadNotFoundException("Lead not found with ID: " + id));

        if (!lead.getUser().getId().equals(actorUserId)) {
            throw new AccessDeniedException("Access denied: You do not have permission to convert this lead.");
        }

        lead.setStatus("CONVERTED");
        lead.setNextAction("Enrolled Member");
        lead.setNextActionTime("Active");

        Lead saved = leadRepository.save(lead);
        return LeadResponse.fromEntity(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public LeadStatsResponse getStats(UUID actorUserId) {
        validateAndGetActorUser(actorUserId);

        List<Lead> leads = leadRepository.findByUserIdOrderByCreatedAtDesc(actorUserId);

        LeadStatsResponse stats = new LeadStatsResponse();
        stats.setTotalLeads(leads.size());

        long newCount = leads.stream()
                .filter(l -> "NEW".equalsIgnoreCase(l.getStatus()) || "NEW_INQUIRY".equalsIgnoreCase(l.getStatus()))
                .count();
        long followUpCount = leads.stream()
                .filter(l -> "FOLLOWUP_SCHEDULED".equalsIgnoreCase(l.getStatus()) || "CONTACTED".equalsIgnoreCase(l.getStatus()))
                .count();
        long trialCount = leads.stream()
                .filter(l -> "TRIAL_VISITED".equalsIgnoreCase(l.getStatus()) || Boolean.TRUE.equals(l.getTrialCompleted()))
                .count();
        long convertedCount = leads.stream()
                .filter(l -> "CONVERTED".equalsIgnoreCase(l.getStatus()))
                .count();
        long paymentPendingCount = leads.stream()
                .filter(l -> "PAYMENT_PENDING".equalsIgnoreCase(l.getStatus()))
                .count();
        long lostCount = leads.stream()
                .filter(l -> "LOST".equalsIgnoreCase(l.getStatus()))
                .count();

        stats.setNewLeads(newCount);
        stats.setFollowUpLeads(followUpCount);
        stats.setTrialLeads(trialCount);
        stats.setConvertedLeads(convertedCount);
        stats.setPaymentPendingLeads(paymentPendingCount);
        stats.setLostLeads(lostCount);

        BigDecimal quotedSum = leads.stream()
                .map(l -> l.getQuotedAmount() != null ? l.getQuotedAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal depositSum = leads.stream()
                .map(l -> l.getDepositPaid() != null ? l.getDepositPaid() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal balanceSum = leads.stream()
                .map(l -> l.getBalanceDue() != null ? l.getBalanceDue() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        stats.setTotalQuotedRevenue(quotedSum);
        stats.setTotalDepositsCollected(depositSum);
        stats.setTotalBalanceDue(balanceSum);

        return stats;
    }

    @Override
    public LeadOptionsResponse getOptions() {
        return new LeadOptionsResponse(
                Arrays.asList("NEW_INQUIRY", "CONTACTED", "TRIAL_VISITED", "FOLLOWUP_SCHEDULED", "PAYMENT_PENDING", "CONVERTED", "LOST"),
                Arrays.asList("Walk-in", "Instagram", "Website", "Google", "Referral", "Facebook", "WhatsApp", "Other"),
                Arrays.asList("HIGH", "MEDIUM", "LOW"),
                Arrays.asList("Muscle Gain & Strength", "Weight Loss & Fat Burn", "Cardio & Stamina", "CrossFit & Functional", "Personal Training (1-on-1)", "General Fitness & Wellness"),
                Arrays.asList("Morning", "Afternoon", "Evening", "Night", "Flexible"),
                Arrays.asList("Phone Call", "WhatsApp", "In-Person Visit", "Trial Workout", "Front Desk Consultation")
        );
    }
}
