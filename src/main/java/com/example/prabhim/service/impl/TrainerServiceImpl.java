package com.example.prabhim.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.prabhim.dto.trainer.TrainerCreateRequest;
import com.example.prabhim.dto.trainer.TrainerResponse;
import com.example.prabhim.dto.trainer.TrainerStatsResponse;
import com.example.prabhim.dto.trainer.TrainerUpdateRequest;
import com.example.prabhim.entity.Trainer;
import com.example.prabhim.exception.TrainerAlreadyExistsException;
import com.example.prabhim.exception.TrainerNotFoundException;
import com.example.prabhim.repository.MemberRepository;
import com.example.prabhim.repository.TrainerRepository;
import com.example.prabhim.repository.UserRepository;
import com.example.prabhim.service.TrainerService;

import jakarta.annotation.PostConstruct;

@Service
@Transactional
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository trainerRepository;
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;

    public TrainerServiceImpl(
            TrainerRepository trainerRepository,
            MemberRepository memberRepository,
            UserRepository userRepository) {
        this.trainerRepository = trainerRepository;
        this.memberRepository = memberRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    @Override
    public void seedDefaultTrainersIfEmpty() {
        if (trainerRepository.count() == 0) {
            Trainer t1 = new Trainer(
                    "TR-101",
                    "Vikram Malhotra",
                    "vikram@gym.com",
                    "+91 98221 00000",
                    "Strength & Conditioning",
                    "3 Years",
                    new BigDecimal("38000.00"),
                    "Full Time (06:00 - 22:00)",
                    LocalDate.of(2026, 1, 15),
                    "Certified CSCS trainer specializing in powerlifting, hypertrophy, and functional movement.",
                    "ACTIVE"
            );

            Trainer t2 = new Trainer(
                    "TR-102",
                    "Priya Sharma",
                    "priya@gym.com",
                    "+91 98222 12345",
                    "HIIT & Functional Training",
                    "4 Years",
                    new BigDecimal("45000.00"),
                    "Morning (06:00 - 14:00)",
                    LocalDate.of(2025, 6, 1),
                    "Former national gymnast with expertise in mobility, calisthenics, and high-intensity metabolic conditioning.",
                    "ACTIVE"
            );

            Trainer t3 = new Trainer(
                    "TR-103",
                    "Rohan Joshi",
                    "rohan@gym.com",
                    "+91 98223 44556",
                    "Postural Rehab & Mobility",
                    "5 Years",
                    new BigDecimal("40000.00"),
                    "Evening (14:00 - 22:00)",
                    LocalDate.of(2024, 11, 10),
                    "Kinesiologist focusing on joint health, corrective exercise patterns, and posture optimization.",
                    "ACTIVE"
            );

            trainerRepository.saveAllAndFlush(List.of(t1, t2, t3));
        }
    }

    @Override
    public Page<TrainerResponse> getAllTrainers(String search, String status, String specialization, Pageable pageable) {
        seedDefaultTrainersIfEmpty();

        Pageable effectivePageable = pageable;
        if (pageable == null || pageable.getSort().isUnsorted()) {
            effectivePageable = PageRequest.of(
                    pageable != null ? pageable.getPageNumber() : 0,
                    pageable != null ? pageable.getPageSize() : 10,
                    Sort.by(Sort.Direction.ASC, "trainerCode")
            );
        }

        Page<Trainer> page = trainerRepository.searchTrainers(search, status, specialization, effectivePageable);
        return page.map(this::mapToResponse);
    }

    @Override
    public List<TrainerResponse> getActiveTrainers() {
        seedDefaultTrainersIfEmpty();
        List<Trainer> trainers = trainerRepository.findByStatusOrderByFullNameAsc("ACTIVE");
        return trainers.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public TrainerStatsResponse getTrainerStats() {
        seedDefaultTrainersIfEmpty();
        TrainerStatsResponse stats = new TrainerStatsResponse();
        stats.setTotalTrainers(trainerRepository.count());
        stats.setActiveTrainers(trainerRepository.countByStatusIgnoreCase("ACTIVE"));
        stats.setOnLeaveTrainers(trainerRepository.countByStatusIgnoreCase("ON_LEAVE"));
        stats.setInactiveTrainers(trainerRepository.countByStatusIgnoreCase("INACTIVE"));

        BigDecimal payroll = trainerRepository.sumActiveMonthlySalary();
        if (payroll == null || payroll.compareTo(BigDecimal.ZERO) == 0) {
            payroll = trainerRepository.sumTotalMonthlyPayroll();
        }
        stats.setTotalMonthlyPayroll(payroll != null ? payroll : BigDecimal.ZERO);
        stats.setAverageExperienceYears(4.0);
        stats.setMostPopularSpecialization("Strength & Conditioning");
        return stats;
    }

    @Override
    public TrainerResponse getTrainerById(UUID id) {
        seedDefaultTrainersIfEmpty();
        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new TrainerNotFoundException("Trainer not found with id: " + id));
        return mapToResponse(trainer);
    }

    @Override
    public TrainerResponse getTrainerByCode(String code) {
        Trainer trainer = trainerRepository.findByTrainerCode(code)
                .orElseThrow(() -> new TrainerNotFoundException("Trainer not found with code: " + code));
        return mapToResponse(trainer);
    }

    @Override
    public TrainerResponse createTrainer(TrainerCreateRequest request) {
        seedDefaultTrainersIfEmpty();
        String email = request.resolveEmail();
        if (email != null && trainerRepository.existsByEmailIgnoreCase(email)) {
            throw new TrainerAlreadyExistsException("Trainer with email '" + email + "' already exists");
        }

        String code = generateNextTrainerCode();

        Trainer trainer = new Trainer();
        trainer.setTrainerCode(code);
        trainer.setFullName(request.resolveFullName());
        trainer.setEmail(email != null ? email : (code.toLowerCase() + "@gym.com"));
        trainer.setPhone(request.resolvePhone() != null ? request.resolvePhone() : "+91 00000 00000");
        trainer.setSpecialization(request.getSpecialization());
        trainer.setExperience(request.getExperience() != null ? request.getExperience() : "1 Year");
        trainer.setMonthlySalary(request.getMonthlySalary() != null ? request.getMonthlySalary() : BigDecimal.ZERO);
        trainer.setShift(request.resolveShift());
        trainer.setJoiningDate(request.resolveJoiningDate());
        trainer.setBio(request.resolveBio());
        trainer.setStatus(request.getStatus() != null ? request.getStatus().toUpperCase() : "ACTIVE");

        Trainer saved = trainerRepository.save(trainer);
        return mapToResponse(saved);
    }

    @Override
    public TrainerResponse updateTrainer(UUID id, TrainerUpdateRequest request) {
        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new TrainerNotFoundException("Trainer not found with id: " + id));

        String newEmail = request.resolveEmail();
        if (newEmail != null && !newEmail.equalsIgnoreCase(trainer.getEmail())) {
            if (trainerRepository.existsByEmailIgnoreCase(newEmail)) {
                throw new TrainerAlreadyExistsException("Trainer with email '" + newEmail + "' already exists");
            }
            trainer.setEmail(newEmail);
        }

        if (request.resolveFullName() != null) {
            trainer.setFullName(request.resolveFullName());
        }

        if (request.resolvePhone() != null) {
            trainer.setPhone(request.resolvePhone());
        }

        if (request.getSpecialization() != null) {
            trainer.setSpecialization(request.getSpecialization());
        }

        if (request.getExperience() != null) {
            trainer.setExperience(request.getExperience());
        }

        if (request.getMonthlySalary() != null) {
            trainer.setMonthlySalary(request.getMonthlySalary());
        }

        if (request.resolveShift() != null) {
            trainer.setShift(request.resolveShift());
        }

        if (request.resolveJoiningDate() != null) {
            trainer.setJoiningDate(request.resolveJoiningDate());
        }

        if (request.resolveBio() != null) {
            trainer.setBio(request.resolveBio());
        }

        if (request.getStatus() != null && !request.getStatus().trim().isEmpty()) {
            trainer.setStatus(request.getStatus().trim().toUpperCase());
        }

        Trainer updated = trainerRepository.save(trainer);
        return mapToResponse(updated);
    }

    @Override
    public void deleteTrainer(UUID id) {
        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new TrainerNotFoundException("Trainer not found with id: " + id));
        trainerRepository.delete(trainer);
    }

    private String generateNextTrainerCode() {
        long count = trainerRepository.count();
        long nextIndex = 101 + count;
        String candidate = "TR-" + nextIndex;
        while (trainerRepository.findByTrainerCode(candidate).isPresent()) {
            nextIndex++;
            candidate = "TR-" + nextIndex;
        }
        return candidate;
    }

    private TrainerResponse mapToResponse(Trainer trainer) {
        long clientCount = 0;
        try {
            clientCount = memberRepository.countByTrainerId(trainer.getId());
        } catch (Exception ignored) {
        }
        return TrainerResponse.fromEntity(trainer, clientCount);
    }
}
