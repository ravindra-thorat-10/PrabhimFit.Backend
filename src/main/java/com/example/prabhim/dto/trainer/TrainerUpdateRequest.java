package com.example.prabhim.dto.trainer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;

public class TrainerUpdateRequest {

    private String trainerFullName;
    private String fullName;
    private String firstName;
    private String lastName;

    @Email(message = "Please provide a valid email address")
    private String email;
    private String emailAddress;

    private String phone;
    private String phoneNumber;

    private String specialization;

    private String experience;

    @DecimalMin(value = "0.0", message = "Salary cannot be negative")
    private BigDecimal monthlySalary;

    private String shift;
    private String availability;

    private LocalDate joiningDate;
    private String joiningDateStr;

    private String bio;
    private String professionalBio;

    private String status;

    public TrainerUpdateRequest() {
    }

    public String resolveFullName() {
        if (trainerFullName != null && !trainerFullName.trim().isEmpty()) {
            return trainerFullName.trim();
        }
        if (fullName != null && !fullName.trim().isEmpty()) {
            return fullName.trim();
        }
        if (firstName != null && !firstName.trim().isEmpty()) {
            return (firstName + " " + (lastName != null ? lastName : "")).trim();
        }
        return null;
    }

    public String resolveEmail() {
        if (email != null && !email.trim().isEmpty()) {
            return email.trim();
        }
        if (emailAddress != null && !emailAddress.trim().isEmpty()) {
            return emailAddress.trim();
        }
        return null;
    }

    public String resolvePhone() {
        if (phone != null && !phone.trim().isEmpty()) {
            return phone.trim();
        }
        if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
            return phoneNumber.trim();
        }
        return null;
    }

    public String resolveShift() {
        if (shift != null && !shift.trim().isEmpty()) {
            return shift.trim();
        }
        if (availability != null && !availability.trim().isEmpty()) {
            return availability.trim();
        }
        return null;
    }

    public String resolveBio() {
        if (bio != null) {
            return bio.trim();
        }
        if (professionalBio != null) {
            return professionalBio.trim();
        }
        return null;
    }

    public LocalDate resolveJoiningDate() {
        if (joiningDate != null) {
            return joiningDate;
        }
        if (joiningDateStr != null && !joiningDateStr.trim().isEmpty()) {
            String str = joiningDateStr.trim();
            try {
                if (str.contains("-")) {
                    String[] parts = str.split("-");
                    if (parts[0].length() == 4) {
                        return LocalDate.parse(str, DateTimeFormatter.ISO_LOCAL_DATE);
                    } else if (parts[2].length() == 4) {
                        return LocalDate.parse(str, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                    }
                }
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    public String getTrainerFullName() {
        return trainerFullName;
    }

    public void setTrainerFullName(String trainerFullName) {
        this.trainerFullName = trainerFullName;
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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public String getJoiningDateStr() {
        return joiningDateStr;
    }

    public void setJoiningDateStr(String joiningDateStr) {
        this.joiningDateStr = joiningDateStr;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfessionalBio() {
        return professionalBio;
    }

    public void setProfessionalBio(String professionalBio) {
        this.professionalBio = professionalBio;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
