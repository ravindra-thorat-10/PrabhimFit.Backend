package com.example.prabhim.service.impl;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.prabhim.dto.member.AttendanceRecordDto;
import com.example.prabhim.dto.member.AttendanceSummaryDto;
import com.example.prabhim.dto.member.BodyMeasurementDto;
import com.example.prabhim.dto.member.DietPlanDto;
import com.example.prabhim.dto.member.MemberActivityLogDto;
import com.example.prabhim.dto.member.MemberCreateRequest;
import com.example.prabhim.dto.member.MemberPatchRequest;
import com.example.prabhim.dto.member.MemberProfileResponse;
import com.example.prabhim.dto.member.MemberRenewalRequest;
import com.example.prabhim.dto.member.MemberResponse;
import com.example.prabhim.dto.member.MemberStatsResponse;
import com.example.prabhim.dto.member.MemberUpdateRequest;
import com.example.prabhim.dto.member.MembershipDto;
import com.example.prabhim.dto.member.PageResponse;
import com.example.prabhim.dto.member.PaymentDto;
import com.example.prabhim.dto.member.PtSessionDto;
import com.example.prabhim.dto.member.QuickCheckInRequest;
import com.example.prabhim.dto.member.QuickCheckInResponse;
import com.example.prabhim.dto.member.TrainerSummaryDto;
import com.example.prabhim.dto.member.WorkoutLogDto;
import com.example.prabhim.entity.Attendance;
import com.example.prabhim.entity.Member;
import com.example.prabhim.entity.MemberActivityLog;
import com.example.prabhim.entity.Membership;
import com.example.prabhim.entity.Payment;
import com.example.prabhim.entity.User;
import com.example.prabhim.entity.enums.Gender;
import com.example.prabhim.entity.enums.MemberStatus;
import com.example.prabhim.entity.enums.MembershipStatus;
import com.example.prabhim.entity.enums.PaymentStatus;
import com.example.prabhim.exception.MemberAlreadyExistsException;
import com.example.prabhim.exception.MemberNotFoundException;
import com.example.prabhim.repository.AttendanceRepository;
import com.example.prabhim.repository.BodyMeasurementRepository;
import com.example.prabhim.repository.DietPlanRepository;
import com.example.prabhim.repository.MemberActivityLogRepository;
import com.example.prabhim.repository.MemberRepository;
import com.example.prabhim.repository.MembershipRepository;
import com.example.prabhim.repository.PaymentRepository;
import com.example.prabhim.repository.PtSessionRepository;
import com.example.prabhim.repository.UserRepository;
import com.example.prabhim.repository.WorkoutLogRepository;
import com.example.prabhim.service.MemberService;
import com.example.prabhim.specification.MemberSpecification;

@Service
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MembershipRepository membershipRepository;
    private final AttendanceRepository attendanceRepository;
    private final PaymentRepository paymentRepository;
    private final WorkoutLogRepository workoutLogRepository;
    private final DietPlanRepository dietPlanRepository;
    private final BodyMeasurementRepository bodyMeasurementRepository;
    private final PtSessionRepository ptSessionRepository;
    private final MemberActivityLogRepository memberActivityLogRepository;
    private final UserRepository userRepository;

    public MemberServiceImpl(
            MemberRepository memberRepository,
            MembershipRepository membershipRepository,
            AttendanceRepository attendanceRepository,
            PaymentRepository paymentRepository,
            WorkoutLogRepository workoutLogRepository,
            DietPlanRepository dietPlanRepository,
            BodyMeasurementRepository bodyMeasurementRepository,
            PtSessionRepository ptSessionRepository,
            MemberActivityLogRepository memberActivityLogRepository,
            UserRepository userRepository) {
        this.memberRepository = memberRepository;
        this.membershipRepository = membershipRepository;
        this.attendanceRepository = attendanceRepository;
        this.paymentRepository = paymentRepository;
        this.workoutLogRepository = workoutLogRepository;
        this.dietPlanRepository = dietPlanRepository;
        this.bodyMeasurementRepository = bodyMeasurementRepository;
        this.ptSessionRepository = ptSessionRepository;
        this.memberActivityLogRepository = memberActivityLogRepository;
        this.userRepository = userRepository;
    }

    @Override
    public MemberResponse createMember(MemberCreateRequest request, UUID actorUserId) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new MemberAlreadyExistsException("Member with email " + request.getEmail() + " already exists");
        }

        String memberCode = request.getMemberCode();
        if (memberCode == null || memberCode.trim().isEmpty()) {
            memberCode = generateNextMemberCode();
        } else {
            memberCode = memberCode.trim();
            if (memberRepository.existsByMemberCode(memberCode)) {
                throw new MemberAlreadyExistsException("Member code " + memberCode + " is already in use");
            }
        }

        Member member = new Member();
        member.setMemberCode(memberCode);
        member.setFirstName(request.getFirstName() != null ? request.getFirstName().trim() : "Member");
        member.setLastName(request.getLastName() != null ? request.getLastName().trim() : "");
        member.setEmail(request.getEmail().trim().toLowerCase());
        member.setPhone(request.getPhone());
        member.setDateOfBirth(request.getDateOfBirth());
        member.setGender(request.getGender());
        member.setAddress(request.getAddress());
        member.setEmergencyContactName(request.getEmergencyContactName());
        member.setEmergencyContactPhone(request.getEmergencyContactPhone());
        member.setJoinDate(request.getJoinDate() != null ? request.getJoinDate() : LocalDate.now());
        member.setStatus(request.getStatus() != null ? request.getStatus() : MemberStatus.ACTIVE);
        member.setProfileImage(request.getProfileImage());

        if (request.getTrainerId() != null) {
            User trainer = userRepository.findById(request.getTrainerId()).orElse(null);
            member.setTrainer(trainer);
        }

        Member savedMember = memberRepository.save(member);

        MembershipDto currentPlanDto = null;
        if (request.getPlanName() != null && !request.getPlanName().trim().isEmpty()) {
            Membership membership = new Membership();
            membership.setMember(savedMember);
            membership.setPlanName(request.getPlanName().trim());
            membership.setPlanType(request.getPlanType() != null ? request.getPlanType() : "STANDARD");
            LocalDate planStart = request.getStartDate() != null ? request.getStartDate() : savedMember.getJoinDate();
            membership.setStartDate(planStart);
            LocalDate planEnd = request.getEndDate();
            if (planEnd == null) {
                int durationMonths = request.getPlanDurationMonths() != null ? request.getPlanDurationMonths() : 1;
                planEnd = planStart.plusMonths(durationMonths);
            }
            membership.setEndDate(planEnd);
            membership.setPrice(request.getPlanPrice() != null ? request.getPlanPrice() : request.getAmountReceived());
            membership.setStatus(MembershipStatus.ACTIVE);
            membership.setAutoRenew(false);
            Membership savedMembership = membershipRepository.save(membership);
            currentPlanDto = MembershipDto.fromEntity(savedMembership);
        }

        // Record payment if requested or received
        if (Boolean.TRUE.equals(request.getRecordPayment()) || request.getAmountReceived() != null) {
            BigDecimal amount = request.getAmountReceived() != null ? request.getAmountReceived() : request.getPlanPrice();
            if (amount == null) {
                amount = BigDecimal.ZERO;
            }
            Payment payment = new Payment();
            payment.setMember(savedMember);
            payment.setAmount(amount);
            payment.setPaymentDate(LocalDateTime.now());
            payment.setPaymentMethod(request.getPaymentMethod() != null ? request.getPaymentMethod() : "CASH");
            payment.setStatus(PaymentStatus.PAID);
            payment.setInvoiceNumber("INV-" + (System.currentTimeMillis() % 1000000));
            payment.setDescription("Enrollment: " + (request.getPlanName() != null ? request.getPlanName() : "Membership"));
            paymentRepository.save(payment);
        }

        String actorName = resolveActorName(actorUserId);
        MemberActivityLog activityLog = new MemberActivityLog(
                savedMember,
                "MEMBER_CREATED",
                "Member registered with code " + savedMember.getMemberCode(),
                actorName
        );
        memberActivityLogRepository.save(activityLog);

        return MemberResponse.fromEntity(savedMember, currentPlanDto, null);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<MemberResponse> getMembers(
            String search,
            MemberStatus status,
            UUID trainerId,
            String membershipPlan,
            MembershipStatus membershipStatus,
            LocalDate startDate,
            LocalDate endDate,
            Gender gender,
            Pageable pageable) {

        Specification<Member> spec = MemberSpecification.filter(
                search, status, trainerId, membershipPlan, membershipStatus, startDate, endDate, gender
        );

        Page<Member> memberPage = memberRepository.findAll(spec, pageable);

        List<MemberResponse> responses = memberPage.getContent().stream().map(member -> {
            Membership latestMembership = membershipRepository.findLatestMembershipByMemberId(member.getId()).orElse(null);
            MembershipDto planDto = MembershipDto.fromEntity(latestMembership);
            LocalDateTime lastCheckIn = attendanceRepository.findLatestByMemberId(member.getId())
                    .map(Attendance::getCheckInTime)
                    .orElse(null);
            return MemberResponse.fromEntity(member, planDto, lastCheckIn);
        }).collect(Collectors.toList());

        return PageResponse.fromPage(memberPage, responses);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberResponse getMemberById(UUID id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + id));

        Membership latestMembership = membershipRepository.findLatestMembershipByMemberId(member.getId()).orElse(null);
        MembershipDto planDto = MembershipDto.fromEntity(latestMembership);
        return MemberResponse.fromEntity(member, planDto);
    }

    @Override
    public MemberResponse updateMember(UUID id, MemberUpdateRequest request, UUID actorUserId) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + id));

        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            String newEmail = request.getEmail().trim().toLowerCase();
            if (!member.getEmail().equalsIgnoreCase(newEmail) && memberRepository.existsByEmail(newEmail)) {
                throw new MemberAlreadyExistsException("Member with email " + newEmail + " already exists");
            }
            member.setEmail(newEmail);
        }

        if (request.getMemberCode() != null && !request.getMemberCode().trim().isEmpty()) {
            String newCode = request.getMemberCode().trim();
            if (!member.getMemberCode().equalsIgnoreCase(newCode) && memberRepository.existsByMemberCode(newCode)) {
                throw new MemberAlreadyExistsException("Member code " + newCode + " is already in use");
            }
            member.setMemberCode(newCode);
        }

        if (request.getFirstName() != null) {
            member.setFirstName(request.getFirstName().trim());
        }
        if (request.getLastName() != null) {
            member.setLastName(request.getLastName().trim());
        }
        if (request.getPhone() != null) {
            member.setPhone(request.getPhone());
        }
        if (request.getDateOfBirth() != null) {
            member.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getGender() != null) {
            member.setGender(request.getGender());
        }
        if (request.getAddress() != null) {
            member.setAddress(request.getAddress());
        }
        if (request.getEmergencyContactName() != null) {
            member.setEmergencyContactName(request.getEmergencyContactName());
        }
        if (request.getEmergencyContactPhone() != null) {
            member.setEmergencyContactPhone(request.getEmergencyContactPhone());
        }
        if (request.getJoinDate() != null) {
            member.setJoinDate(request.getJoinDate());
        }
        if (request.getStatus() != null) {
            member.setStatus(request.getStatus());
        }
        if (request.getProfileImage() != null) {
            member.setProfileImage(request.getProfileImage());
        }

        if (request.getTrainerId() != null) {
            User trainer = userRepository.findById(request.getTrainerId()).orElse(null);
            member.setTrainer(trainer);
        }

        Member updated = memberRepository.save(member);

        // Update membership plan if provided
        Membership latestMembership = membershipRepository.findLatestMembershipByMemberId(updated.getId()).orElse(null);
        if (request.getPlanName() != null && !request.getPlanName().trim().isEmpty()) {
            if (latestMembership != null) {
                latestMembership.setPlanName(request.getPlanName().trim());
                if (request.getStartDate() != null) {
                    latestMembership.setStartDate(request.getStartDate());
                }
                if (request.getEndDate() != null) {
                    latestMembership.setEndDate(request.getEndDate());
                }
                latestMembership = membershipRepository.save(latestMembership);
            } else {
                Membership newPlan = new Membership();
                newPlan.setMember(updated);
                newPlan.setPlanName(request.getPlanName().trim());
                newPlan.setStartDate(request.getStartDate() != null ? request.getStartDate() : LocalDate.now());
                newPlan.setEndDate(request.getEndDate() != null ? request.getEndDate() : LocalDate.now().plusMonths(1));
                newPlan.setStatus(MembershipStatus.ACTIVE);
                latestMembership = membershipRepository.save(newPlan);
            }
        } else if (latestMembership != null && request.getEndDate() != null) {
            latestMembership.setEndDate(request.getEndDate());
            if (request.getStartDate() != null) {
                latestMembership.setStartDate(request.getStartDate());
            }
            latestMembership = membershipRepository.save(latestMembership);
        }

        String actorName = resolveActorName(actorUserId);
        MemberActivityLog activityLog = new MemberActivityLog(
                updated,
                "MEMBER_UPDATED",
                "Member details updated",
                actorName
        );
        memberActivityLogRepository.save(activityLog);

        LocalDateTime lastCheckIn = attendanceRepository.findLatestByMemberId(updated.getId())
                .map(Attendance::getCheckInTime)
                .orElse(null);

        return MemberResponse.fromEntity(updated, MembershipDto.fromEntity(latestMembership), lastCheckIn);
    }

    @Override
    public MemberResponse patchMember(UUID id, MemberPatchRequest request, UUID actorUserId) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + id));

        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            String newEmail = request.getEmail().trim().toLowerCase();
            if (!member.getEmail().equalsIgnoreCase(newEmail) && memberRepository.existsByEmail(newEmail)) {
                throw new MemberAlreadyExistsException("Member with email " + newEmail + " already exists");
            }
            member.setEmail(newEmail);
        }

        if (request.getMemberCode() != null && !request.getMemberCode().trim().isEmpty()) {
            String newCode = request.getMemberCode().trim();
            if (!member.getMemberCode().equalsIgnoreCase(newCode) && memberRepository.existsByMemberCode(newCode)) {
                throw new MemberAlreadyExistsException("Member code " + newCode + " is already in use");
            }
            member.setMemberCode(newCode);
        }

        if (request.getFirstName() != null) {
            member.setFirstName(request.getFirstName().trim());
        }
        if (request.getLastName() != null) {
            member.setLastName(request.getLastName().trim());
        }
        if (request.getPhone() != null) {
            member.setPhone(request.getPhone());
        }
        if (request.getDateOfBirth() != null) {
            member.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getGender() != null) {
            member.setGender(request.getGender());
        }
        if (request.getAddress() != null) {
            member.setAddress(request.getAddress());
        }
        if (request.getEmergencyContactName() != null) {
            member.setEmergencyContactName(request.getEmergencyContactName());
        }
        if (request.getEmergencyContactPhone() != null) {
            member.setEmergencyContactPhone(request.getEmergencyContactPhone());
        }
        if (request.getJoinDate() != null) {
            member.setJoinDate(request.getJoinDate());
        }
        if (request.getStatus() != null) {
            member.setStatus(request.getStatus());
        }
        if (request.getProfileImage() != null) {
            member.setProfileImage(request.getProfileImage());
        }
        if (request.getTrainerId() != null) {
            User trainer = userRepository.findById(request.getTrainerId()).orElse(null);
            member.setTrainer(trainer);
        }

        Member updated = memberRepository.save(member);

        // Update membership plan if provided
        Membership latestMembership = membershipRepository.findLatestMembershipByMemberId(updated.getId()).orElse(null);
        if (request.getPlanName() != null && !request.getPlanName().trim().isEmpty()) {
            if (latestMembership != null) {
                latestMembership.setPlanName(request.getPlanName().trim());
                if (request.getStartDate() != null) {
                    latestMembership.setStartDate(request.getStartDate());
                }
                if (request.getEndDate() != null) {
                    latestMembership.setEndDate(request.getEndDate());
                }
                latestMembership = membershipRepository.save(latestMembership);
            } else {
                Membership newPlan = new Membership();
                newPlan.setMember(updated);
                newPlan.setPlanName(request.getPlanName().trim());
                newPlan.setStartDate(request.getStartDate() != null ? request.getStartDate() : LocalDate.now());
                newPlan.setEndDate(request.getEndDate() != null ? request.getEndDate() : LocalDate.now().plusMonths(1));
                newPlan.setStatus(MembershipStatus.ACTIVE);
                latestMembership = membershipRepository.save(newPlan);
            }
        } else if (latestMembership != null && request.getEndDate() != null) {
            latestMembership.setEndDate(request.getEndDate());
            if (request.getStartDate() != null) {
                latestMembership.setStartDate(request.getStartDate());
            }
            latestMembership = membershipRepository.save(latestMembership);
        }

        String actorName = resolveActorName(actorUserId);
        MemberActivityLog activityLog = new MemberActivityLog(
                updated,
                "MEMBER_PATCHED",
                "Member details partially updated",
                actorName
        );
        memberActivityLogRepository.save(activityLog);

        LocalDateTime lastCheckIn = attendanceRepository.findLatestByMemberId(updated.getId())
                .map(Attendance::getCheckInTime)
                .orElse(null);

        return MemberResponse.fromEntity(updated, MembershipDto.fromEntity(latestMembership), lastCheckIn);
    }

    @Override
    public void deleteMember(UUID id, UUID actorUserId) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + id));

        // Cascade delete child entities
        membershipRepository.deleteByMemberId(id);
        attendanceRepository.deleteByMemberId(id);
        paymentRepository.deleteByMemberId(id);
        workoutLogRepository.deleteByMemberId(id);
        dietPlanRepository.deleteByMemberId(id);
        bodyMeasurementRepository.deleteByMemberId(id);
        ptSessionRepository.deleteByMemberId(id);
        memberActivityLogRepository.deleteByMemberId(id);

        memberRepository.delete(member);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberProfileResponse getMemberProfile(UUID id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + id));

        // 1. Current Membership Plan
        Membership latestMembership = membershipRepository.findLatestMembershipByMemberId(id).orElse(null);
        MembershipDto membershipDto = MembershipDto.fromEntity(latestMembership);

        // 2. Member Response
        MemberResponse memberResponse = MemberResponse.fromEntity(member, membershipDto);

        // 3. Attendance Summary
        long totalVisits = attendanceRepository.countTotalVisitsByMemberId(id);
        LocalDateTime startOfMonth = YearMonth.now().atDay(1).atStartOfDay();
        long visitsThisMonth = attendanceRepository.countVisitsThisMonthByMemberId(id, startOfMonth);
        Attendance latestAttendance = attendanceRepository.findLatestByMemberId(id).orElse(null);
        LocalDateTime lastCheckIn = latestAttendance != null ? latestAttendance.getCheckInTime() : null;

        List<AttendanceRecordDto> recentRecords = attendanceRepository.findTop10ByMemberIdOrderByCheckInTimeDesc(id)
                .stream()
                .map(AttendanceRecordDto::fromEntity)
                .collect(Collectors.toList());

        AttendanceSummaryDto attendanceSummary = new AttendanceSummaryDto(
                totalVisits,
                visitsThisMonth,
                lastCheckIn,
                recentRecords
        );

        // 4. Trainer Summary
        TrainerSummaryDto trainerSummary = null;
        if (member.getTrainer() != null) {
            User trainer = member.getTrainer();
            trainerSummary = new TrainerSummaryDto(
                    trainer.getId(),
                    trainer.getFirstName() + " " + (trainer.getLastName() != null ? trainer.getLastName() : "").trim(),
                    trainer.getEmail(),
                    trainer.getPhone(),
                    trainer.getDesignation(),
                    trainer.getAvatar() != null ? trainer.getAvatar() : trainer.getProfilePic()
            );
        }

        // 5. Payments
        List<PaymentDto> payments = paymentRepository.findByMemberIdOrderByPaymentDateDesc(id)
                .stream()
                .map(PaymentDto::fromEntity)
                .collect(Collectors.toList());

        // 6. Workouts
        List<WorkoutLogDto> workouts = workoutLogRepository.findByMemberIdOrderByWorkoutDateDesc(id)
                .stream()
                .map(WorkoutLogDto::fromEntity)
                .collect(Collectors.toList());

        // 7. Body Measurements
        List<BodyMeasurementDto> measurements = bodyMeasurementRepository.findByMemberIdOrderByRecordedDateDesc(id)
                .stream()
                .map(BodyMeasurementDto::fromEntity)
                .collect(Collectors.toList());

        // 8. Diet Plan
        DietPlanDto dietPlanDto = dietPlanRepository.findFirstByMemberIdAndStatusOrderByCreatedAtDesc(id, "ACTIVE")
                .map(DietPlanDto::fromEntity)
                .orElse(null);

        // 9. PT Sessions
        List<PtSessionDto> ptSessions = ptSessionRepository.findByMemberIdOrderBySessionDateDesc(id)
                .stream()
                .map(PtSessionDto::fromEntity)
                .collect(Collectors.toList());

        // 10. Activity History
        List<MemberActivityLogDto> activityLogs = memberActivityLogRepository.findByMemberIdOrderByTimestampDesc(id)
                .stream()
                .map(MemberActivityLogDto::fromEntity)
                .collect(Collectors.toList());

        return new MemberProfileResponse(
                memberResponse,
                membershipDto,
                attendanceSummary,
                trainerSummary,
                payments,
                workouts,
                measurements,
                dietPlanDto,
                ptSessions,
                activityLogs
        );
    }

    @Override
    @Transactional(readOnly = true)
    public MemberStatsResponse getMemberStats() {
        long totalRecords = memberRepository.count();
        long activeSubscriptions = membershipRepository.countByStatus(MembershipStatus.ACTIVE);
        long expiring7Days = membershipRepository.countExpiringBetween(LocalDate.now(), LocalDate.now().plusDays(7));
        long temporaryHolds = memberRepository.countByStatus(MemberStatus.FROZEN) + memberRepository.countByStatus(MemberStatus.SUSPENDED);
        long lapsedMemberships = memberRepository.countByStatus(MemberStatus.INACTIVE) + memberRepository.countByStatus(MemberStatus.EXPIRED);

        if (activeSubscriptions == 0) {
            activeSubscriptions = memberRepository.countByStatus(MemberStatus.ACTIVE);
        }

        Double activeGrowthPercentage = -8.4;
        Double expiringGrowthPercentage = -1.2;
        Double holdsPercentage = 4.8;

        return new MemberStatsResponse(
                totalRecords,
                activeSubscriptions,
                activeGrowthPercentage,
                expiring7Days,
                expiringGrowthPercentage,
                temporaryHolds,
                holdsPercentage,
                lapsedMemberships
        );
    }

    @Override
    public QuickCheckInResponse quickCheckIn(UUID memberId, QuickCheckInRequest request, UUID actorUserId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + memberId));

        Attendance attendance = new Attendance();
        attendance.setMember(member);
        attendance.setCheckInTime(LocalDateTime.now());
        String facility = (request != null && request.getFacility() != null && !request.getFacility().trim().isEmpty())
                ? request.getFacility().trim()
                : "Downtown Flagship Turnstile";
        attendance.setFacility(facility);
        attendance.setStatus("PRESENT");

        Attendance savedAttendance = attendanceRepository.save(attendance);

        String actorName = resolveActorName(actorUserId);
        MemberActivityLog activityLog = new MemberActivityLog(
                member,
                "MEMBER_CHECK_IN",
                "Quick check-in recorded at " + facility,
                actorName
        );
        memberActivityLogRepository.save(activityLog);

        return new QuickCheckInResponse(
                savedAttendance.getId(),
                member.getId(),
                member.getFullName(),
                member.getMemberCode(),
                savedAttendance.getCheckInTime(),
                savedAttendance.getFacility(),
                savedAttendance.getStatus()
        );
    }

    @Override
    public MemberResponse renewMembership(UUID memberId, MemberRenewalRequest request, UUID actorUserId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + memberId));

        Membership latestMembership = membershipRepository.findLatestMembershipByMemberId(memberId).orElse(null);

        String planName = (request != null && request.getPlanName() != null && !request.getPlanName().trim().isEmpty())
                ? request.getPlanName().trim()
                : (latestMembership != null ? latestMembership.getPlanName() : "12-Month All Access");

        String planType = (request != null && request.getPlanType() != null && !request.getPlanType().trim().isEmpty())
                ? request.getPlanType().trim()
                : (latestMembership != null && latestMembership.getPlanType() != null ? latestMembership.getPlanType() : "STANDARD");

        int durationMonths = (request != null && request.getDurationMonths() != null && request.getDurationMonths() > 0)
                ? request.getDurationMonths()
                : 12;

        LocalDate startDate = LocalDate.now();
        if (latestMembership != null && latestMembership.getEndDate() != null && latestMembership.getEndDate().isAfter(LocalDate.now())) {
            startDate = latestMembership.getEndDate().plusDays(1);
        }

        LocalDate endDate = startDate.plusMonths(durationMonths);

        Membership newMembership = new Membership();
        newMembership.setMember(member);
        newMembership.setPlanName(planName);
        newMembership.setPlanType(planType);
        newMembership.setStartDate(startDate);
        newMembership.setEndDate(endDate);
        if (request != null && request.getPrice() != null) {
            newMembership.setPrice(request.getPrice());
        } else if (latestMembership != null) {
            newMembership.setPrice(latestMembership.getPrice());
        }
        newMembership.setStatus(MembershipStatus.ACTIVE);
        newMembership.setAutoRenew(request != null && request.getAutoRenew() != null ? request.getAutoRenew() : false);

        Membership savedMembership = membershipRepository.save(newMembership);

        // Update member status to ACTIVE
        member.setStatus(MemberStatus.ACTIVE);
        Member updatedMember = memberRepository.save(member);

        // Record payment if price exists
        if (newMembership.getPrice() != null) {
            Payment payment = new Payment();
            payment.setMember(member);
            payment.setAmount(newMembership.getPrice());
            payment.setPaymentDate(LocalDateTime.now());
            payment.setPaymentMethod(request != null && request.getPaymentMethod() != null ? request.getPaymentMethod() : "CARD");
            payment.setStatus(PaymentStatus.PAID);
            payment.setInvoiceNumber("INV-" + System.currentTimeMillis() % 1000000);
            payment.setDescription("Renewal: " + planName);
            paymentRepository.save(payment);
        }

        String actorName = resolveActorName(actorUserId);
        MemberActivityLog activityLog = new MemberActivityLog(
                member,
                "MEMBERSHIP_RENEWED",
                "Membership renewed for " + planName + " (" + durationMonths + " months) until " + endDate,
                actorName
        );
        memberActivityLogRepository.save(activityLog);

        LocalDateTime lastCheckIn = attendanceRepository.findLatestByMemberId(memberId)
                .map(Attendance::getCheckInTime)
                .orElse(null);

        return MemberResponse.fromEntity(updatedMember, MembershipDto.fromEntity(savedMembership), lastCheckIn);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainerSummaryDto> getTrainers() {
        List<User> trainers = userRepository.findByStaffTrueOrDesignationNotNullOrderByFirstNameAsc();
        if (trainers.isEmpty()) {
            trainers = userRepository.findAllByOrderByFirstNameAsc();
        }
        return trainers.stream().map(trainer -> new TrainerSummaryDto(
                trainer.getId(),
                trainer.getFirstName() + " " + (trainer.getLastName() != null ? trainer.getLastName() : "").trim(),
                trainer.getEmail(),
                trainer.getPhone(),
                trainer.getDesignation(),
                trainer.getAvatar() != null ? trainer.getAvatar() : trainer.getProfilePic()
        )).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] exportMembersCsv() {
        List<Member> members = memberRepository.findAll();
        StringBuilder csv = new StringBuilder();
        csv.append("Member ID,First Name,Last Name,Email,Phone,Gender,Join Date,Status,Plan,Plan Start Date,Plan Expiry Date,Assigned Trainer\n");

        for (Member m : members) {
            Membership latestMembership = membershipRepository.findLatestMembershipByMemberId(m.getId()).orElse(null);
            String planName = latestMembership != null ? latestMembership.getPlanName() : "None";
            String planStart = latestMembership != null && latestMembership.getStartDate() != null ? latestMembership.getStartDate().toString() : "";
            String planEnd = latestMembership != null && latestMembership.getEndDate() != null ? latestMembership.getEndDate().toString() : "";
            String trainer = m.getTrainer() != null
                    ? (m.getTrainer().getFirstName() + " " + (m.getTrainer().getLastName() != null ? m.getTrainer().getLastName() : "")).trim()
                    : "Unassigned";

            csv.append(String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"\n",
                    m.getMemberCode(),
                    m.getFirstName(),
                    m.getLastName() != null ? m.getLastName() : "",
                    m.getEmail(),
                    m.getPhone() != null ? m.getPhone() : "",
                    m.getGender() != null ? m.getGender().name() : "",
                    m.getJoinDate() != null ? m.getJoinDate().toString() : "",
                    m.getStatus() != null ? m.getStatus().name() : "",
                    planName,
                    planStart,
                    planEnd,
                    trainer
            ));
        }

        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    private String generateNextMemberCode() {
        long count = 1001 + memberRepository.count();
        String code = String.format("PF-%04d", count);
        while (memberRepository.existsByMemberCode(code)) {
            count++;
            code = String.format("PF-%04d", count);
        }
        return code;
    }

    private String resolveActorName(UUID actorUserId) {
        if (actorUserId == null) {
            return "System";
        }
        return userRepository.findById(actorUserId)
                .map(u -> u.getFirstName() + " " + (u.getLastName() != null ? u.getLastName() : "").trim())
                .orElse("Admin");
    }
}
