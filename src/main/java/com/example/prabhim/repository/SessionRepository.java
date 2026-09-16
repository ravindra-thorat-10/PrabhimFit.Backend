package com.example.prabhim.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.prabhim.entity.Session;

@Repository
public interface SessionRepository extends JpaRepository<Session, UUID> {

    List<Session> findByUserId(UUID userId);

    Optional<Session> findByRefreshToken(String refreshToken);

    void deleteByRefreshToken(String refreshToken);

    void deleteByUserId(UUID userId);
}
