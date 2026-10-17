package com.example.prabhim.dto.profile;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.prabhim.dto.UserResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProfileResponse {

    private UUID id;
    private String email;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    private String name;

    @JsonProperty("full_name")
    private String fullName;

    private String phone;

    @JsonProperty("alternate_phone")
    private String alternatePhone;

    private String designation;
    private String role;

    @JsonProperty("admin_id")
    private String adminId;

    @JsonProperty("admin_since")
    private String adminSince;

    private String avatar;

    @JsonProperty("profile_image")
    private String profileImage;

    private String address;
    private String city;
    private String state;
    private String pincode;
    private String gender;
    private String dob;

    @JsonProperty("emergency_contact_name")
    private String emergencyContactName;

    @JsonProperty("emergency_contact_phone")
    private String emergencyContactPhone;

    @JsonProperty("emergency_contact_relation")
    private String emergencyContactRelation;

    private String bio;

    @JsonProperty("whatsapp_alerts")
    private Boolean whatsappAlerts;

    @JsonProperty("email_notifications")
    private Boolean emailNotifications;

    @JsonProperty("two_factor_enabled")
    private Boolean twoFactorEnabled;

    @JsonProperty("gym_logo")
    private String gymLogo;

    @JsonProperty("is_email_verified")
    private boolean emailVerified;

    @JsonProperty("is_active")
    private boolean active;

    @JsonProperty("is_staff")
    private boolean staff;

    @JsonProperty("is_superuser")
    private boolean superuser;

    @JsonProperty("user")
    private UserResponse user;

    @JsonProperty("memberProfile")
    private Object memberProfile;

    @JsonProperty("adminProfile")
    private Object adminProfile;

    @JsonProperty("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime updatedAt;

    public ProfileResponse() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAlternatePhone() {
        return alternatePhone;
    }

    public void setAlternatePhone(String alternatePhone) {
        this.alternatePhone = alternatePhone;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getAdminSince() {
        return adminSince;
    }

    public void setAdminSince(String adminSince) {
        this.adminSince = adminSince;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
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

    public String getEmergencyContactRelation() {
        return emergencyContactRelation;
    }

    public void setEmergencyContactRelation(String emergencyContactRelation) {
        this.emergencyContactRelation = emergencyContactRelation;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Boolean getWhatsappAlerts() {
        return whatsappAlerts;
    }

    public void setWhatsappAlerts(Boolean whatsappAlerts) {
        this.whatsappAlerts = whatsappAlerts;
    }

    public Boolean getEmailNotifications() {
        return emailNotifications;
    }

    public void setEmailNotifications(Boolean emailNotifications) {
        this.emailNotifications = emailNotifications;
    }

    public Boolean getTwoFactorEnabled() {
        return twoFactorEnabled;
    }

    public void setTwoFactorEnabled(Boolean twoFactorEnabled) {
        this.twoFactorEnabled = twoFactorEnabled;
    }

    public String getGymLogo() {
        return gymLogo;
    }

    public void setGymLogo(String gymLogo) {
        this.gymLogo = gymLogo;
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

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public Object getMemberProfile() {
        return memberProfile;
    }

    public void setMemberProfile(Object memberProfile) {
        this.memberProfile = memberProfile;
    }

    public Object getAdminProfile() {
        return adminProfile;
    }

    public void setAdminProfile(Object adminProfile) {
        this.adminProfile = adminProfile;
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
