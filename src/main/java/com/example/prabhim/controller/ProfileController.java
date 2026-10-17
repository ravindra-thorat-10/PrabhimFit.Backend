package com.example.prabhim.controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.prabhim.dto.ApiResponse;
import com.example.prabhim.dto.profile.ChangePasswordRequest;
import com.example.prabhim.dto.profile.ProfileResponse;
import com.example.prabhim.dto.profile.ProfileUpdateRequest;
import com.example.prabhim.service.ProfileService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping({"/api/v1/profile", "/api/profile"})
@CrossOrigin(origins = "*")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping({"", "/", "/me", "/me/"})
    public ResponseEntity<ApiResponse<ProfileResponse>> getProfile(
            @AuthenticationPrincipal UUID userId,
            HttpServletRequest request) {
        ProfileResponse response = profileService.getProfile(userId, request);
        return ResponseEntity.ok(ApiResponse.success("Profile retrieved successfully.", response));
    }

    @PutMapping({"", "/"})
    public ResponseEntity<ApiResponse<ProfileResponse>> updateProfile(
            @AuthenticationPrincipal UUID userId,
            @RequestBody ProfileUpdateRequest requestDto,
            HttpServletRequest request) {
        ProfileResponse response = profileService.updateProfile(userId, requestDto, request);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully.", response));
    }

    @PatchMapping({"", "/"})
    public ResponseEntity<ApiResponse<ProfileResponse>> patchProfile(
            @AuthenticationPrincipal UUID userId,
            @RequestBody ProfileUpdateRequest requestDto,
            HttpServletRequest request) {
        ProfileResponse response = profileService.updateProfile(userId, requestDto, request);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully.", response));
    }

    @PutMapping({"/change-password", "/change-password/"})
    public ResponseEntity<ApiResponse<Void>> changePasswordPut(
            @AuthenticationPrincipal UUID userId,
            @Valid @RequestBody ChangePasswordRequest requestDto,
            HttpServletRequest request) {
        profileService.changePassword(userId, requestDto, request);
        return ResponseEntity.ok(ApiResponse.success("Password changed successfully."));
    }

    @PostMapping({"/change-password", "/change-password/"})
    public ResponseEntity<ApiResponse<Void>> changePasswordPost(
            @AuthenticationPrincipal UUID userId,
            @Valid @RequestBody ChangePasswordRequest requestDto,
            HttpServletRequest request) {
        profileService.changePassword(userId, requestDto, request);
        return ResponseEntity.ok(ApiResponse.success("Password changed successfully."));
    }

    @PostMapping({"/avatar", "/avatar/"})
    public ResponseEntity<ApiResponse<ProfileResponse>> updateAvatar(
            @AuthenticationPrincipal UUID userId,
            @RequestBody Map<String, String> payload,
            HttpServletRequest request) {
        String avatar = payload.getOrDefault("avatar", payload.getOrDefault("profileImage", payload.get("photo")));
        ProfileResponse response = profileService.updateAvatar(userId, avatar, request);
        return ResponseEntity.ok(ApiResponse.success("Profile avatar updated successfully.", response));
    }

    @PostMapping({"/logo", "/logo/"})
    public ResponseEntity<ApiResponse<ProfileResponse>> updateLogo(
            @AuthenticationPrincipal UUID userId,
            @RequestBody Map<String, String> payload,
            HttpServletRequest request) {
        String gymLogo = payload.getOrDefault("gymLogo", payload.getOrDefault("logo", payload.get("brandLogo")));
        ProfileResponse response = profileService.updateGymLogo(userId, gymLogo, request);
        return ResponseEntity.ok(ApiResponse.success("Gym brand logo updated successfully.", response));
    }
}
