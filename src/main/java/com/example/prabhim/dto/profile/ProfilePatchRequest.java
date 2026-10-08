package com.example.prabhim.dto.profile;

import com.fasterxml.jackson.annotation.JsonAlias;

public class ProfilePatchRequest {

    @JsonAlias({"name", "adminFullName"})
    private String fullName;

    private String firstName;
    private String lastName;

    @JsonAlias({"officialEmail", "officialEmailAddress"})
    private String email;

    @JsonAlias({"officialPhone", "officialPhoneNumber", "mobile"})
    private String phone;

    @JsonAlias({"officialTitle", "title", "roleTitle"})
    private String designation;

    @JsonAlias({"avatar", "profilePic", "photoUrl"})
    private String profileImage;

    @JsonAlias({"address", "residentialPostalAddress"})
    private String residentialAddress;

    private String city;
    private String state;

    @JsonAlias({"postalCode", "zipCode"})
    private String pincode;

    @JsonAlias({"contactName", "emergencyContactName"})
    private String secondaryContactName;

    @JsonAlias({"contactPhone", "emergencyContactPhone"})
    private String secondaryContactPhone;

    @JsonAlias({"relationRole", "relation", "role", "emergencyContactRole"})
    private String secondaryContactRole;

    @JsonAlias({"professionalStatement", "statement", "about"})
    private String bio;

    @JsonAlias({"linkedFacility", "gymName", "organizationName"})
    private String facilityName;

    private String timeZone;
    private String theme;

    public ProfilePatchRequest() {
    }

    // Getters and Setters

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

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
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
}
