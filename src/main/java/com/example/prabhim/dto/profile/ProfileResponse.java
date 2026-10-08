package com.example.prabhim.dto.profile;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.prabhim.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;

public class ProfileResponse {

    private UUID id;
    private String adminCode;
    private String roleBadge;
    private String status;

    // Header & Personal Information
    private String fullName;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String designation;
    private String profileImage;
    private String avatar;
    private String location;

    // Top KPI & Privilege Cards
    private String privilegeLevel;
    private String privilegeDescription;
    private String adminTenure;
    private String adminTenureSubtext;
    private String twoFactorSecurity;
    private String twoFactorSubtext;
    private boolean twoFactorEnabled;
    private boolean twoFactorVerified;
    private String linkedFacility;
    private String linkedFacilitySubtext;

    // Residential & Postal Information
    private String residentialAddress;
    private String city;
    private String state;
    private String pincode;

    // Emergency & Secondary Administrative Contact
    private String secondaryContactName;
    private String secondaryContactPhone;
    private String secondaryContactRole;

    // Admin Bio & Professional Statement
    private String bio;

    // System Settings
    private String timeZone;
    private String theme;
    private boolean emailVerified;
    private boolean active;
    private boolean staff;
    private boolean superuser;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime updatedAt;

    public ProfileResponse() {
    }

    public static ProfileResponse fromEntity(User user) {
        if (user == null) {
            return null;
        }

        ProfileResponse dto = new ProfileResponse();
        dto.setId(user.getId());
        dto.setAdminCode(user.getAdminCode() != null ? user.getAdminCode() : "#ADM-2024-001");
        dto.setRoleBadge(user.isSuperuser() ? "SUPER ADMIN" : (user.isStaff() ? "ADMIN" : "MEMBER"));
        dto.setStatus(user.isActive() ? "Active Now" : "Inactive");

        dto.setFullName(user.getFullName());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone() != null ? user.getPhone() : "");
        dto.setDesignation(user.getDesignation() != null ? user.getDesignation() : "Managing Director & Chief Administrator");
        dto.setProfileImage(user.getProfileImage() != null ? user.getProfileImage() : user.getAvatar());
        dto.setAvatar(user.getAvatar() != null ? user.getAvatar() : user.getProfileImage());

        String city = user.getCity() != null ? user.getCity() : "Pune";
        String state = user.getState() != null ? user.getState() : "Maharashtra";
        dto.setLocation(city + ", " + state);

        dto.setPrivilegeLevel(user.getPrivilegeLevel() != null ? user.getPrivilegeLevel() : (user.isSuperuser() ? "Full Root / Superadmin" : "Staff Administrator"));
        dto.setPrivilegeDescription("Unrestricted System Authority");
        dto.setAdminTenure(user.getAdminTenure() != null ? user.getAdminTenure() : "Since Jan 2023");
        dto.setAdminTenureSubtext("Primary Facility Founder");

        dto.setTwoFactorEnabled(user.isTwoFactorEnabled());
        dto.setTwoFactorVerified(user.isTwoFactorVerified());
        dto.setTwoFactorSecurity(user.isTwoFactorEnabled() ? "Enabled & Verified" : "Disabled");
        dto.setTwoFactorSubtext("Session encrypted with SSL");

        dto.setLinkedFacility(user.getFacilityName() != null ? user.getFacilityName() : "Vishal Fit Gym & Performance Club");
        dto.setLinkedFacilitySubtext("All Branches & Turfs");

        dto.setResidentialAddress(user.getResidentialAddress() != null ? user.getResidentialAddress() : "Flat 1002-A, Utsav Residency Phase 1, Awhalwadi Road");
        dto.setCity(city);
        dto.setState(state);
        dto.setPincode(user.getPincode() != null ? user.getPincode() : "412207");

        dto.setSecondaryContactName(user.getSecondaryContactName() != null ? user.getSecondaryContactName() : "Ravindra Thorat");
        dto.setSecondaryContactPhone(user.getSecondaryContactPhone() != null ? user.getSecondaryContactPhone() : "+91 98220 12345");
        dto.setSecondaryContactRole(user.getSecondaryContactRole() != null ? user.getSecondaryContactRole() : "Operations Partner");

        dto.setBio(user.getBio() != null ? user.getBio() : "Master Administrator and Gym Owner overseeing elite fitness programming, operational billing, trainer staff, and member lifecycle intelligence.");

        dto.setTimeZone(user.getTimeZone() != null ? user.getTimeZone() : "Asia/Kolkata");
        dto.setTheme(user.getTheme() != null ? user.getTheme() : "dark");
        dto.setEmailVerified(user.isEmailVerified());
        dto.setActive(user.isActive());
        dto.setStaff(user.isStaff());
        dto.setSuperuser(user.isSuperuser());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());

        return dto;
    }

    // Getters and Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAdminCode() {
        return adminCode;
    }

    public void setAdminCode(String adminCode) {
        this.adminCode = adminCode;
    }

    public String getRoleBadge() {
        return roleBadge;
    }

    public void setRoleBadge(String roleBadge) {
        this.roleBadge = roleBadge;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPrivilegeLevel() {
        return privilegeLevel;
    }

    public void setPrivilegeLevel(String privilegeLevel) {
        this.privilegeLevel = privilegeLevel;
    }

    public String getPrivilegeDescription() {
        return privilegeDescription;
    }

    public void setPrivilegeDescription(String privilegeDescription) {
        this.privilegeDescription = privilegeDescription;
    }

    public String getAdminTenure() {
        return adminTenure;
    }

    public void setAdminTenure(String adminTenure) {
        this.adminTenure = adminTenure;
    }

    public String getAdminTenureSubtext() {
        return adminTenureSubtext;
    }

    public void setAdminTenureSubtext(String adminTenureSubtext) {
        this.adminTenureSubtext = adminTenureSubtext;
    }

    public String getTwoFactorSecurity() {
        return twoFactorSecurity;
    }

    public void setTwoFactorSecurity(String twoFactorSecurity) {
        this.twoFactorSecurity = twoFactorSecurity;
    }

    public String getTwoFactorSubtext() {
        return twoFactorSubtext;
    }

    public void setTwoFactorSubtext(String twoFactorSubtext) {
        this.twoFactorSubtext = twoFactorSubtext;
    }

    public boolean isTwoFactorEnabled() {
        return twoFactorEnabled;
    }

    public void setTwoFactorEnabled(boolean twoFactorEnabled) {
        this.twoFactorEnabled = twoFactorEnabled;
    }

    public boolean isTwoFactorVerified() {
        return twoFactorVerified;
    }

    public void setTwoFactorVerified(boolean twoFactorVerified) {
        this.twoFactorVerified = twoFactorVerified;
    }

    public String getLinkedFacility() {
        return linkedFacility;
    }

    public void setLinkedFacility(String linkedFacility) {
        this.linkedFacility = linkedFacility;
    }

    public String getLinkedFacilitySubtext() {
        return linkedFacilitySubtext;
    }

    public void setLinkedFacilitySubtext(String linkedFacilitySubtext) {
        this.linkedFacilitySubtext = linkedFacilitySubtext;
    }

    public String getResidentialAddress() {
        return residentialAddress;
    }

    public void setResidentialAddress(String residentialAddress) {
        this.residentialAddress = residentialAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getSecondaryContactName() {
        return secondaryContactName;
    }

    public void setSecondaryContactName(String secondaryContactName) {
        this.secondaryContactName = secondaryContactName;
    }

    public String getSecondaryContactPhone() {
        return secondaryContactPhone;
    }

    public void setSecondaryContactPhone(String secondaryContactPhone) {
        this.secondaryContactPhone = secondaryContactPhone;
    }

    public String getSecondaryContactRole() {
        return secondaryContactRole;
    }

    public void setSecondaryContactRole(String secondaryContactRole) {
        this.secondaryContactRole = secondaryContactRole;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isStaff() {
        return staff;
    }

    public void setStaff(boolean staff) {
        this.staff = staff;
    }

    public boolean isSuperuser() {
        return superuser;
    }

    public void setSuperuser(boolean superuser) {
        this.superuser = superuser;
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
