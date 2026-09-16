package com.example.prabhim.dto.trainer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.example.prabhim.entity.Trainer;

public class TrainerResponse {

    private UUID id;
    private String trainerCode;
    private String fullName;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String specialization;
    private String experience;
    private BigDecimal monthlySalary;
    private String monthlySalaryFormatted;
    private String shift;
    private LocalDate joiningDate;
    private String bio;
    private String avatar;
    private String status;
    private long assignedClientsCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TrainerResponse() {
    }

    public static TrainerResponse fromEntity(Trainer trainer, long assignedClientsCount) {
        if (trainer == null) {
            return null;
        }
        TrainerResponse resp = new TrainerResponse();
        resp.setId(trainer.getId());
        resp.setTrainerCode(trainer.getTrainerCode());
        resp.setFullName(trainer.getFullName());
        resp.setFirstName(trainer.getFirstName());
        resp.setLastName(trainer.getLastName());
        resp.setEmail(trainer.getEmail());
        resp.setPhone(trainer.getPhone());
        resp.setSpecialization(trainer.getSpecialization());
        resp.setExperience(trainer.getExperience());
        resp.setMonthlySalary(trainer.getMonthlySalary());
        if (trainer.getMonthlySalary() != null) {
            resp.setMonthlySalaryFormatted(TrainerStatsResponse.formatInr(trainer.getMonthlySalary()));
        }
        resp.setShift(trainer.getShift());
        resp.setJoiningDate(trainer.getJoiningDate());
        resp.setBio(trainer.getBio());
        resp.setAvatar(trainer.getAvatar());
        resp.setStatus(trainer.getStatus());
        resp.setAssignedClientsCount(assignedClientsCount);
        resp.setCreatedAt(trainer.getCreatedAt());
        resp.setUpdatedAt(trainer.getUpdatedAt());
        return resp;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTrainerCode() {
        return trainerCode;
    }

    public void setTrainerCode(String trainerCode) {
        this.trainerCode = trainerCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public BigDecimal getMonthlySalary() {
        return monthlySalary;
    }

    public void setMonthlySalary(BigDecimal monthlySalary) {
        this.monthlySalary = monthlySalary;
    }

    public String getMonthlySalaryFormatted() {
        return monthlySalaryFormatted;
    }

    public void setMonthlySalaryFormatted(String monthlySalaryFormatted) {
        this.monthlySalaryFormatted = monthlySalaryFormatted;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getAssignedClientsCount() {
        return assignedClientsCount;
    }

    public void setAssignedClientsCount(long assignedClientsCount) {
        this.assignedClientsCount = assignedClientsCount;
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
