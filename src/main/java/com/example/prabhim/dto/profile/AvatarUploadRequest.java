package com.example.prabhim.dto.profile;

import com.fasterxml.jackson.annotation.JsonAlias;

import jakarta.validation.constraints.NotBlank;

public class AvatarUploadRequest {

    @NotBlank(message = "Avatar image URL, base64, or preset key is required")
    @JsonAlias({"avatar", "profilePic", "photoUrl", "preset", "imageUrl"})
    private String profileImage;

    public AvatarUploadRequest() {
    }

    public AvatarUploadRequest(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
