package com.example.prabhim.service;

import java.util.List;
import java.util.UUID;

import com.example.prabhim.dto.profile.AvatarUploadRequest;
import com.example.prabhim.dto.profile.ChangePasswordRequest;
import com.example.prabhim.dto.profile.ProfilePatchRequest;
import com.example.prabhim.dto.profile.ProfileResponse;
import com.example.prabhim.dto.profile.ProfileUpdateRequest;

public interface ProfileService {

    ProfileResponse getProfile(UUID userId);

    ProfileResponse updateProfile(UUID userId, ProfileUpdateRequest request);

    ProfileResponse patchProfile(UUID userId, ProfilePatchRequest request);

    ProfileResponse updateAvatar(UUID userId, AvatarUploadRequest request);

    void changePassword(UUID userId, ChangePasswordRequest request);

    List<String> getAvatarPresets();
}
