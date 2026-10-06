package com.example.prabhim.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.prabhim.entity.GymSettings;
import com.example.prabhim.entity.User;

@Repository
public interface SettingsRepository extends JpaRepository<GymSettings, UUID> {

    Optional<GymSettings> findByUserId(UUID userId);

    Optional<GymSettings> findByUser(User user);

    boolean existsByUserId(UUID userId);
}
