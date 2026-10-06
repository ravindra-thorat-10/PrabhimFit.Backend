package com.example.prabhim.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.prabhim.dto.ApiResponse;
import com.example.prabhim.dto.settings.SettingsDTO;
import com.example.prabhim.dto.settings.SettingsSaveResponse;
import com.example.prabhim.service.SettingsService;

@RestController
@CrossOrigin(origins = "*")
public class SettingsController {

    private final SettingsService settingsService;

    public SettingsController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @GetMapping({ "/api/v1/settings", "/api/v1/settings/", "/api/settings", "/api/settings/" })
    public ResponseEntity<ApiResponse<SettingsDTO>> getSettings(@AuthenticationPrincipal UUID actorUserId) {
        SettingsDTO settings = settingsService.getSettings(actorUserId);
        return ResponseEntity.ok(ApiResponse.success("Settings retrieved successfully.", settings));
    }

    @PutMapping({ "/api/v1/settings", "/api/v1/settings/", "/api/settings", "/api/settings/" })
    public ResponseEntity<ApiResponse<SettingsSaveResponse>> updateSettings(
            @RequestBody SettingsDTO request,
            @AuthenticationPrincipal UUID actorUserId) {
        SettingsSaveResponse response = settingsService.updateSettings(actorUserId, request);
        return ResponseEntity.ok(ApiResponse.success("Settings saved successfully.", response));
    }
}
