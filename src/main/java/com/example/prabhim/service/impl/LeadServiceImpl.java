package com.example.prabhim.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.prabhim.dto.lead.LeadConvertToMemberRequest;
import com.example.prabhim.dto.lead.LeadCreateRequest;
import com.example.prabhim.dto.lead.LeadPatchRequest;
import com.example.prabhim.dto.lead.LeadResponse;
import com.example.prabhim.dto.lead.LeadStatsResponse;
import com.example.prabhim.dto.lead.LeadUpdateRequest;
import com.example.prabhim.dto.member.MemberCreateRequest;
import com.example.prabhim.dto.member.MemberResponse;
import com.example.prabhim.dto.member.PageResponse;
import com.example.prabhim.entity.Lead;
import com.example.prabhim.entity.MembershipPlan;
import com.example.prabhim.entity.User;
import com.example.prabhim.entity.enums.Gender;
import com.example.prabhim.entity.enums.LeadPaymentStatus;
import com.example.prabhim.entity.enums.LeadPriority;
import com.example.prabhim.entity.enums.LeadSource;
import com.example.prabhim.entity.enums.LeadStatus;
import com.example.prabhim.entity.enums.PaymentModePreference;
import com.example.prabhim.exception.LeadNotFoundException;
import com.example.prabhim.repository.LeadRepository;
import com.example.prabhim.repository.MembershipPlanRepository;
import com.example.prabhim.repository.UserRepository;
import com.example.prabhim.service.LeadService;
import com.example.prabhim.service.MemberService;
import com.example.prabhim.specification.LeadSpecification;

@Service
@Transactional
public class LeadServiceImpl implements LeadService {

    private final LeadRepository leadRepository;
    private final MembershipPlanRepository membershipPlanRepository;
    private final UserRepository userRepository;
    private final MemberService memberService;

    public LeadServiceImpl(
            LeadRepository leadRepository,
            MembershipPlanRepository membershipPlanRepository,
            UserRepository userRepository,
            MemberService memberService) {
        this.leadRepository = leadRepository;
        this.membershipPlanRepository = membershipPlanRepository;
        this.userRepository = userRepository;
        this.memberService = memberService;
    }

    @Override
    public LeadResponse createLead(LeadCreateRequest request, UUID actorUserId) {
        Lead lead = new Lead();

        String code = request.getLeadCode();
        if (code == null || code.trim().isEmpty()) {
            code = generateNextLeadCode();
        } else {
            code = code.trim().toUpperCase();
        }
        lead.setLeadCode(code);

        // Section 1: Personal & Demographics
        lead.setFullName(request.getFullName() != null ? request.getFullName().trim() : "Walk-in Lead");
        lead.setPhone(request.getPhone() != null ? request.getPhone().trim() : "");
        lead.setGender(request.getGender() != null ? request.getGender() : Gender.MALE);
        lead.setAge(request.getAge() != null ? request.getAge() : 25);
        lead.setProfession(request.getProfession());
        lead.setResidentialArea(request.getResidentialArea());
        lead.setEmail(request.getEmail() != null ? request.getEmail().trim().toLowerCase() : null);
        lead.setEmergencyContact(request.getEmergencyContact());

        // Section 2: Fitness Goal & Physical Profile
        lead.setFitnessGoal(request.getFitnessGoal());
        lead.setPhysicalProfileNotes(request.getPhysicalProfileNotes());

        // Section 3: Membership Plan & Commercials
        lead.setPlanId(request.getPlanId());
        String planName = request.getPlanName();
        BigDecimal standardPrice = request.getStandardPrice();

        if (request.getPlanId() != null) {
            membershipPlanRepository.findById(request.getPlanId()).ifPresent(plan -> {
                lead.setPlanName(plan.getName());
                lead.setStandardPrice(plan.getPrice());
            });
        }
        if (planName != null && !planName.trim().isEmpty()) {
            lead.setPlanName(planName.trim());
        }
        if (standardPrice != null) {
            lead.setStandardPrice(standardPrice);
        }

        lead.setQuotedFee(request.getQuotedFee() != null ? request.getQuotedFee() : (lead.getStandardPrice() != null ? lead.getStandardPrice() : BigDecimal.ZERO));
        lead.setAdvanceToken(request.getAdvanceToken() != null ? request.getAdvanceToken() : BigDecimal.ZERO);
        lead.setPaymentModePreference(request.getPaymentModePreference() != null ? request.getPaymentModePreference() : PaymentModePreference.UPI);

        if (request.getPaymentStatus() != null) {
            lead.setPaymentStatus(request.getPaymentStatus());
        }

        // Section 4: Follow-up Schedule & Pipeline
        lead.setFollowUpDate(request.getFollowUpDate() != null ? request.getFollowUpDate() : LocalDate.now());
        lead.setFollowUpTime(request.getFollowUpTime() != null ? request.getFollowUpTime().trim() : "05:00 PM");
        lead.setExpectedJoiningDate(request.getExpectedJoiningDate() != null ? request.getExpectedJoiningDate() : LocalDate.now().plusDays(3));
        lead.setInquirySource(request.getInquirySource() != null ? request.getInquirySource() : LeadSource.WALK_IN);
        lead.setPriority(request.getPriority() != null ? request.getPriority() : LeadPriority.HIGH);
        lead.setStatus(request.getStatus() != null ? request.getStatus() : LeadStatus.NEW);

        // Section 5: Discussion Notes & Reminders
        lead.setDiscussionNotes(request.getDiscussionNotes());
        lead.setWhatsappReminder(request.getWhatsappReminder() != null ? request.getWhatsappReminder() : true);
        lead.setPhoneCallTask(request.getPhoneCallTask() != null ? request.getPhoneCallTask() : true);
        lead.setPaymentDuesReminder(request.getPaymentDuesReminder() != null ? request.getPaymentDuesReminder() : false);

        // Assigned User
        if (request.getAssignedToUserId() != null) {
            userRepository.findById(request.getAssignedToUserId()).ifPresent(lead::setAssignedTo);
        } else if (actorUserId != null) {
            userRepository.findById(actorUserId).ifPresent(lead::setAssignedTo);
        }

        lead.recalculateCommercials();
        Lead saved = leadRepository.save(lead);
        return LeadResponse.fromEntity(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<LeadResponse> getLeads(
            String search,
            Gender gender,
            LeadStatus status,
            LeadPriority priority,
            LeadSource source,
            LocalDate followUpStartDate,
            LocalDate followUpEndDate,
            LocalDate joiningStartDate,
            LocalDate joiningEndDate,
            Boolean actionRequiredToday,
            Boolean paymentDueOnly,
            Pageable pageable) {

        Specification<Lead> spec = LeadSpecification.filter(
                search, gender, status, priority, source,
                followUpStartDate, followUpEndDate,
                joiningStartDate, joiningEndDate,
                actionRequiredToday, paymentDueOnly
        );

        Page<Lead> page = leadRepository.findAll(spec, pageable);
        List<LeadResponse> items = page.getContent().stream()
                .map(LeadResponse::fromEntity)
                .collect(Collectors.toList());

        return PageResponse.fromPage(page, items);
    }

    @Override
    @Transactional(readOnly = true)
    public LeadResponse getLeadById(UUID id) {
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new LeadNotFoundException("Lead not found with ID: " + id));
        return LeadResponse.fromEntity(lead);
    }

    @Override
    @Transactional(readOnly = true)
    public LeadResponse getLeadByCode(String leadCode) {
        Lead lead = leadRepository.findByLeadCode(leadCode)
                .orElseThrow(() -> new LeadNotFoundException("Lead not found with code: " + leadCode));
        return LeadResponse.fromEntity(lead);
    }

    @Override
    public LeadResponse updateLead(UUID id, LeadUpdateRequest request, UUID actorUserId) {
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new LeadNotFoundException("Lead not found with ID: " + id));

        // Section 1: Personal & Demographics
        lead.setFullName(request.getFullName().trim());
        lead.setPhone(request.getPhone().trim());
        lead.setGender(request.getGender());
        lead.setAge(request.getAge());
        lead.setProfession(request.getProfession());
        lead.setResidentialArea(request.getResidentialArea());
        lead.setEmail(request.getEmail() != null ? request.getEmail().trim().toLowerCase() : null);
        lead.setEmergencyContact(request.getEmergencyContact());

        // Section 2: Fitness Goal & Physical Profile
        lead.setFitnessGoal(request.getFitnessGoal());
        lead.setPhysicalProfileNotes(request.getPhysicalProfileNotes());

        // Section 3: Commercials
        lead.setPlanId(request.getPlanId());
        if (request.getPlanName() != null) {
            lead.setPlanName(request.getPlanName().trim());
        }
        if (request.getStandardPrice() != null) {
            lead.setStandardPrice(request.getStandardPrice());
        }
        if (request.getQuotedFee() != null) {
            lead.setQuotedFee(request.getQuotedFee());
        }
        if (request.getAdvanceToken() != null) {
            lead.setAdvanceToken(request.getAdvanceToken());
        }
        if (request.getPaymentModePreference() != null) {
            lead.setPaymentModePreference(request.getPaymentModePreference());
        }
        if (request.getPaymentStatus() != null) {
            lead.setPaymentStatus(request.getPaymentStatus());
        }

        // Section 4: Follow-up & Pipeline
        lead.setFollowUpDate(request.getFollowUpDate());
        if (request.getFollowUpTime() != null) {
            lead.setFollowUpTime(request.getFollowUpTime().trim());
        }
        if (request.getExpectedJoiningDate() != null) {
            lead.setExpectedJoiningDate(request.getExpectedJoiningDate());
        }
        if (request.getInquirySource() != null) {
            lead.setInquirySource(request.getInquirySource());
        }
        if (request.getPriority() != null) {
            lead.setPriority(request.getPriority());
        }
        if (request.getStatus() != null) {
            lead.setStatus(request.getStatus());
        }

        // Section 5: Discussion & Reminders
        lead.setDiscussionNotes(request.getDiscussionNotes());
        if (request.getWhatsappReminder() != null) {
            lead.setWhatsappReminder(request.getWhatsappReminder());
        }
        if (request.getPhoneCallTask() != null) {
            lead.setPhoneCallTask(request.getPhoneCallTask());
        }
        if (request.getPaymentDuesReminder() != null) {
            lead.setPaymentDuesReminder(request.getPaymentDuesReminder());
        }

        if (request.getAssignedToUserId() != null) {
            userRepository.findById(request.getAssignedToUserId()).ifPresent(lead::setAssignedTo);
        }

        lead.recalculateCommercials();
        Lead updated = leadRepository.save(lead);
        return LeadResponse.fromEntity(updated);
    }

    @Override
    public LeadResponse patchLead(UUID id, LeadPatchRequest request, UUID actorUserId) {
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new LeadNotFoundException("Lead not found with ID: " + id));

        // Section 1
        if (request.getFullName() != null && !request.getFullName().trim().isEmpty()) {
            lead.setFullName(request.getFullName().trim());
        }
        if (request.getPhone() != null && !request.getPhone().trim().isEmpty()) {
            lead.setPhone(request.getPhone().trim());
        }
        if (request.getGender() != null) {
            lead.setGender(request.getGender());
        }
        if (request.getAge() != null) {
            lead.setAge(request.getAge());
        }
        if (request.getProfession() != null) {
            lead.setProfession(request.getProfession());
        }
        if (request.getResidentialArea() != null) {
            lead.setResidentialArea(request.getResidentialArea());
        }
        if (request.getEmail() != null) {
            lead.setEmail(request.getEmail().trim().toLowerCase());
        }
        if (request.getEmergencyContact() != null) {
            lead.setEmergencyContact(request.getEmergencyContact());
        }

        // Section 2
        if (request.getFitnessGoal() != null) {
            lead.setFitnessGoal(request.getFitnessGoal());
        }
        if (request.getPhysicalProfileNotes() != null) {
            lead.setPhysicalProfileNotes(request.getPhysicalProfileNotes());
        }

        // Section 3
        if (request.getPlanId() != null) {
            lead.setPlanId(request.getPlanId());
        }
        if (request.getPlanName() != null) {
            lead.setPlanName(request.getPlanName());
        }
        if (request.getStandardPrice() != null) {
            lead.setStandardPrice(request.getStandardPrice());
        }
        if (request.getQuotedFee() != null) {
            lead.setQuotedFee(request.getQuotedFee());
        }
        if (request.getAdvanceToken() != null) {
            lead.setAdvanceToken(request.getAdvanceToken());
        }
        if (request.getPaymentModePreference() != null) {
            lead.setPaymentModePreference(request.getPaymentModePreference());
        }
        if (request.getPaymentStatus() != null) {
            lead.setPaymentStatus(request.getPaymentStatus());
        }

        // Section 4
        if (request.getFollowUpDate() != null) {
            lead.setFollowUpDate(request.getFollowUpDate());
        }
        if (request.getFollowUpTime() != null) {
            lead.setFollowUpTime(request.getFollowUpTime().trim());
        }
        if (request.getExpectedJoiningDate() != null) {
            lead.setExpectedJoiningDate(request.getExpectedJoiningDate());
        }
        if (request.getInquirySource() != null) {
            lead.setInquirySource(request.getInquirySource());
        }
        if (request.getPriority() != null) {
            lead.setPriority(request.getPriority());
        }
        if (request.getStatus() != null) {
            lead.setStatus(request.getStatus());
        }

        // Section 5
        if (request.getDiscussionNotes() != null) {
            lead.setDiscussionNotes(request.getDiscussionNotes());
        }
        if (request.getWhatsappReminder() != null) {
            lead.setWhatsappReminder(request.getWhatsappReminder());
        }
        if (request.getPhoneCallTask() != null) {
            lead.setPhoneCallTask(request.getPhoneCallTask());
        }
        if (request.getPaymentDuesReminder() != null) {
            lead.setPaymentDuesReminder(request.getPaymentDuesReminder());
        }

        if (request.getAssignedToUserId() != null) {
            userRepository.findById(request.getAssignedToUserId()).ifPresent(lead::setAssignedTo);
        }

        lead.recalculateCommercials();
        Lead updated = leadRepository.save(lead);
        return LeadResponse.fromEntity(updated);
    }

    @Override
    public void deleteLead(UUID id, UUID actorUserId) {
        if (!leadRepository.existsById(id)) {
            throw new LeadNotFoundException("Lead not found with ID: " + id);
        }
        leadRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public LeadStatsResponse getLeadStats() {
        LocalDate today = LocalDate.now();
        LocalDate weekEnd = today.plusDays(7);
        YearMonth currentMonth = YearMonth.now();
        LocalDate monthStart = currentMonth.atDay(1);
        LocalDate monthEnd = currentMonth.atEndOfMonth();

        List<LeadStatus> closedStatuses = Arrays.asList(LeadStatus.WON, LeadStatus.CONVERTED, LeadStatus.LOST);

        long todaysDue = leadRepository.countDueOnDate(today, closedStatuses);
        long weeklyCalls = leadRepository.countDueBetween(today, weekEnd, closedStatuses);
        long thisMonth = leadRepository.countDueBetween(monthStart, monthEnd, closedStatuses);
        long paymentDue = leadRepository.countPendingPaymentDues(BigDecimal.ZERO, closedStatuses);
        long visitors = leadRepository.countByInquirySource(LeadSource.WALK_IN);
        long joinedGym = leadRepository.countByStatusIn(Arrays.asList(LeadStatus.WON, LeadStatus.CONVERTED));
        long totalLeads = leadRepository.count();
        long actionRequiredToday = leadRepository.countActionRequiredToday(today, closedStatuses);

        double conversionRate = 0.0;
        if (totalLeads > 0) {
            conversionRate = BigDecimal.valueOf((joinedGym * 100.0) / totalLeads)
                    .setScale(1, RoundingMode.HALF_UP)
                    .doubleValue();
        }

        return new LeadStatsResponse(
                todaysDue,
                weeklyCalls,
                thisMonth,
                paymentDue,
                visitors,
                joinedGym,
                conversionRate,
                totalLeads,
                actionRequiredToday
        );
    }

    @Override
    public MemberResponse convertLeadToMember(UUID id, LeadConvertToMemberRequest request, UUID actorUserId) {
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new LeadNotFoundException("Lead not found with ID: " + id));

        if (lead.getConvertedMemberId() != null) {
            throw new IllegalArgumentException("Lead is already converted to a member with ID: " + lead.getConvertedMemberId());
        }

        MemberCreateRequest memberReq = new MemberCreateRequest();
        memberReq.setFullName(lead.getFullName());
        memberReq.setFirstName(lead.getFirstName());
        memberReq.setLastName(lead.getLastName());
        
        // Handle Email
        String email = lead.getEmail();
        if (email == null || email.trim().isEmpty()) {
            String cleanPhone = lead.getPhone() != null ? lead.getPhone().replaceAll("[^0-9]", "") : "";
            email = "lead." + (cleanPhone.isEmpty() ? UUID.randomUUID().toString().substring(0, 8) : cleanPhone) + "@prabhimfit.local";
        }
        memberReq.setEmail(email);

        memberReq.setPhone(lead.getPhone());
        memberReq.setGender(lead.getGender());
        memberReq.setAddress(lead.getResidentialArea() != null ? lead.getResidentialArea() : lead.getProfession());
        memberReq.setEmergencyContactName(lead.getEmergencyContact());
        memberReq.setEmergencyContactPhone(lead.getEmergencyContact());

        LocalDate startDate = request != null && request.getStartDate() != null ? request.getStartDate() : LocalDate.now();
        memberReq.setJoinDate(startDate);

        // Membership plan
        if (lead.getPlanName() != null && !lead.getPlanName().trim().isEmpty()) {
            memberReq.setPlanName(lead.getPlanName());
            memberReq.setPlanPrice(lead.getQuotedFee());
            memberReq.setStartDate(startDate);
        }

        // Trainer
        if (request != null && request.getTrainerId() != null) {
            memberReq.setTrainerId(request.getTrainerId());
        }

        // Payment record
        boolean shouldRecordPayment = (request != null && Boolean.TRUE.equals(request.getRecordPayment()))
                || (lead.getAdvanceToken() != null && lead.getAdvanceToken().compareTo(BigDecimal.ZERO) > 0);
        memberReq.setRecordPayment(shouldRecordPayment);

        if (shouldRecordPayment) {
            BigDecimal amt = request != null && request.getAmountPaid() != null
                    ? request.getAmountPaid()
                    : (lead.getAdvanceToken() != null && lead.getAdvanceToken().compareTo(BigDecimal.ZERO) > 0
                    ? lead.getAdvanceToken()
                    : lead.getQuotedFee());
            memberReq.setAmountReceived(amt);
            String paymentMethod = request != null && request.getPaymentMethod() != null
                    ? request.getPaymentMethod()
                    : (lead.getPaymentModePreference() != null ? lead.getPaymentModePreference().name() : "UPI");
            memberReq.setPaymentMethod(paymentMethod);
        }

        MemberResponse createdMember = memberService.createMember(memberReq, actorUserId);

        lead.setConvertedMemberId(createdMember.getId());
        lead.setConvertedAt(LocalDateTime.now());
        lead.setStatus(LeadStatus.CONVERTED);
        leadRepository.save(lead);

        return createdMember;
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] exportLeadsCsv(
            String search,
            Gender gender,
            LeadStatus status,
            LeadPriority priority,
            LeadSource source,
            LocalDate followUpStartDate,
            LocalDate followUpEndDate,
            LocalDate joiningStartDate,
            LocalDate joiningEndDate,
            Boolean actionRequiredToday,
            Boolean paymentDueOnly) {

        Specification<Lead> spec = LeadSpecification.filter(
                search, gender, status, priority, source,
                followUpStartDate, followUpEndDate,
                joiningStartDate, joiningEndDate,
                actionRequiredToday, paymentDueOnly
        );

        List<Lead> leads = leadRepository.findAll(spec);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (PrintWriter writer = new PrintWriter(out, true, StandardCharsets.UTF_8)) {
            writer.println("Lead Code,Full Name,Phone,Email,Gender,Age,Profession,Residential Area,Fitness Goal,Plan Name,Quoted Fee,Advance Token,Pending Fee,Payment Status,Payment Mode,Follow-up Date,Follow-up Time,Expected Joining Date,Source,Priority,Status,Discussion Notes,Created At");

            for (Lead lead : leads) {
                writer.println(String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",%d,\"%s\",\"%s\",\"%s\",\"%s\",%.2f,%.2f,%.2f,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"",
                        escapeCsv(lead.getLeadCode()),
                        escapeCsv(lead.getFullName()),
                        escapeCsv(lead.getPhone()),
                        escapeCsv(lead.getEmail()),
                        lead.getGender() != null ? lead.getGender().name() : "",
                        lead.getAge() != null ? lead.getAge() : 0,
                        escapeCsv(lead.getProfession()),
                        escapeCsv(lead.getResidentialArea()),
                        escapeCsv(lead.getFitnessGoal()),
                        escapeCsv(lead.getPlanName()),
                        lead.getQuotedFee() != null ? lead.getQuotedFee() : BigDecimal.ZERO,
                        lead.getAdvanceToken() != null ? lead.getAdvanceToken() : BigDecimal.ZERO,
                        lead.getPendingPlanFee() != null ? lead.getPendingPlanFee() : BigDecimal.ZERO,
                        lead.getPaymentStatus() != null ? lead.getPaymentStatus().name() : "",
                        lead.getPaymentModePreference() != null ? lead.getPaymentModePreference().name() : "",
                        lead.getFollowUpDate() != null ? lead.getFollowUpDate().toString() : "",
                        escapeCsv(lead.getFollowUpTime()),
                        lead.getExpectedJoiningDate() != null ? lead.getExpectedJoiningDate().toString() : "",
                        lead.getInquirySource() != null ? lead.getInquirySource().name() : "",
                        lead.getPriority() != null ? lead.getPriority().name() : "",
                        lead.getStatus() != null ? lead.getStatus().name() : "",
                        escapeCsv(lead.getDiscussionNotes()),
                        lead.getCreatedAt() != null ? lead.getCreatedAt().toString() : ""
                ));
            }
        }

        return out.toByteArray();
    }

    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\"", "\"\"");
    }

    private synchronized String generateNextLeadCode() {
        long count = leadRepository.count();
        long nextId = count + 1001;
        String candidateCode = "LD-" + nextId;
        while (leadRepository.existsByLeadCode(candidateCode)) {
            nextId++;
            candidateCode = "LD-" + nextId;
        }
        return candidateCode;
    }
}
