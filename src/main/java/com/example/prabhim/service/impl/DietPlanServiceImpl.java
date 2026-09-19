package com.example.prabhim.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.prabhim.dto.diet.DietProtocolCreateRequest;
import com.example.prabhim.dto.diet.DietProtocolResponse;
import com.example.prabhim.dto.diet.DietProtocolUpdateRequest;
import com.example.prabhim.dto.diet.DietStatsResponse;
import com.example.prabhim.entity.DietPlan;
import com.example.prabhim.entity.Member;
import com.example.prabhim.entity.Trainer;
import com.example.prabhim.exception.DietNotFoundException;
import com.example.prabhim.repository.DietPlanRepository;
import com.example.prabhim.repository.MemberRepository;
import com.example.prabhim.repository.TrainerRepository;
import com.example.prabhim.service.DietPlanService;

import jakarta.persistence.criteria.Predicate;

@Service
@Transactional
public class DietPlanServiceImpl implements DietPlanService {

    private final DietPlanRepository dietPlanRepository;
    private final MemberRepository memberRepository;
    private final TrainerRepository trainerRepository;

    public DietPlanServiceImpl(
            DietPlanRepository dietPlanRepository,
            MemberRepository memberRepository,
            TrainerRepository trainerRepository
    ) {
        this.dietPlanRepository = dietPlanRepository;
        this.memberRepository = memberRepository;
        this.trainerRepository = trainerRepository;
    }

    @Override
    public DietProtocolResponse createDietPlan(DietProtocolCreateRequest request) {
        DietPlan dietPlan = new DietPlan();
        dietPlan.setPlanName(request.getName());
        dietPlan.setDailyCalories(request.getCalorieTarget());
        dietPlan.setDailyWaterTarget(request.getDailyWaterTarget());
        dietPlan.setBreakfastProtocol(request.getBreakfastProtocol());
        dietPlan.setLunchProtocol(request.getLunchProtocol());
        dietPlan.setDinnerProtocol(request.getDinnerProtocol());
        dietPlan.setSnacksProtocol(request.getSnacksProtocol());
        dietPlan.setSpecialGuidance(request.getSpecialGuidance());
        dietPlan.setProteinGrams(request.getProteinGrams());
        dietPlan.setCarbsGrams(request.getCarbsGrams());
        dietPlan.setFatGrams(request.getFatGrams());
        dietPlan.setMealSchedule(request.getMealSchedule());
        dietPlan.setStatus(request.getStatus() != null && !request.getStatus().trim().isEmpty()
                ? request.getStatus().trim().toUpperCase() : "ACTIVE");
        dietPlan.setStartDate(request.getStartDate());
        dietPlan.setEndDate(request.getEndDate());
        dietPlan.setNotes(request.getNotes());

        // Associate Athlete / Member
        resolveAndSetMember(dietPlan, request.getMemberId(), request.getMemberCode(), request.getAthleteName());

        // Associate Coach / Trainer
        resolveAndSetTrainer(dietPlan, request.getTrainerId(), request.getCoachName());

        DietPlan saved = dietPlanRepository.save(dietPlan);
        return DietProtocolResponse.fromEntity(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DietProtocolResponse> getAllDietPlans(
            String search,
            UUID memberId,
            UUID trainerId,
            String status,
            Pageable pageable
    ) {
        Specification<DietPlan> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (memberId != null) {
                predicates.add(cb.equal(root.get("member").get("id"), memberId));
            }

            if (trainerId != null) {
                predicates.add(cb.equal(root.get("trainer").get("id"), trainerId));
            }

            if (status != null && !status.trim().isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("status")), status.trim().toLowerCase()));
            }

            if (search != null && !search.trim().isEmpty()) {
                String searchPattern = "%" + search.trim().toLowerCase() + "%";
                Predicate planNameMatch = cb.like(cb.lower(root.get("planName")), searchPattern);
                Predicate athleteNameMatch = cb.like(cb.lower(root.get("athleteName")), searchPattern);
                Predicate coachNameMatch = cb.like(cb.lower(root.get("coachName")), searchPattern);
                Predicate guidanceMatch = cb.like(cb.lower(root.get("specialGuidance")), searchPattern);
                Predicate breakfastMatch = cb.like(cb.lower(root.get("breakfastProtocol")), searchPattern);
                Predicate lunchMatch = cb.like(cb.lower(root.get("lunchProtocol")), searchPattern);
                Predicate dinnerMatch = cb.like(cb.lower(root.get("dinnerProtocol")), searchPattern);
                Predicate snacksMatch = cb.like(cb.lower(root.get("snacksProtocol")), searchPattern);

                predicates.add(cb.or(
                        planNameMatch,
                        athleteNameMatch,
                        coachNameMatch,
                        guidanceMatch,
                        breakfastMatch,
                        lunchMatch,
                        dinnerMatch,
                        snacksMatch
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return dietPlanRepository.findAll(spec, pageable)
                .map(DietProtocolResponse::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public DietProtocolResponse getDietPlanById(UUID id) {
        DietPlan dietPlan = dietPlanRepository.findById(id)
                .orElseThrow(() -> new DietNotFoundException("Diet protocol not found with id: " + id));
        return DietProtocolResponse.fromEntity(dietPlan);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DietProtocolResponse> getDietPlansByMemberId(UUID memberId) {
        return dietPlanRepository.findByMemberIdOrderByCreatedAtDesc(memberId)
                .stream()
                .map(DietProtocolResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DietProtocolResponse getActiveDietPlanByMemberId(UUID memberId) {
        DietPlan dietPlan = dietPlanRepository.findFirstByMemberIdAndStatusOrderByCreatedAtDesc(memberId, "ACTIVE")
                .orElseThrow(() -> new DietNotFoundException("No active diet protocol found for member id: " + memberId));
        return DietProtocolResponse.fromEntity(dietPlan);
    }

    @Override
    public DietProtocolResponse updateDietPlan(UUID id, DietProtocolUpdateRequest request) {
        DietPlan dietPlan = dietPlanRepository.findById(id)
                .orElseThrow(() -> new DietNotFoundException("Diet protocol not found with id: " + id));

        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            dietPlan.setPlanName(request.getName().trim());
        }

        if (request.getCalorieTarget() != null) {
            dietPlan.setDailyCalories(request.getCalorieTarget());
        }

        if (request.getDailyWaterTarget() != null) {
            dietPlan.setDailyWaterTarget(request.getDailyWaterTarget());
        }

        if (request.getBreakfastProtocol() != null) {
            dietPlan.setBreakfastProtocol(request.getBreakfastProtocol());
        }

        if (request.getLunchProtocol() != null) {
            dietPlan.setLunchProtocol(request.getLunchProtocol());
        }

        if (request.getDinnerProtocol() != null) {
            dietPlan.setDinnerProtocol(request.getDinnerProtocol());
        }

        if (request.getSnacksProtocol() != null) {
            dietPlan.setSnacksProtocol(request.getSnacksProtocol());
        }

        if (request.getSpecialGuidance() != null) {
            dietPlan.setSpecialGuidance(request.getSpecialGuidance());
        }

        if (request.getProteinGrams() != null) {
            dietPlan.setProteinGrams(request.getProteinGrams());
        }

        if (request.getCarbsGrams() != null) {
            dietPlan.setCarbsGrams(request.getCarbsGrams());
        }

        if (request.getFatGrams() != null) {
            dietPlan.setFatGrams(request.getFatGrams());
        }

        if (request.getMealSchedule() != null) {
            dietPlan.setMealSchedule(request.getMealSchedule());
        }

        if (request.getStatus() != null && !request.getStatus().trim().isEmpty()) {
            dietPlan.setStatus(request.getStatus().trim().toUpperCase());
        }

        if (request.getStartDate() != null) {
            dietPlan.setStartDate(request.getStartDate());
        }

        if (request.getEndDate() != null) {
            dietPlan.setEndDate(request.getEndDate());
        }

        if (request.getNotes() != null) {
            dietPlan.setNotes(request.getNotes());
        }

        // Update Athlete / Member if supplied
        if (request.getMemberId() != null || request.getMemberCode() != null || request.getAthleteName() != null) {
            resolveAndSetMember(dietPlan, request.getMemberId(), request.getMemberCode(), request.getAthleteName());
        }

        // Update Coach / Trainer if supplied
        if (request.getTrainerId() != null || request.getCoachName() != null) {
            resolveAndSetTrainer(dietPlan, request.getTrainerId(), request.getCoachName());
        }

        DietPlan saved = dietPlanRepository.save(dietPlan);
        return DietProtocolResponse.fromEntity(saved);
    }

    @Override
    public DietProtocolResponse updateDietPlanStatus(UUID id, String status) {
        DietPlan dietPlan = dietPlanRepository.findById(id)
                .orElseThrow(() -> new DietNotFoundException("Diet protocol not found with id: " + id));

        if (status != null && !status.trim().isEmpty()) {
            dietPlan.setStatus(status.trim().toUpperCase());
        }

        DietPlan saved = dietPlanRepository.save(dietPlan);
        return DietProtocolResponse.fromEntity(saved);
    }

    @Override
    public void deleteDietPlan(UUID id) {
        DietPlan dietPlan = dietPlanRepository.findById(id)
                .orElseThrow(() -> new DietNotFoundException("Diet protocol not found with id: " + id));
        dietPlanRepository.delete(dietPlan);
    }

    @Override
    @Transactional(readOnly = true)
    public DietStatsResponse getDietStats() {
        long total = dietPlanRepository.count();
        long active = dietPlanRepository.countByStatusIgnoreCase("ACTIVE");
        long inactive = total - active;
        Double avgCalories = dietPlanRepository.findAverageCalories();
        Double avgWater = dietPlanRepository.findAverageWaterTarget();

        return new DietStatsResponse(
                total,
                active,
                inactive,
                avgCalories != null ? Math.round(avgCalories * 100.0) / 100.0 : 0.0,
                avgWater != null ? Math.round(avgWater * 10.0) / 10.0 : 0.0
        );
    }

    private void resolveAndSetMember(DietPlan dietPlan, UUID memberId, String memberCode, String athleteName) {
        if (memberId != null) {
            Member member = memberRepository.findById(memberId).orElse(null);
            if (member != null) {
                dietPlan.setMember(member);
                return;
            }
        }
        if (memberCode != null && !memberCode.trim().isEmpty()) {
            Member member = memberRepository.findByMemberCode(memberCode.trim()).orElse(null);
            if (member != null) {
                dietPlan.setMember(member);
                return;
            }
        }
        if (athleteName != null && !athleteName.trim().isEmpty()) {
            dietPlan.setAthleteName(athleteName.trim());
        }
    }

    private void resolveAndSetTrainer(DietPlan dietPlan, UUID trainerId, String coachName) {
        if (trainerId != null) {
            Trainer trainer = trainerRepository.findById(trainerId).orElse(null);
            if (trainer != null) {
                dietPlan.setTrainer(trainer);
                return;
            }
        }
        if (coachName != null && !coachName.trim().isEmpty()) {
            dietPlan.setCoachName(coachName.trim());
            dietPlan.setAssignedBy(coachName.trim());
        }
    }
}
