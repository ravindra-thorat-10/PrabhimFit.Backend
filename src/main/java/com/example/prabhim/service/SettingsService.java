package com.example.prabhim.service;

import java.util.UUID;

import com.example.prabhim.dto.settings.SettingsDTO;
import com.example.prabhim.dto.settings.SettingsSaveResponse;

public interface SettingsService {

    SettingsDTO getSettings(UUID userId);

    SettingsSaveResponse updateSettings(UUID userId, SettingsDTO request);
}
