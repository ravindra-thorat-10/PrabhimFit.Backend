package com.example.prabhim.service.impl;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.prabhim.dto.settings.SettingsDTO;
import com.example.prabhim.dto.settings.SettingsSaveResponse;
import com.example.prabhim.entity.GymSettings;
import com.example.prabhim.entity.User;
import com.example.prabhim.exception.UserNotFoundException;
import com.example.prabhim.repository.SettingsRepository;
import com.example.prabhim.repository.UserRepository;
import com.example.prabhim.service.SettingsService;

@Service
@Transactional
public class SettingsServiceImpl implements SettingsService {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[+0-9()\\s-]{7,25}$");

    private final SettingsRepository settingsRepository;
    private final UserRepository userRepository;

    public SettingsServiceImpl(SettingsRepository settingsRepository, UserRepository userRepository) {
        this.settingsRepository = settingsRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public SettingsDTO getSettings(UUID userId) {
        if (userId == null) {
            throw new UserNotFoundException("Authenticated user ID cannot be null");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        GymSettings settings = settingsRepository.findByUserId(userId)
                .orElseGet(() -> {
                    GymSettings defaultSettings = GymSettings.createDefault(user);
                    return settingsRepository.save(defaultSettings);
                });

        return settings.toDto();
    }

    @Override
    @Transactional
    public SettingsSaveResponse updateSettings(UUID userId, SettingsDTO request) {
        if (userId == null) {
            throw new UserNotFoundException("Authenticated user ID cannot be null");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        validateSettings(request);

        GymSettings settings = settingsRepository.findByUserId(userId)
                .orElseGet(() -> GymSettings.createDefault(user));

        settings.setUser(user);
        settings.updateFromDto(request);

        if (request != null) {
            if (request.getGeneral() != null && request.getGeneral().getTimezone() != null) {
                user.setTimeZone(request.getGeneral().getTimezone());
            }
            if (request.getAppearance() != null && request.getAppearance().getTheme() != null) {
                user.setTheme(request.getAppearance().getTheme());
            }
            userRepository.save(user);
        }

        settings = settingsRepository.save(settings);

        return new SettingsSaveResponse("Settings saved successfully.", settings.getUpdatedAt(), settings.toDto());
    }

    private void validateSettings(SettingsDTO request) {
        if (request == null) {
            return;
        }

        // Gym Profile validation
        if (request.getGymProfile() != null) {
            String email = request.getGymProfile().getEmail();
            if (email != null && !email.trim().isEmpty() && !EMAIL_PATTERN.matcher(email.trim()).matches()) {
                throw new IllegalArgumentException("Please provide a valid gym email address");
            }

            String phone = request.getGymProfile().getPhone();
            if (phone != null && !phone.trim().isEmpty() && !PHONE_PATTERN.matcher(phone.trim()).matches()) {
                throw new IllegalArgumentException("Please provide a valid phone number");
            }
        }

        // Membership validation
        if (request.getMembership() != null) {
            Integer duration = request.getMembership().getDefaultMembershipDurationDays();
            if (duration != null && duration <= 0) {
                throw new IllegalArgumentException("Default membership duration must be greater than 0 days");
            }

            Integer reminder = request.getMembership().getMembershipExpiryReminderDays();
            if (reminder != null && reminder < 0) {
                throw new IllegalArgumentException("Membership expiry reminder days cannot be negative");
            }

            Integer maxFreeze = request.getMembership().getMaxFreezeDays();
            if (maxFreeze != null && maxFreeze < 0) {
                throw new IllegalArgumentException("Max freeze days cannot be negative");
            }
        }

        // Attendance validation
        if (request.getAttendance() != null) {
            Integer lateEntryMinutes = request.getAttendance().getLateEntryMinutes();
            if (lateEntryMinutes != null && lateEntryMinutes < 0) {
                throw new IllegalArgumentException("Late entry minutes cannot be negative");
            }
        }

        // Trainer validation
        if (request.getTrainer() != null) {
            Integer commission = request.getTrainer().getDefaultCommissionPercentage();
            if (commission != null && (commission < 0 || commission > 100)) {
                throw new IllegalArgumentException("Trainer commission percentage must be between 0 and 100");
            }
        }

        // Payment validation
        if (request.getPayment() != null) {
            BigDecimal taxRate = request.getPayment().getTaxRate();
            if (taxRate != null && (taxRate.compareTo(BigDecimal.ZERO) < 0 || taxRate.compareTo(new BigDecimal("100")) > 0)) {
                throw new IllegalArgumentException("Tax rate must be between 0% and 100%");
            }
        }

        // Security validation
        if (request.getSecurity() != null) {
            Integer timeout = request.getSecurity().getSessionTimeoutMinutes();
            if (timeout != null && timeout <= 0) {
                throw new IllegalArgumentException("Session timeout must be greater than 0 minutes");
            }
        }
    }
}
