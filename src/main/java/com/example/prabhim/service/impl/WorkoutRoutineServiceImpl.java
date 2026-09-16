package com.example.prabhim.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.prabhim.dto.workout.WorkoutExerciseRequest;
import com.example.prabhim.dto.workout.WorkoutRoutineCreateRequest;
import com.example.prabhim.dto.workout.WorkoutRoutineResponse;
import com.example.prabhim.dto.workout.WorkoutRoutineUpdateRequest;
import com.example.prabhim.entity.Member;
import com.example.prabhim.entity.Trainer;
import com.example.prabhim.entity.WorkoutExercise;
import com.example.prabhim.entity.WorkoutRoutine;
import com.example.prabhim.exception.WorkoutNotFoundException;
import com.example.prabhim.repository.MemberRepository;
import com.example.prabhim.repository.TrainerRepository;
import com.example.prabhim.repository.WorkoutExerciseRepository;
import com.example.prabhim.repository.WorkoutRoutineRepository;
import com.example.prabhim.service.WorkoutRoutineService;

import jakarta.persistence.criteria.Predicate;

@Service
@Transactional
public class WorkoutRoutineServiceImpl implements WorkoutRoutineService {

    private final WorkoutRoutineRepository workoutRoutineRepository;
    private final WorkoutExerciseRepository workoutExerciseRepository;
    private final MemberRepository memberRepository;
    private final TrainerRepository trainerRepository;

    public WorkoutRoutineServiceImpl(
            WorkoutRoutineRepository workoutRoutineRepository,
            WorkoutExerciseRepository workoutExerciseRepository,
            MemberRepository memberRepository,
            TrainerRepository trainerRepository
    ) {
        this.workoutRoutineRepository = workoutRoutineRepository;
        this.workoutExerciseRepository = workoutExerciseRepository;
        this.memberRepository = memberRepository;
        this.trainerRepository = trainerRepository;
    }

    @Override
    public WorkoutRoutineResponse createWorkoutRoutine(WorkoutRoutineCreateRequest request) {
        WorkoutRoutine routine = new WorkoutRoutine();
        routine.setName(request.getName());
        routine.setTrainingGoal(request.getTrainingGoal());
        routine.setDurationWeeks(request.getDurationWeeks());
        routine.setStatus(request.getStatus() != null ? request.getStatus().trim() : "ACTIVE");
        routine.setInstructions(request.getInstructions());

        // Associate Member / Athlete if provided
        if (request.getMemberId() != null) {
            Member member = memberRepository.findById(request.getMemberId()).orElse(null);
            if (member != null) {
                routine.setMember(member);
            } else if (request.getAthleteName() != null) {
                routine.setAthleteName(request.getAthleteName().trim());
            }
        } else if (request.getMemberCode() != null && !request.getMemberCode().trim().isEmpty()) {
            Member member = memberRepository.findByMemberCode(request.getMemberCode().trim()).orElse(null);
            if (member != null) {
                routine.setMember(member);
            } else if (request.getAthleteName() != null) {
                routine.setAthleteName(request.getAthleteName().trim());
            }
        } else if (request.getAthleteName() != null) {
            routine.setAthleteName(request.getAthleteName().trim());
        }

        // Associate Trainer / Coach if provided
        if (request.getTrainerId() != null) {
            Trainer trainer = trainerRepository.findById(request.getTrainerId()).orElse(null);
            if (trainer != null) {
                routine.setTrainer(trainer);
            } else if (request.getCoachName() != null) {
                routine.setCoachName(request.getCoachName().trim());
            }
        } else if (request.getCoachName() != null) {
            routine.setCoachName(request.getCoachName().trim());
        }

        // Add Prescribed Exercises Roster
        if (request.getExercises() != null && !request.getExercises().isEmpty()) {
            int order = 1;
            for (WorkoutExerciseRequest exReq : request.getExercises()) {
                WorkoutExercise exercise = new WorkoutExercise();
                exercise.setExerciseName(exReq.getExerciseName());
                exercise.setSets(exReq.getSets() != null ? exReq.getSets() : 1);
                exercise.setReps(exReq.getReps());
                exercise.setTargetWeight(exReq.getTargetWeight());
                exercise.setRestInterval(exReq.getRestInterval());
                exercise.setOrderIndex(exReq.getOrderIndex() != null ? exReq.getOrderIndex() : order++);
                exercise.setNotes(exReq.getNotes());
                routine.addExercise(exercise);
            }
        }

        WorkoutRoutine saved = workoutRoutineRepository.save(routine);
        return WorkoutRoutineResponse.fromEntity(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkoutRoutineResponse> getAllWorkoutRoutines(
            String search, UUID memberId, UUID trainerId, String status, Pageable pageable
    ) {
        Specification<WorkoutRoutine> spec = (root, query, cb) -> {
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
                Predicate nameMatch = cb.like(cb.lower(root.get("name")), searchPattern);
                Predicate goalMatch = cb.like(cb.lower(root.get("trainingGoal")), searchPattern);
                Predicate athleteMatch = cb.like(cb.lower(root.get("athleteName")), searchPattern);
                Predicate coachMatch = cb.like(cb.lower(root.get("coachName")), searchPattern);
                predicates.add(cb.or(nameMatch, goalMatch, athleteMatch, coachMatch));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return workoutRoutineRepository.findAll(spec, pageable)
                .map(WorkoutRoutineResponse::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public WorkoutRoutineResponse getWorkoutRoutineById(UUID id) {
        WorkoutRoutine routine = workoutRoutineRepository.findById(id)
                .orElseThrow(() -> new WorkoutNotFoundException("Workout routine not found with id: " + id));
        return WorkoutRoutineResponse.fromEntity(routine);
    }

    @Override
    public WorkoutRoutineResponse updateWorkoutRoutine(UUID id, WorkoutRoutineUpdateRequest request) {
        WorkoutRoutine routine = workoutRoutineRepository.findById(id)
                .orElseThrow(() -> new WorkoutNotFoundException("Workout routine not found with id: " + id));

        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            routine.setName(request.getName().trim());
        }

        if (request.getTrainingGoal() != null) {
            routine.setTrainingGoal(request.getTrainingGoal().trim());
        }

        if (request.getDurationWeeks() != null) {
            routine.setDurationWeeks(request.getDurationWeeks());
        }

        if (request.getStatus() != null && !request.getStatus().trim().isEmpty()) {
            routine.setStatus(request.getStatus().trim());
        }

        if (request.getInstructions() != null) {
            routine.setInstructions(request.getInstructions().trim());
        }

        // Update Member / Athlete if provided
        if (request.getMemberId() != null) {
            Member member = memberRepository.findById(request.getMemberId()).orElse(null);
            if (member != null) {
                routine.setMember(member);
            }
        } else if (request.getMemberCode() != null && !request.getMemberCode().trim().isEmpty()) {
            Member member = memberRepository.findByMemberCode(request.getMemberCode().trim()).orElse(null);
            if (member != null) {
                routine.setMember(member);
            }
        }
        if (request.getAthleteName() != null) {
            routine.setAthleteName(request.getAthleteName().trim());
        }

        // Update Trainer / Coach if provided
        if (request.getTrainerId() != null) {
            Trainer trainer = trainerRepository.findById(request.getTrainerId()).orElse(null);
            if (trainer != null) {
                routine.setTrainer(trainer);
            }
        }
        if (request.getCoachName() != null) {
            routine.setCoachName(request.getCoachName().trim());
        }

        // Update Exercises roster if provided
        if (request.getExercises() != null) {
            routine.clearExercises();
            int order = 1;
            for (WorkoutExerciseRequest exReq : request.getExercises()) {
                WorkoutExercise exercise = new WorkoutExercise();
                exercise.setExerciseName(exReq.getExerciseName());
                exercise.setSets(exReq.getSets() != null ? exReq.getSets() : 1);
                exercise.setReps(exReq.getReps());
                exercise.setTargetWeight(exReq.getTargetWeight());
                exercise.setRestInterval(exReq.getRestInterval());
                exercise.setOrderIndex(exReq.getOrderIndex() != null ? exReq.getOrderIndex() : order++);
                exercise.setNotes(exReq.getNotes());
                routine.addExercise(exercise);
            }
        }

        WorkoutRoutine saved = workoutRoutineRepository.save(routine);
        return WorkoutRoutineResponse.fromEntity(saved);
    }

    @Override
    public void deleteWorkoutRoutine(UUID id) {
        WorkoutRoutine routine = workoutRoutineRepository.findById(id)
                .orElseThrow(() -> new WorkoutNotFoundException("Workout routine not found with id: " + id));
        workoutRoutineRepository.delete(routine);
    }

    @Override
    public WorkoutRoutineResponse addExercise(UUID routineId, WorkoutExerciseRequest exerciseRequest) {
        WorkoutRoutine routine = workoutRoutineRepository.findById(routineId)
                .orElseThrow(() -> new WorkoutNotFoundException("Workout routine not found with id: " + routineId));

        WorkoutExercise exercise = new WorkoutExercise();
        exercise.setExerciseName(exerciseRequest.getExerciseName());
        exercise.setSets(exerciseRequest.getSets() != null ? exerciseRequest.getSets() : 1);
        exercise.setReps(exerciseRequest.getReps());
        exercise.setTargetWeight(exerciseRequest.getTargetWeight());
        exercise.setRestInterval(exerciseRequest.getRestInterval());
        exercise.setOrderIndex(exerciseRequest.getOrderIndex() != null ? exerciseRequest.getOrderIndex() : routine.getExercises().size() + 1);
        exercise.setNotes(exerciseRequest.getNotes());

        routine.addExercise(exercise);
        WorkoutRoutine saved = workoutRoutineRepository.save(routine);
        return WorkoutRoutineResponse.fromEntity(saved);
    }

    @Override
    public WorkoutRoutineResponse removeExercise(UUID routineId, UUID exerciseId) {
        WorkoutRoutine routine = workoutRoutineRepository.findById(routineId)
                .orElseThrow(() -> new WorkoutNotFoundException("Workout routine not found with id: " + routineId));

        WorkoutExercise target = routine.getExercises().stream()
                .filter(e -> e.getId() != null && e.getId().equals(exerciseId))
                .findFirst()
                .orElse(null);

        if (target != null) {
            routine.removeExercise(target);
            workoutRoutineRepository.save(routine);
        }

        return WorkoutRoutineResponse.fromEntity(routine);
    }
}
