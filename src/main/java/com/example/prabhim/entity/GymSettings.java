package com.example.prabhim.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.example.prabhim.dto.settings.SettingsDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "gym_settings")
public class GymSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    // Gym Profile
    @Column(name = "gym_name")
    private String gymName = "";

    @Column(name = "email")
    private String email = "";

    @Column(name = "phone")
    private String phone = "";

    @Column(name = "address", columnDefinition = "TEXT")
    private String address = "";

    @Column(name = "city")
    private String city = "";

    @Column(name = "state")
    private String state = "";

    @Column(name = "pincode")
    private String pincode = "";

    @Column(name = "country")
    private String country = "India";

    @Column(name = "website")
    private String website = "";

    @Column(name = "logo", columnDefinition = "TEXT")
    private String logo = "";

    // General
    @Column(name = "currency")
    private String currency = "INR";

    @Column(name = "timezone")
    private String timezone = "Asia/Kolkata";

    @Column(name = "date_format")
    private String dateFormat = "YYYY-MM-DD";

    @Column(name = "language")
    private String language = "English";

    // Membership
    @Column(name = "default_membership_duration_days")
    private Integer defaultMembershipDurationDays = 30;

    @Column(name = "membership_expiry_reminder_days")
    private Integer membershipExpiryReminderDays = 7;

    @Column(name = "auto_renewal_enabled")
    private Boolean autoRenewalEnabled = false;

    @Column(name = "allow_freeze")
    private Boolean allowFreeze = true;

    @Column(name = "max_freeze_days")
    private Integer maxFreezeDays = 30;

    // Attendance
    @Column(name = "attendance_enabled")
    private Boolean attendanceEnabled = true;

    @Column(name = "check_in_enabled")
    private Boolean checkInEnabled = true;

    @Column(name = "check_out_enabled")
    private Boolean checkOutEnabled = true;

    @Column(name = "late_entry_allowed")
    private Boolean lateEntryAllowed = true;

    @Column(name = "late_entry_minutes")
    private Integer lateEntryMinutes = 15;

    @Column(name = "allow_multiple_check_ins_per_day")
    private Boolean allowMultipleCheckInsPerDay = false;

    // Trainer
    @Column(name = "trainer_commission_enabled")
    private Boolean trainerCommissionEnabled = false;

    @Column(name = "default_commission_percentage")
    private Integer defaultCommissionPercentage = 0;

    @Column(name = "trainer_attendance_required")
    private Boolean trainerAttendanceRequired = false;

    // Workout
    @Column(name = "workout_plans_enabled")
    private Boolean workoutPlansEnabled = true;

    @Column(name = "exercise_tracking_enabled")
    private Boolean exerciseTrackingEnabled = true;

    @Column(name = "allow_trainer_workout_assignment")
    private Boolean allowTrainerWorkoutAssignment = true;

    // Nutrition
    @Column(name = "nutrition_plans_enabled")
    private Boolean nutritionPlansEnabled = true;

    @Column(name = "meal_tracking_enabled")
    private Boolean mealTrackingEnabled = true;

    @Column(name = "allow_trainer_nutrition_assignment")
    private Boolean allowTrainerNutritionAssignment = true;

    // Payment
    @Column(name = "default_payment_method")
    private String defaultPaymentMethod = "UPI";

    @Column(name = "payment_methods")
    private String paymentMethods = "UPI,Cash,Bank Transfer,Card";

    @Column(name = "payment_receipt_enabled")
    private Boolean paymentReceiptEnabled = true;

    @Column(name = "tax_enabled")
    private Boolean taxEnabled = false;

    @Column(name = "tax_rate", precision = 5, scale = 2)
    private BigDecimal taxRate = BigDecimal.ZERO;

    // Invoice
    @Column(name = "invoice_prefix")
    private String invoicePrefix = "INV-";

    @Column(name = "auto_generate_invoice_number")
    private Boolean autoGenerateInvoiceNumber = true;

    @Column(name = "invoice_terms", columnDefinition = "TEXT")
    private String invoiceTerms = "Due on Receipt";

    @Column(name = "invoice_notes", columnDefinition = "TEXT")
    private String invoiceNotes = "";

    // Notification
    @Column(name = "email_notifications_enabled")
    private Boolean emailNotificationsEnabled = true;

    @Column(name = "sms_notifications_enabled")
    private Boolean smsNotificationsEnabled = false;

    @Column(name = "membership_expiry_notification")
    private Boolean membershipExpiryNotification = true;

    @Column(name = "payment_notification")
    private Boolean paymentNotification = true;

    @Column(name = "attendance_notification")
    private Boolean attendanceNotification = false;

    // Security
    @Column(name = "session_timeout_minutes")
    private Integer sessionTimeoutMinutes = 30;

    @Column(name = "allow_multiple_sessions")
    private Boolean allowMultipleSessions = true;

    // Appearance
    @Column(name = "theme")
    private String theme = "light";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public GymSettings() {
    }

    public static GymSettings createDefault(User user) {
        GymSettings settings = new GymSettings();
        settings.setUser(user);
        if (user != null) {
            if (user.getTimeZone() != null && !user.getTimeZone().trim().isEmpty()) {
                settings.setTimezone(user.getTimeZone());
            }
            if (user.getTheme() != null && !user.getTheme().trim().isEmpty()) {
                settings.setTheme(user.getTheme());
            }
        }
        return settings;
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.updatedAt == null) {
            this.updatedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public SettingsDTO toDto() {
        SettingsDTO dto = new SettingsDTO();

        // Gym Profile
        SettingsDTO.GymProfileDTO profile = new SettingsDTO.GymProfileDTO();
        profile.setGymName(this.gymName);
        profile.setEmail(this.email);
        profile.setPhone(this.phone);
        profile.setAddress(this.address);
        profile.setCity(this.city);
        profile.setState(this.state);
        profile.setPincode(this.pincode);
        profile.setCountry(this.country);
        profile.setWebsite(this.website);
        profile.setLogo(this.logo);
        dto.setGymProfile(profile);

        // General
        SettingsDTO.GeneralDTO gen = new SettingsDTO.GeneralDTO();
        gen.setCurrency(this.currency);
        gen.setTimezone(this.timezone);
        gen.setDateFormat(this.dateFormat);
        gen.setLanguage(this.language);
        dto.setGeneral(gen);

        // Membership
        SettingsDTO.MembershipDTO mem = new SettingsDTO.MembershipDTO();
        mem.setDefaultMembershipDurationDays(this.defaultMembershipDurationDays);
        mem.setMembershipExpiryReminderDays(this.membershipExpiryReminderDays);
        mem.setAutoRenewalEnabled(this.autoRenewalEnabled);
        mem.setAllowFreeze(this.allowFreeze);
        mem.setMaxFreezeDays(this.maxFreezeDays);
        dto.setMembership(mem);

        // Attendance
        SettingsDTO.AttendanceDTO att = new SettingsDTO.AttendanceDTO();
        att.setAttendanceEnabled(this.attendanceEnabled);
        att.setCheckInEnabled(this.checkInEnabled);
        att.setCheckOutEnabled(this.checkOutEnabled);
        att.setLateEntryAllowed(this.lateEntryAllowed);
        att.setLateEntryMinutes(this.lateEntryMinutes);
        att.setAllowMultipleCheckInsPerDay(this.allowMultipleCheckInsPerDay);
        dto.setAttendance(att);

        // Trainer
        SettingsDTO.TrainerDTO trn = new SettingsDTO.TrainerDTO();
        trn.setTrainerCommissionEnabled(this.trainerCommissionEnabled);
        trn.setDefaultCommissionPercentage(this.defaultCommissionPercentage);
        trn.setTrainerAttendanceRequired(this.trainerAttendanceRequired);
        dto.setTrainer(trn);

        // Workout
        SettingsDTO.WorkoutDTO wko = new SettingsDTO.WorkoutDTO();
        wko.setWorkoutPlansEnabled(this.workoutPlansEnabled);
        wko.setExerciseTrackingEnabled(this.exerciseTrackingEnabled);
        wko.setAllowTrainerWorkoutAssignment(this.allowTrainerWorkoutAssignment);
        dto.setWorkout(wko);

        // Nutrition
        SettingsDTO.NutritionDTO nut = new SettingsDTO.NutritionDTO();
        nut.setNutritionPlansEnabled(this.nutritionPlansEnabled);
        nut.setMealTrackingEnabled(this.mealTrackingEnabled);
        nut.setAllowTrainerNutritionAssignment(this.allowTrainerNutritionAssignment);
        dto.setNutrition(nut);

        // Payment
        SettingsDTO.PaymentDTO pay = new SettingsDTO.PaymentDTO();
        pay.setDefaultPaymentMethod(this.defaultPaymentMethod);
        pay.setPaymentMethods(this.paymentMethods);
        pay.setPaymentReceiptEnabled(this.paymentReceiptEnabled);
        pay.setTaxEnabled(this.taxEnabled);
        pay.setTaxRate(this.taxRate);
        dto.setPayment(pay);

        // Invoice
        SettingsDTO.InvoiceDTO inv = new SettingsDTO.InvoiceDTO();
        inv.setInvoicePrefix(this.invoicePrefix);
        inv.setAutoGenerateInvoiceNumber(this.autoGenerateInvoiceNumber);
        inv.setInvoiceTerms(this.invoiceTerms);
        inv.setInvoiceNotes(this.invoiceNotes);
        dto.setInvoice(inv);

        // Notification
        SettingsDTO.NotificationDTO notif = new SettingsDTO.NotificationDTO();
        notif.setEmailNotificationsEnabled(this.emailNotificationsEnabled);
        notif.setSmsNotificationsEnabled(this.smsNotificationsEnabled);
        notif.setMembershipExpiryNotification(this.membershipExpiryNotification);
        notif.setPaymentNotification(this.paymentNotification);
        notif.setAttendanceNotification(this.attendanceNotification);
        dto.setNotification(notif);

        // Security
        SettingsDTO.SecurityDTO sec = new SettingsDTO.SecurityDTO();
        sec.setSessionTimeoutMinutes(this.sessionTimeoutMinutes);
        sec.setAllowMultipleSessions(this.allowMultipleSessions);
        dto.setSecurity(sec);

        // Appearance
        SettingsDTO.AppearanceDTO app = new SettingsDTO.AppearanceDTO();
        app.setTheme(this.theme);
        dto.setAppearance(app);

        return dto;
    }

    public void updateFromDto(SettingsDTO dto) {
        if (dto == null) {
            return;
        }

        // Gym Profile
        if (dto.getGymProfile() != null) {
            SettingsDTO.GymProfileDTO profile = dto.getGymProfile();
            if (profile.getGymName() != null) this.gymName = profile.getGymName();
            if (profile.getEmail() != null) this.email = profile.getEmail();
            if (profile.getPhone() != null) this.phone = profile.getPhone();
            if (profile.getAddress() != null) this.address = profile.getAddress();
            if (profile.getCity() != null) this.city = profile.getCity();
            if (profile.getState() != null) this.state = profile.getState();
            if (profile.getPincode() != null) this.pincode = profile.getPincode();
            if (profile.getCountry() != null) this.country = profile.getCountry();
            if (profile.getWebsite() != null) this.website = profile.getWebsite();
            if (profile.getLogo() != null) this.logo = profile.getLogo();
        }

        // General
        if (dto.getGeneral() != null) {
            SettingsDTO.GeneralDTO gen = dto.getGeneral();
            if (gen.getCurrency() != null) this.currency = gen.getCurrency();
            if (gen.getTimezone() != null) this.timezone = gen.getTimezone();
            if (gen.getDateFormat() != null) this.dateFormat = gen.getDateFormat();
            if (gen.getLanguage() != null) this.language = gen.getLanguage();
        }

        // Membership
        if (dto.getMembership() != null) {
            SettingsDTO.MembershipDTO mem = dto.getMembership();
            if (mem.getDefaultMembershipDurationDays() != null) this.defaultMembershipDurationDays = mem.getDefaultMembershipDurationDays();
            if (mem.getMembershipExpiryReminderDays() != null) this.membershipExpiryReminderDays = mem.getMembershipExpiryReminderDays();
            if (mem.getAutoRenewalEnabled() != null) this.autoRenewalEnabled = mem.getAutoRenewalEnabled();
            if (mem.getAllowFreeze() != null) this.allowFreeze = mem.getAllowFreeze();
            if (mem.getMaxFreezeDays() != null) this.maxFreezeDays = mem.getMaxFreezeDays();
        }

        // Attendance
        if (dto.getAttendance() != null) {
            SettingsDTO.AttendanceDTO att = dto.getAttendance();
            if (att.getAttendanceEnabled() != null) this.attendanceEnabled = att.getAttendanceEnabled();
            if (att.getCheckInEnabled() != null) this.checkInEnabled = att.getCheckInEnabled();
            if (att.getCheckOutEnabled() != null) this.checkOutEnabled = att.getCheckOutEnabled();
            if (att.getLateEntryAllowed() != null) this.lateEntryAllowed = att.getLateEntryAllowed();
            if (att.getLateEntryMinutes() != null) this.lateEntryMinutes = att.getLateEntryMinutes();
            if (att.getAllowMultipleCheckInsPerDay() != null) this.allowMultipleCheckInsPerDay = att.getAllowMultipleCheckInsPerDay();
        }

        // Trainer
        if (dto.getTrainer() != null) {
            SettingsDTO.TrainerDTO trn = dto.getTrainer();
            if (trn.getTrainerCommissionEnabled() != null) this.trainerCommissionEnabled = trn.getTrainerCommissionEnabled();
            if (trn.getDefaultCommissionPercentage() != null) this.defaultCommissionPercentage = trn.getDefaultCommissionPercentage();
            if (trn.getTrainerAttendanceRequired() != null) this.trainerAttendanceRequired = trn.getTrainerAttendanceRequired();
        }

        // Workout
        if (dto.getWorkout() != null) {
            SettingsDTO.WorkoutDTO wko = dto.getWorkout();
            if (wko.getWorkoutPlansEnabled() != null) this.workoutPlansEnabled = wko.getWorkoutPlansEnabled();
            if (wko.getExerciseTrackingEnabled() != null) this.exerciseTrackingEnabled = wko.getExerciseTrackingEnabled();
            if (wko.getAllowTrainerWorkoutAssignment() != null) this.allowTrainerWorkoutAssignment = wko.getAllowTrainerWorkoutAssignment();
        }

        // Nutrition
        if (dto.getNutrition() != null) {
            SettingsDTO.NutritionDTO nut = dto.getNutrition();
            if (nut.getNutritionPlansEnabled() != null) this.nutritionPlansEnabled = nut.getNutritionPlansEnabled();
            if (nut.getMealTrackingEnabled() != null) this.mealTrackingEnabled = nut.getMealTrackingEnabled();
            if (nut.getAllowTrainerNutritionAssignment() != null) this.allowTrainerNutritionAssignment = nut.getAllowTrainerNutritionAssignment();
        }

        // Payment
        if (dto.getPayment() != null) {
            SettingsDTO.PaymentDTO pay = dto.getPayment();
            if (pay.getDefaultPaymentMethod() != null) this.defaultPaymentMethod = pay.getDefaultPaymentMethod();
            if (pay.getPaymentMethods() != null) this.paymentMethods = pay.getPaymentMethods();
            if (pay.getPaymentReceiptEnabled() != null) this.paymentReceiptEnabled = pay.getPaymentReceiptEnabled();
            if (pay.getTaxEnabled() != null) this.taxEnabled = pay.getTaxEnabled();
            if (pay.getTaxRate() != null) this.taxRate = pay.getTaxRate();
        }

        // Invoice
        if (dto.getInvoice() != null) {
            SettingsDTO.InvoiceDTO inv = dto.getInvoice();
            if (inv.getInvoicePrefix() != null) this.invoicePrefix = inv.getInvoicePrefix();
            if (inv.getAutoGenerateInvoiceNumber() != null) this.autoGenerateInvoiceNumber = inv.getAutoGenerateInvoiceNumber();
            if (inv.getInvoiceTerms() != null) this.invoiceTerms = inv.getInvoiceTerms();
            if (inv.getInvoiceNotes() != null) this.invoiceNotes = inv.getInvoiceNotes();
        }

        // Notification
        if (dto.getNotification() != null) {
            SettingsDTO.NotificationDTO notif = dto.getNotification();
            if (notif.getEmailNotificationsEnabled() != null) this.emailNotificationsEnabled = notif.getEmailNotificationsEnabled();
            if (notif.getSmsNotificationsEnabled() != null) this.smsNotificationsEnabled = notif.getSmsNotificationsEnabled();
            if (notif.getMembershipExpiryNotification() != null) this.membershipExpiryNotification = notif.getMembershipExpiryNotification();
            if (notif.getPaymentNotification() != null) this.paymentNotification = notif.getPaymentNotification();
            if (notif.getAttendanceNotification() != null) this.attendanceNotification = notif.getAttendanceNotification();
        }

        // Security
        if (dto.getSecurity() != null) {
            SettingsDTO.SecurityDTO sec = dto.getSecurity();
            if (sec.getSessionTimeoutMinutes() != null) this.sessionTimeoutMinutes = sec.getSessionTimeoutMinutes();
            if (sec.getAllowMultipleSessions() != null) this.allowMultipleSessions = sec.getAllowMultipleSessions();
        }

        // Appearance
        if (dto.getAppearance() != null) {
            SettingsDTO.AppearanceDTO app = dto.getAppearance();
            if (app.getTheme() != null) this.theme = app.getTheme();
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
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
