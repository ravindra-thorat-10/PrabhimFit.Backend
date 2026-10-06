package com.example.prabhim.dto.settings;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SettingsDTO {

    private GymProfileDTO gymProfile;
    private GeneralDTO general;
    private MembershipDTO membership;
    private AttendanceDTO attendance;
    private TrainerDTO trainer;
    private WorkoutDTO workout;
    private NutritionDTO nutrition;
    private PaymentDTO payment;
    private InvoiceDTO invoice;
    private NotificationDTO notification;
    private SecurityDTO security;
    private AppearanceDTO appearance;

    public SettingsDTO() {
    }

    public GymProfileDTO getGymProfile() {
        return gymProfile;
    }

    public void setGymProfile(GymProfileDTO gymProfile) {
        this.gymProfile = gymProfile;
    }

    public GeneralDTO getGeneral() {
        return general;
    }

    public void setGeneral(GeneralDTO general) {
        this.general = general;
    }

    public MembershipDTO getMembership() {
        return membership;
    }

    public void setMembership(MembershipDTO membership) {
        this.membership = membership;
    }

    public AttendanceDTO getAttendance() {
        return attendance;
    }

    public void setAttendance(AttendanceDTO attendance) {
        this.attendance = attendance;
    }

    public TrainerDTO getTrainer() {
        return trainer;
    }

    public void setTrainer(TrainerDTO trainer) {
        this.trainer = trainer;
    }

    public WorkoutDTO getWorkout() {
        return workout;
    }

    public void setWorkout(WorkoutDTO workout) {
        this.workout = workout;
    }

    public NutritionDTO getNutrition() {
        return nutrition;
    }

    public void setNutrition(NutritionDTO nutrition) {
        this.nutrition = nutrition;
    }

    public PaymentDTO getPayment() {
        return payment;
    }

    public void setPayment(PaymentDTO payment) {
        this.payment = payment;
    }

    public InvoiceDTO getInvoice() {
        return invoice;
    }

    public void setInvoice(InvoiceDTO invoice) {
        this.invoice = invoice;
    }

    public NotificationDTO getNotification() {
        return notification;
    }

    public void setNotification(NotificationDTO notification) {
        this.notification = notification;
    }

    public SecurityDTO getSecurity() {
        return security;
    }

    public void setSecurity(SecurityDTO security) {
        this.security = security;
    }

    public AppearanceDTO getAppearance() {
        return appearance;
    }

    public void setAppearance(AppearanceDTO appearance) {
        this.appearance = appearance;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class GymProfileDTO {
        private String gymName;
        private String email;
        private String phone;
        private String address;
        private String city;
        private String state;
        private String pincode;
        private String country;
        private String website;
        private String logo;

        public GymProfileDTO() {
        }

        public String getGymName() {
            return gymName;
        }

        public void setGymName(String gymName) {
            this.gymName = gymName;
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

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class GeneralDTO {
        private String currency;
        private String timezone;
        private String dateFormat;
        private String language;

        public GeneralDTO() {
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getTimezone() {
            return timezone;
        }

        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }

        public String getDateFormat() {
            return dateFormat;
        }

        public void setDateFormat(String dateFormat) {
            this.dateFormat = dateFormat;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class MembershipDTO {
        private Integer defaultMembershipDurationDays;
        private Integer membershipExpiryReminderDays;
        private Boolean autoRenewalEnabled;
        private Boolean allowFreeze;
        private Integer maxFreezeDays;

        public MembershipDTO() {
        }

        public Integer getDefaultMembershipDurationDays() {
            return defaultMembershipDurationDays;
        }

        public void setDefaultMembershipDurationDays(Integer defaultMembershipDurationDays) {
            this.defaultMembershipDurationDays = defaultMembershipDurationDays;
        }

        public Integer getMembershipExpiryReminderDays() {
            return membershipExpiryReminderDays;
        }

        public void setMembershipExpiryReminderDays(Integer membershipExpiryReminderDays) {
            this.membershipExpiryReminderDays = membershipExpiryReminderDays;
        }

        public Boolean getAutoRenewalEnabled() {
            return autoRenewalEnabled;
        }

        public void setAutoRenewalEnabled(Boolean autoRenewalEnabled) {
            this.autoRenewalEnabled = autoRenewalEnabled;
        }

        public Boolean getAllowFreeze() {
            return allowFreeze;
        }

        public void setAllowFreeze(Boolean allowFreeze) {
            this.allowFreeze = allowFreeze;
        }

        public Integer getMaxFreezeDays() {
            return maxFreezeDays;
        }

        public void setMaxFreezeDays(Integer maxFreezeDays) {
            this.maxFreezeDays = maxFreezeDays;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AttendanceDTO {
        private Boolean attendanceEnabled;
        private Boolean checkInEnabled;
        private Boolean checkOutEnabled;
        private Boolean lateEntryAllowed;
        private Integer lateEntryMinutes;
        private Boolean allowMultipleCheckInsPerDay;

        public AttendanceDTO() {
        }

        public Boolean getAttendanceEnabled() {
            return attendanceEnabled;
        }

        public void setAttendanceEnabled(Boolean attendanceEnabled) {
            this.attendanceEnabled = attendanceEnabled;
        }

        public Boolean getCheckInEnabled() {
            return checkInEnabled;
        }

        public void setCheckInEnabled(Boolean checkInEnabled) {
            this.checkInEnabled = checkInEnabled;
        }

        public Boolean getCheckOutEnabled() {
            return checkOutEnabled;
        }

        public void setCheckOutEnabled(Boolean checkOutEnabled) {
            this.checkOutEnabled = checkOutEnabled;
        }

        public Boolean getLateEntryAllowed() {
            return lateEntryAllowed;
        }

        public void setLateEntryAllowed(Boolean lateEntryAllowed) {
            this.lateEntryAllowed = lateEntryAllowed;
        }

        public Integer getLateEntryMinutes() {
            return lateEntryMinutes;
        }

        public void setLateEntryMinutes(Integer lateEntryMinutes) {
            this.lateEntryMinutes = lateEntryMinutes;
        }

        public Boolean getAllowMultipleCheckInsPerDay() {
            return allowMultipleCheckInsPerDay;
        }

        public void setAllowMultipleCheckInsPerDay(Boolean allowMultipleCheckInsPerDay) {
            this.allowMultipleCheckInsPerDay = allowMultipleCheckInsPerDay;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class TrainerDTO {
        private Boolean trainerCommissionEnabled;
        private Integer defaultCommissionPercentage;
        private Boolean trainerAttendanceRequired;

        public TrainerDTO() {
        }

        public Boolean getTrainerCommissionEnabled() {
            return trainerCommissionEnabled;
        }

        public void setTrainerCommissionEnabled(Boolean trainerCommissionEnabled) {
            this.trainerCommissionEnabled = trainerCommissionEnabled;
        }

        public Integer getDefaultCommissionPercentage() {
            return defaultCommissionPercentage;
        }

        public void setDefaultCommissionPercentage(Integer defaultCommissionPercentage) {
            this.defaultCommissionPercentage = defaultCommissionPercentage;
        }

        public Boolean getTrainerAttendanceRequired() {
            return trainerAttendanceRequired;
        }

        public void setTrainerAttendanceRequired(Boolean trainerAttendanceRequired) {
            this.trainerAttendanceRequired = trainerAttendanceRequired;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class WorkoutDTO {
        private Boolean workoutPlansEnabled;
        private Boolean exerciseTrackingEnabled;
        private Boolean allowTrainerWorkoutAssignment;

        public WorkoutDTO() {
        }

        public Boolean getWorkoutPlansEnabled() {
            return workoutPlansEnabled;
        }

        public void setWorkoutPlansEnabled(Boolean workoutPlansEnabled) {
            this.workoutPlansEnabled = workoutPlansEnabled;
        }

        public Boolean getExerciseTrackingEnabled() {
            return exerciseTrackingEnabled;
        }

        public void setExerciseTrackingEnabled(Boolean exerciseTrackingEnabled) {
            this.exerciseTrackingEnabled = exerciseTrackingEnabled;
        }

        public Boolean getAllowTrainerWorkoutAssignment() {
            return allowTrainerWorkoutAssignment;
        }

        public void setAllowTrainerWorkoutAssignment(Boolean allowTrainerWorkoutAssignment) {
            this.allowTrainerWorkoutAssignment = allowTrainerWorkoutAssignment;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class NutritionDTO {
        private Boolean nutritionPlansEnabled;
        private Boolean mealTrackingEnabled;
        private Boolean allowTrainerNutritionAssignment;

        public NutritionDTO() {
        }

        public Boolean getNutritionPlansEnabled() {
            return nutritionPlansEnabled;
        }

        public void setNutritionPlansEnabled(Boolean nutritionPlansEnabled) {
            this.nutritionPlansEnabled = nutritionPlansEnabled;
        }

        public Boolean getMealTrackingEnabled() {
            return mealTrackingEnabled;
        }

        public void setMealTrackingEnabled(Boolean mealTrackingEnabled) {
            this.mealTrackingEnabled = mealTrackingEnabled;
        }

        public Boolean getAllowTrainerNutritionAssignment() {
            return allowTrainerNutritionAssignment;
        }

        public void setAllowTrainerNutritionAssignment(Boolean allowTrainerNutritionAssignment) {
            this.allowTrainerNutritionAssignment = allowTrainerNutritionAssignment;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PaymentDTO {
        private String defaultPaymentMethod;
        private String paymentMethods;
        private Boolean paymentReceiptEnabled;
        private Boolean taxEnabled;
        private BigDecimal taxRate;

        public PaymentDTO() {
        }

        public String getDefaultPaymentMethod() {
            return defaultPaymentMethod;
        }

        public void setDefaultPaymentMethod(String defaultPaymentMethod) {
            this.defaultPaymentMethod = defaultPaymentMethod;
        }

        public String getPaymentMethods() {
            return paymentMethods;
        }

        public void setPaymentMethods(String paymentMethods) {
            this.paymentMethods = paymentMethods;
        }

        public Boolean getPaymentReceiptEnabled() {
            return paymentReceiptEnabled;
        }

        public void setPaymentReceiptEnabled(Boolean paymentReceiptEnabled) {
            this.paymentReceiptEnabled = paymentReceiptEnabled;
        }

        public Boolean getTaxEnabled() {
            return taxEnabled;
        }

        public void setTaxEnabled(Boolean taxEnabled) {
            this.taxEnabled = taxEnabled;
        }

        public BigDecimal getTaxRate() {
            return taxRate;
        }

        public void setTaxRate(BigDecimal taxRate) {
            this.taxRate = taxRate;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class InvoiceDTO {
        private String invoicePrefix;
        private Boolean autoGenerateInvoiceNumber;
        private String invoiceTerms;
        private String invoiceNotes;

        public InvoiceDTO() {
        }

        public String getInvoicePrefix() {
            return invoicePrefix;
        }

        public void setInvoicePrefix(String invoicePrefix) {
            this.invoicePrefix = invoicePrefix;
        }

        public Boolean getAutoGenerateInvoiceNumber() {
            return autoGenerateInvoiceNumber;
        }

        public void setAutoGenerateInvoiceNumber(Boolean autoGenerateInvoiceNumber) {
            this.autoGenerateInvoiceNumber = autoGenerateInvoiceNumber;
        }

        public String getInvoiceTerms() {
            return invoiceTerms;
        }

        public void setInvoiceTerms(String invoiceTerms) {
            this.invoiceTerms = invoiceTerms;
        }

        public String getInvoiceNotes() {
            return invoiceNotes;
        }

        public void setInvoiceNotes(String invoiceNotes) {
            this.invoiceNotes = invoiceNotes;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class NotificationDTO {
        private Boolean emailNotificationsEnabled;
        private Boolean smsNotificationsEnabled;
        private Boolean membershipExpiryNotification;
        private Boolean paymentNotification;
        private Boolean attendanceNotification;

        public NotificationDTO() {
        }

        public Boolean getEmailNotificationsEnabled() {
            return emailNotificationsEnabled;
        }

        public void setEmailNotificationsEnabled(Boolean emailNotificationsEnabled) {
            this.emailNotificationsEnabled = emailNotificationsEnabled;
        }

        public Boolean getSmsNotificationsEnabled() {
            return smsNotificationsEnabled;
        }

        public void setSmsNotificationsEnabled(Boolean smsNotificationsEnabled) {
            this.smsNotificationsEnabled = smsNotificationsEnabled;
        }

        public Boolean getMembershipExpiryNotification() {
            return membershipExpiryNotification;
        }

        public void setMembershipExpiryNotification(Boolean membershipExpiryNotification) {
            this.membershipExpiryNotification = membershipExpiryNotification;
        }

        public Boolean getPaymentNotification() {
            return paymentNotification;
        }

        public void setPaymentNotification(Boolean paymentNotification) {
            this.paymentNotification = paymentNotification;
        }

        public Boolean getAttendanceNotification() {
            return attendanceNotification;
        }

        public void setAttendanceNotification(Boolean attendanceNotification) {
            this.attendanceNotification = attendanceNotification;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SecurityDTO {
        private Integer sessionTimeoutMinutes;
        private Boolean allowMultipleSessions;

        public SecurityDTO() {
        }

        public Integer getSessionTimeoutMinutes() {
            return sessionTimeoutMinutes;
        }

        public void setSessionTimeoutMinutes(Integer sessionTimeoutMinutes) {
            this.sessionTimeoutMinutes = sessionTimeoutMinutes;
        }

        public Boolean getAllowMultipleSessions() {
            return allowMultipleSessions;
        }

        public void setAllowMultipleSessions(Boolean allowMultipleSessions) {
            this.allowMultipleSessions = allowMultipleSessions;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AppearanceDTO {
        private String theme;

        public AppearanceDTO() {
        }

        public String getTheme() {
            return theme;
        }

        public void setTheme(String theme) {
            this.theme = theme;
        }
    }
}
