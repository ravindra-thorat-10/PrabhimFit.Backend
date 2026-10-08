package com.example.prabhim.controller;

import java.util.List;
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
import com.example.prabhim.dto.profile.AvatarUploadRequest;
import com.example.prabhim.dto.profile.ChangePasswordRequest;
import com.example.prabhim.dto.profile.ProfilePatchRequest;
import com.example.prabhim.dto.profile.ProfileResponse;
import com.example.prabhim.dto.profile.ProfileUpdateRequest;
import com.example.prabhim.service.ProfileService;

import jakarta.validation.Valid;

@RestController
@RequestMapping({"/api/v1/profile", "/api/v1/users/profile", "/api/profile"})
@CrossOrigin(origins = "*")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    // 1. GET - Fetch Administrator Profile
    @GetMapping({"", "/"})
    public ResponseEntity<ApiResponse<ProfileResponse>> getProfile(
            @AuthenticationPrincipal UUID actorUserId) {
        ProfileResponse response = profileService.getProfile(actorUserId);
        return ResponseEntity.ok(ApiResponse.success("Profile details retrieved successfully.", response));
    }

    // 2. PUT - Update Full Administrator Profile Details
    @PutMapping({"", "/"})
    public ResponseEntity<ApiResponse<ProfileResponse>> updateProfile(
            @Valid @RequestBody ProfileUpdateRequest request,
            @AuthenticationPrincipal UUID actorUserId) {
        ProfileResponse response = profileService.updateProfile(actorUserId, request);
        return ResponseEntity.ok(ApiResponse.success("Admin profile updated successfully.", response));
    }

    // 3. PATCH - Partial Update of Administrator Profile Details
    @PatchMapping({"", "/"})
    public ResponseEntity<ApiResponse<ProfileResponse>> patchProfile(
            @RequestBody ProfilePatchRequest request,
            @AuthenticationPrincipal UUID actorUserId) {
        ProfileResponse response = profileService.patchProfile(actorUserId, request);
        return ResponseEntity.ok(ApiResponse.success("Admin profile updated successfully.", response));
    }

    // 4. POST - Upload / Pick Preset Avatar Photo
    @PostMapping({"/avatar", "/avatar/"})
    public ResponseEntity<ApiResponse<ProfileResponse>> updateAvatar(
            @Valid @RequestBody AvatarUploadRequest request,
            @AuthenticationPrincipal UUID actorUserId) {
        ProfileResponse response = profileService.updateAvatar(actorUserId, request);
        return ResponseEntity.ok(ApiResponse.success("Profile avatar updated successfully.", response));
    }

    // 5. POST - Change Password (Security Tab)
    @PostMapping({"/change-password", "/change-password/"})
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            @AuthenticationPrincipal UUID actorUserId) {
        profileService.changePassword(actorUserId, request);
        return ResponseEntity.ok(ApiResponse.success("Password changed successfully."));
    }

    // 6. GET - Get Available Avatar Presets
    @GetMapping({"/presets", "/presets/"})
    public ResponseEntity<ApiResponse<List<String>>> getAvatarPresets() {
        List<String> presets = profileService.getAvatarPresets();
        return ResponseEntity.ok(ApiResponse.success("Avatar presets retrieved successfully.", presets));
    }
}
