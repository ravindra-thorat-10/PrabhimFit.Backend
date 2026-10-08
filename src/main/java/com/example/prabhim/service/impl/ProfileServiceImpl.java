package com.example.prabhim.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.prabhim.dto.profile.AvatarUploadRequest;
import com.example.prabhim.dto.profile.ChangePasswordRequest;
import com.example.prabhim.dto.profile.ProfilePatchRequest;
import com.example.prabhim.dto.profile.ProfileResponse;
import com.example.prabhim.dto.profile.ProfileUpdateRequest;
import com.example.prabhim.entity.User;
import com.example.prabhim.exception.UserNotFoundException;
import com.example.prabhim.repository.UserRepository;
import com.example.prabhim.service.ProfileService;

@Service
@Transactional
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfileServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private User resolveUser(UUID userId) {
        if (userId != null) {
            return userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        }
        // Fallback to first available admin or user
        return userRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("No user profile found in the system."));
    }

    @Override
    @Transactional(readOnly = true)
    public ProfileResponse getProfile(UUID userId) {
        User user = resolveUser(userId);
        return ProfileResponse.fromEntity(user);
    }

    @Override
    public ProfileResponse updateProfile(UUID userId, ProfileUpdateRequest request) {
        User user = resolveUser(userId);

        // Section 1: Personal Info
        if (request.getFullName() != null && !request.getFullName().trim().isEmpty()) {
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
        } else {
            if (request.getFirstName() != null) user.setFirstName(request.getFirstName().trim());
            if (request.getLastName() != null) user.setLastName(request.getLastName().trim());
        }

        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            user.setEmail(request.getEmail().trim().toLowerCase());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone().trim());
        }
        if (request.getDesignation() != null) {
            user.setDesignation(request.getDesignation().trim());
        }
        if (request.getProfileImage() != null) {
            user.setProfileImage(request.getProfileImage().trim());
            user.setAvatar(request.getProfileImage().trim());
            user.setProfilePic(request.getProfileImage().trim());
        }

        // Section 2: Residential & Postal
        if (request.getResidentialAddress() != null) {
            user.setResidentialAddress(request.getResidentialAddress().trim());
        }
        if (request.getCity() != null) {
            user.setCity(request.getCity().trim());
        }
        if (request.getState() != null) {
            user.setState(request.getState().trim());
        }
        if (request.getPincode() != null) {
            user.setPincode(request.getPincode().trim());
        }

        // Section 3: Emergency & Secondary Contact
        if (request.getSecondaryContactName() != null) {
            user.setSecondaryContactName(request.getSecondaryContactName().trim());
        }
        if (request.getSecondaryContactPhone() != null) {
            user.setSecondaryContactPhone(request.getSecondaryContactPhone().trim());
        }
        if (request.getSecondaryContactRole() != null) {
            user.setSecondaryContactRole(request.getSecondaryContactRole().trim());
        }

        // Section 4: Bio
        if (request.getBio() != null) {
            user.setBio(request.getBio().trim());
        }

        // Facility & System Settings
        if (request.getFacilityName() != null) {
            user.setFacilityName(request.getFacilityName().trim());
        }
        if (request.getTimeZone() != null) {
            user.setTimeZone(request.getTimeZone().trim());
        }
        if (request.getTheme() != null) {
            user.setTheme(request.getTheme().trim());
        }

        User updated = userRepository.save(user);
        return ProfileResponse.fromEntity(updated);
    }

    @Override
    public ProfileResponse patchProfile(UUID userId, ProfilePatchRequest request) {
        User user = resolveUser(userId);

        // Section 1
        if (request.getFullName() != null && !request.getFullName().trim().isEmpty()) {
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
        } else {
            if (request.getFirstName() != null) user.setFirstName(request.getFirstName().trim());
            if (request.getLastName() != null) user.setLastName(request.getLastName().trim());
        }

        if (request.getEmail() != null) {
            user.setEmail(request.getEmail().trim().toLowerCase());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone().trim());
        }
        if (request.getDesignation() != null) {
            user.setDesignation(request.getDesignation().trim());
        }
        if (request.getProfileImage() != null) {
            user.setProfileImage(request.getProfileImage().trim());
            user.setAvatar(request.getProfileImage().trim());
            user.setProfilePic(request.getProfileImage().trim());
        }

        // Section 2
        if (request.getResidentialAddress() != null) {
            user.setResidentialAddress(request.getResidentialAddress().trim());
        }
        if (request.getCity() != null) {
            user.setCity(request.getCity().trim());
        }
        if (request.getState() != null) {
            user.setState(request.getState().trim());
        }
        if (request.getPincode() != null) {
            user.setPincode(request.getPincode().trim());
        }

        // Section 3
        if (request.getSecondaryContactName() != null) {
            user.setSecondaryContactName(request.getSecondaryContactName().trim());
        }
        if (request.getSecondaryContactPhone() != null) {
            user.setSecondaryContactPhone(request.getSecondaryContactPhone().trim());
        }
        if (request.getSecondaryContactRole() != null) {
            user.setSecondaryContactRole(request.getSecondaryContactRole().trim());
        }

        // Section 4
        if (request.getBio() != null) {
            user.setBio(request.getBio().trim());
        }

        // Facility & System
        if (request.getFacilityName() != null) {
            user.setFacilityName(request.getFacilityName().trim());
        }
        if (request.getTimeZone() != null) {
            user.setTimeZone(request.getTimeZone().trim());
        }
        if (request.getTheme() != null) {
            user.setTheme(request.getTheme().trim());
        }

        User updated = userRepository.save(user);
        return ProfileResponse.fromEntity(updated);
    }

    @Override
    public ProfileResponse updateAvatar(UUID userId, AvatarUploadRequest request) {
        User user = resolveUser(userId);
        user.setProfileImage(request.getProfileImage().trim());
        user.setAvatar(request.getProfileImage().trim());
        user.setProfilePic(request.getProfileImage().trim());
        User updated = userRepository.save(user);
        return ProfileResponse.fromEntity(updated);
    }

    @Override
    public void changePassword(UUID userId, ChangePasswordRequest request) {
        User user = resolveUser(userId);

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password does not match.");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("New password and confirm password do not match.");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public List<String> getAvatarPresets() {
        return Arrays.asList(
                "/avatars/admin-preset-1.png",
                "/avatars/admin-preset-2.png",
                "/avatars/admin-preset-3.png",
                "/avatars/admin-preset-4.png",
                "/avatars/admin-preset-5.png",
                "/avatars/admin-preset-6.png"
        );
    }
}
