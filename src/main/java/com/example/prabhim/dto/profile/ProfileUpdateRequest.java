package com.example.prabhim.dto.profile;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProfileUpdateRequest {

    @JsonAlias({"fullName", "full_name"})
    private String name;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    private String email;

    private String phone;

    @JsonAlias({"alternatePhone", "alternate_phone", "secondaryPhone"})
    private String alternatePhone;

    @JsonAlias({"avatar", "profileImage", "profile_image", "photo", "imageUrl"})
    private String avatar;

    private String designation;

    private String address;
    private String city;
    private String state;
    private String pincode;
    private String gender;

    @JsonAlias({"dob", "dateOfBirth", "date_of_birth"})
    private String dob;

    @JsonAlias({"emergencyContactName", "emergency_contact_name"})
    private String emergencyContactName;

    @JsonAlias({"emergencyContactPhone", "emergency_contact_phone"})
    private String emergencyContactPhone;

    @JsonAlias({"emergencyContactRelation", "emergency_contact_relation"})
    private String emergencyContactRelation;

    private String bio;

    @JsonAlias({"adminId", "admin_id", "systemTag"})
    private String adminId;

    @JsonAlias({"adminSince", "admin_since"})
    private String adminSince;

    private String role;

    @JsonAlias({"whatsappAlerts", "whatsapp_alerts"})
    private Boolean whatsappAlerts;

    @JsonAlias({"emailNotifications", "email_notifications"})
    private Boolean emailNotifications;

    @JsonAlias({"twoFactorEnabled", "two_factor_enabled"})
    private Boolean twoFactorEnabled;

    @JsonAlias({"gymLogo", "gym_logo"})
    private String gymLogo;

    @JsonAlias({"memberId", "member_id"})
    private String memberId;

    public ProfileUpdateRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getAlternatePhone() {
        return alternatePhone;
    }

    public void setAlternatePhone(String alternatePhone) {
        this.alternatePhone = alternatePhone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
}
