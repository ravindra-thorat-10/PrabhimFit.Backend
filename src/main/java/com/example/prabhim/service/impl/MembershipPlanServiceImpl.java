package com.example.prabhim.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.prabhim.dto.plan.MembershipPlanCreateRequest;
import com.example.prabhim.dto.plan.MembershipPlanResponse;
import com.example.prabhim.dto.plan.MembershipPlanUpdateRequest;
import com.example.prabhim.entity.MembershipPlan;
import com.example.prabhim.entity.enums.MembershipStatus;
import com.example.prabhim.exception.PlanAlreadyExistsException;
import com.example.prabhim.exception.PlanNotFoundException;
import com.example.prabhim.repository.MembershipPlanRepository;
import com.example.prabhim.repository.MembershipRepository;
import com.example.prabhim.service.MembershipPlanService;

import jakarta.annotation.PostConstruct;

@Service
@Transactional
public class MembershipPlanServiceImpl implements MembershipPlanService {

    private final MembershipPlanRepository membershipPlanRepository;
    private final MembershipRepository membershipRepository;

    public MembershipPlanServiceImpl(
            MembershipPlanRepository membershipPlanRepository,
            MembershipRepository membershipRepository) {
        this.membershipPlanRepository = membershipPlanRepository;
        this.membershipRepository = membershipRepository;
    }

    @PostConstruct
    @Override
    public void seedDefaultPlansIfEmpty() {
        if (membershipPlanRepository.count() == 0) {
            MembershipPlan basic = new MembershipPlan(
                    "Basic Fitness",
                    "Standard cardio and strength floor access with locker facility.",
                    new BigDecimal("1500.00"),
                    1,
                    "Full Gym Floor Access, Locker Room & Shower, Cardio Deck & Free Weights, Free WiFi Access",
                    "ACTIVE"
            );

            MembershipPlan standard = new MembershipPlan(
                    "Standard Pro",
                    "Most popular quarterly pass including fitness evaluation and nutrition consultation.",
                    new BigDecimal("4000.00"),
                    3,
                    "All Basic Features, Quarterly Body Assessment, Custom Diet Plan, Sauna & Steam Bath, 2 Guest Passes",
                    "ACTIVE"
            );

            MembershipPlan premium = new MembershipPlan(
                    "Premium Elite",
                    "Comprehensive 6-month wellness and athletic conditioning plan with dedicated trainer support.",
                    new BigDecimal("7000.00"),
                    6,
                    "All Standard Features, Dedicated Personal Trainer, Bi-weekly Diet Updates, Turnstile RFID Priority Access, Unlimited Sauna Access, 5 Guest Passes",
                    "ACTIVE"
            );

            MembershipPlan vip = new MembershipPlan(
                    "Annual VIP Athlete",
                    "365 days of uncompromised performance access, recovery suits, and elite personal mentoring.",
                    new BigDecimal("12000.00"),
                    12,
                    "All Premium Features, Full Year Gym Access, Personal Nutritionist & Coach, Recovery Lounge Access, FitCore Athlete Welcome Kit, Merchandise 20% Discount",
                    "ACTIVE"
            );

            membershipPlanRepository.saveAllAndFlush(List.of(basic, standard, premium, vip));
        }
    }

    @Override
    public List<MembershipPlanResponse> getAllPlans() {
        seedDefaultPlansIfEmpty();
        List<MembershipPlan> plans = membershipPlanRepository.findAllByOrderByPriceAsc();
        return plans.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MembershipPlanResponse> getActivePlans() {
        seedDefaultPlansIfEmpty();
        List<MembershipPlan> plans = membershipPlanRepository.findByStatusOrderByPriceAsc("ACTIVE");
        return plans.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public MembershipPlanResponse getPlanById(UUID id) {
        MembershipPlan plan = membershipPlanRepository.findById(id)
                .orElseThrow(() -> new PlanNotFoundException("Membership plan not found with id: " + id));
        return mapToResponse(plan);
    }

    @Override
    public MembershipPlanResponse createPlan(MembershipPlanCreateRequest request) {
        String planName = request.resolveName();
        if (membershipPlanRepository.existsByNameIgnoreCase(planName)) {
            throw new PlanAlreadyExistsException("Membership plan with name '" + planName + "' already exists");
        }

        MembershipPlan plan = new MembershipPlan();
        plan.setName(planName);
        plan.setDescription(request.resolveDescription());
        plan.setPrice(request.getPrice() != null ? request.getPrice() : BigDecimal.ZERO);
        plan.setDurationMonths(request.getDurationMonths() != null ? request.getDurationMonths() : 1);
        plan.setFeatures(request.resolveFeaturesString());
        plan.setStatus(request.getStatus() != null ? request.getStatus().toUpperCase() : "ACTIVE");

        MembershipPlan saved = membershipPlanRepository.save(plan);
        return mapToResponse(saved);
    }

    @Override
    public MembershipPlanResponse updatePlan(UUID id, MembershipPlanUpdateRequest request) {
        MembershipPlan plan = membershipPlanRepository.findById(id)
                .orElseThrow(() -> new PlanNotFoundException("Membership plan not found with id: " + id));

        String newName = request.resolveName();
        if (newName != null && !newName.equalsIgnoreCase(plan.getName())) {
            if (membershipPlanRepository.existsByNameIgnoreCase(newName)) {
                throw new PlanAlreadyExistsException("Membership plan with name '" + newName + "' already exists");
            }
            plan.setName(newName);
        }

        if (request.resolveDescription() != null) {
            plan.setDescription(request.resolveDescription());
        }

        if (request.getPrice() != null) {
            plan.setPrice(request.getPrice());
        }

        if (request.getDurationMonths() != null) {
            plan.setDurationMonths(request.getDurationMonths());
        }

        if (request.resolveFeaturesString() != null) {
            plan.setFeatures(request.resolveFeaturesString());
        }

        if (request.getStatus() != null && !request.getStatus().trim().isEmpty()) {
            plan.setStatus(request.getStatus().trim().toUpperCase());
        }

        MembershipPlan updated = membershipPlanRepository.save(plan);
        return mapToResponse(updated);
    }

    @Override
    public void deletePlan(UUID id) {
        MembershipPlan plan = membershipPlanRepository.findById(id)
                .orElseThrow(() -> new PlanNotFoundException("Membership plan not found with id: " + id));
        membershipPlanRepository.delete(plan);
    }

    private MembershipPlanResponse mapToResponse(MembershipPlan plan) {
        long activeCount = 0;
        try {
            activeCount = membershipRepository.countByPlanNameIgnoreCaseAndStatus(plan.getName(), MembershipStatus.ACTIVE);
        } catch (Exception ignored) {
        }
        return MembershipPlanResponse.fromEntity(plan, activeCount);
    }
}
