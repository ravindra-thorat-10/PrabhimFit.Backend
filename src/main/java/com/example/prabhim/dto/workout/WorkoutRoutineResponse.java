package com.example.prabhim.dto.workout;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.example.prabhim.entity.Member;
import com.example.prabhim.entity.WorkoutRoutine;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WorkoutRoutineResponse {

    private UUID id;
    private String name;
    private String workoutName;
    private UUID memberId;
    private String athleteName;
    private String memberName;
    private String memberCode;
    private WorkoutMemberSummary member;
    private UUID trainerId;
    private String coachName;
    private String trainingGoal;
    private String goal;
    private Integer durationWeeks;
    private String durationDisplay;
    private String status;
    private String instructions;
    private Integer exerciseCount;
    private List<WorkoutExerciseResponse> exercises = new ArrayList<>();
    private Object weeklySchedule;
    private String tenantId;
    private String creatorEmail;
    private String gymId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public WorkoutRoutineResponse() {
    }

    public static WorkoutRoutineResponse fromEntity(WorkoutRoutine entity) {
        if (entity == null) {
            return null;
        }
        WorkoutRoutineResponse resp = new WorkoutRoutineResponse();
        resp.setId(entity.getId());
        resp.setName(entity.getName());
        resp.setWorkoutName(entity.getName());
        resp.setTrainingGoal(entity.getTrainingGoal());
        resp.setGoal(entity.getTrainingGoal());
        resp.setDurationWeeks(entity.getDurationWeeks());
        if (entity.getDurationWeeks() != null) {
            resp.setDurationDisplay(entity.getDurationWeeks() + " Weeks Duration");
        }
        resp.setStatus(entity.getStatus() != null ? entity.getStatus() : "ACTIVE");
        resp.setInstructions(entity.getInstructions());

        if (entity.getMember() != null) {
            Member m = entity.getMember();
            resp.setMemberId(m.getId());
            resp.setMemberCode(m.getMemberCode());
            String fullName = (m.getFirstName() != null ? m.getFirstName() : "")
                    + " " + (m.getLastName() != null ? m.getLastName() : "");
            fullName = fullName.trim();
            resp.setAthleteName(fullName);
            resp.setMemberName(fullName);
            resp.setMember(new WorkoutMemberSummary(m.getId(), fullName, m.getMemberCode(), m.getAvatar(), m.getPhone()));
        } else {
            resp.setAthleteName(entity.getAthleteName());
            resp.setMemberName(entity.getAthleteName());
            if (entity.getAthleteName() != null) {
                resp.setMember(new WorkoutMemberSummary(null, entity.getAthleteName(), null, null, null));
            }
        }

        if (entity.getTrainer() != null) {
            resp.setTrainerId(entity.getTrainer().getId());
            resp.setCoachName(entity.getTrainer().getFullName());
        } else {
            resp.setCoachName(entity.getCoachName());
        }

        if (entity.getExercises() != null) {
            resp.setExercises(entity.getExercises().stream()
                    .map(WorkoutExerciseResponse::fromEntity)
                    .collect(Collectors.toList()));
            resp.setExerciseCount(entity.getExercises().size());
        } else {
            resp.setExerciseCount(0);
        }

        // Parse weekly schedule if present
        if (entity.getWeeklySchedule() != null && !entity.getWeeklySchedule().trim().isEmpty()) {
            try {
                resp.setWeeklySchedule(new ObjectMapper().readTree(entity.getWeeklySchedule()));
            } catch (Exception e) {
                resp.setWeeklySchedule(entity.getWeeklySchedule());
            }
        }

        // Tenancy
        String email = entity.getCreatorEmail() != null ? entity.getCreatorEmail() : (entity.getTenantId() != null ? entity.getTenantId() : "");
        if (email.isEmpty()) {
            try {
                var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
                if (auth != null && auth.getName() != null && !auth.getName().equalsIgnoreCase("anonymousUser")) {
                    email = auth.getName();
                }
            } catch (Exception ignored) {}
        }
        resp.setTenantId(email);
        resp.setCreatorEmail(email);
        resp.setGymId(email);

        resp.setCreatedAt(entity.getCreatedAt());
        resp.setUpdatedAt(entity.getUpdatedAt());
        return resp;
    }

    public static class WorkoutMemberSummary {
        private UUID id;
        private String fullName;
        private String name;
        private String memberCode;
        private String avatar;
        private String phone;

        public WorkoutMemberSummary() {
        }

        public WorkoutMemberSummary(UUID id, String fullName, String memberCode, String avatar, String phone) {
            this.id = id;
            this.fullName = fullName;
            this.name = fullName;
            this.memberCode = memberCode;
            this.avatar = avatar;
            this.phone = phone;
        }

        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
            this.name = fullName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMemberCode() {
            return memberCode;
        }

        public void setMemberCode(String memberCode) {
            this.memberCode = memberCode;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String get_id() {
        return id != null ? id.toString() : null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.workoutName = name;
    }

    public String getWorkoutName() {
        return workoutName != null ? workoutName : name;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public UUID getMemberId() {
        return memberId;
    }

    public void setMemberId(UUID memberId) {
        this.memberId = memberId;
    }

    public String getAthleteName() {
        return athleteName;
    }

    public void setAthleteName(String athleteName) {
        this.athleteName = athleteName;
        this.memberName = athleteName;
    }

    public String getMemberName() {
        return memberName != null ? memberName : athleteName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public WorkoutMemberSummary getMember() {
        return member;
    }

    public void setMember(WorkoutMemberSummary member) {
        this.member = member;
    }

    public UUID getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(UUID trainerId) {
        this.trainerId = trainerId;
    }

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }

    public String getTrainingGoal() {
        return trainingGoal;
    }

    public void setTrainingGoal(String trainingGoal) {
        this.trainingGoal = trainingGoal;
        this.goal = trainingGoal;
    }

    public String getGoal() {
        return goal != null ? goal : trainingGoal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public Integer getDurationWeeks() {
        return durationWeeks;
    }

    public void setDurationWeeks(Integer durationWeeks) {
        this.durationWeeks = durationWeeks;
    }

    public String getDurationDisplay() {
        return durationDisplay;
    }

    public void setDurationDisplay(String durationDisplay) {
        this.durationDisplay = durationDisplay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Integer getExerciseCount() {
        return exerciseCount;
    }

    public void setExerciseCount(Integer exerciseCount) {
        this.exerciseCount = exerciseCount;
    }

    public List<WorkoutExerciseResponse> getExercises() {
        return exercises;
    }

    public void setExercises(List<WorkoutExerciseResponse> exercises) {
        this.exercises = exercises != null ? exercises : new ArrayList<>();
    }

    public Object getWeeklySchedule() {
        return weeklySchedule;
    }

    public void setWeeklySchedule(Object weeklySchedule) {
        this.weeklySchedule = weeklySchedule;
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

    public String getGymId() {
        return gymId;
    }

    public void setGymId(String gymId) {
        this.gymId = gymId;
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
