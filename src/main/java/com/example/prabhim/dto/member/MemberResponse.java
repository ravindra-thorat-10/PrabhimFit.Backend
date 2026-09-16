package com.example.prabhim.dto.member;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.example.prabhim.entity.Member;
import com.example.prabhim.entity.enums.Gender;
import com.example.prabhim.entity.enums.MemberStatus;

public class MemberResponse {

    private UUID id;
    private String memberCode;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String address;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private LocalDate joinDate;
    private MemberStatus status;
    private String profileImage;
    private TrainerSummaryDto trainer;
    private MembershipDto currentPlan;
    private LocalDateTime lastCheckIn;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public MemberResponse() {
    }

    public static MemberResponse fromEntity(Member member) {
        return fromEntity(member, null, null);
    }

    public static MemberResponse fromEntity(Member member, MembershipDto currentPlan) {
        return fromEntity(member, currentPlan, null);
    }

    public static MemberResponse fromEntity(Member member, MembershipDto currentPlan, LocalDateTime lastCheckIn) {
        if (member == null) {
            return null;
        }
        MemberResponse response = new MemberResponse();
        response.setId(member.getId());
        response.setMemberCode(member.getMemberCode());
        response.setFirstName(member.getFirstName());
        response.setLastName(member.getLastName());
        response.setFullName(member.getFullName());
        response.setEmail(member.getEmail());
        response.setPhone(member.getPhone());
        response.setDateOfBirth(member.getDateOfBirth());
        response.setGender(member.getGender());
        response.setAddress(member.getAddress());
        response.setEmergencyContactName(member.getEmergencyContactName());
        response.setEmergencyContactPhone(member.getEmergencyContactPhone());
        response.setJoinDate(member.getJoinDate());
        response.setStatus(member.getStatus());
        response.setProfileImage(member.getProfileImage());
        response.setLastCheckIn(lastCheckIn);
        response.setCreatedAt(member.getCreatedAt());
        response.setUpdatedAt(member.getUpdatedAt());

        if (member.getTrainer() != null) {
            response.setTrainer(new TrainerSummaryDto(
                    member.getTrainer().getId(),
                    member.getTrainer().getFirstName() + " " + (member.getTrainer().getLastName() != null ? member.getTrainer().getLastName() : "").trim(),
                    member.getTrainer().getEmail(),
                    member.getTrainer().getPhone(),
                    member.getTrainer().getDesignation(),
                    member.getTrainer().getAvatar() != null ? member.getTrainer().getAvatar() : member.getTrainer().getProfilePic()
            ));
        }

        response.setCurrentPlan(currentPlan);
        return response;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    public void setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
    }

    public String getEmergencyContactPhone() {
        return emergencyContactPhone;
    }

    public void setEmergencyContactPhone(String emergencyContactPhone) {
        this.emergencyContactPhone = emergencyContactPhone;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }

    public MemberStatus getStatus() {
        return status;
    }

    public void setStatus(MemberStatus status) {
        this.status = status;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public TrainerSummaryDto getTrainer() {
        return trainer;
    }

    public void setTrainer(TrainerSummaryDto trainer) {
        this.trainer = trainer;
    }

    public MembershipDto getCurrentPlan() {
        return currentPlan;
    }

    public void setCurrentPlan(MembershipDto currentPlan) {
        this.currentPlan = currentPlan;
    }

    public LocalDateTime getLastCheckIn() {
        return lastCheckIn;
    }

    public void setLastCheckIn(LocalDateTime lastCheckIn) {
        this.lastCheckIn = lastCheckIn;
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
