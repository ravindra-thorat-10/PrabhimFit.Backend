package com.example.prabhim.service;

import java.util.UUID;

import com.example.prabhim.dto.profile.ChangePasswordRequest;
import com.example.prabhim.dto.profile.ProfileResponse;
import com.example.prabhim.dto.profile.ProfileUpdateRequest;

import jakarta.servlet.http.HttpServletRequest;

public interface ProfileService {

    ProfileResponse getProfile(UUID userId, HttpServletRequest httpRequest);

    ProfileResponse updateProfile(UUID userId, ProfileUpdateRequest request, HttpServletRequest httpRequest);

    void changePassword(UUID userId, ChangePasswordRequest request, HttpServletRequest httpRequest);

    ProfileResponse updateAvatar(UUID userId, String avatar, HttpServletRequest httpRequest);

    ProfileResponse updateGymLogo(UUID userId, String gymLogo, HttpServletRequest httpRequest);
}
