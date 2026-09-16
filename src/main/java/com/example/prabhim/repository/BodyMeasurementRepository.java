package com.example.prabhim.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.prabhim.entity.BodyMeasurement;

@Repository
public interface BodyMeasurementRepository extends JpaRepository<BodyMeasurement, UUID> {

    List<BodyMeasurement> findByMemberIdOrderByRecordedDateDesc(UUID memberId);

    Optional<BodyMeasurement> findFirstByMemberIdOrderByRecordedDateDesc(UUID memberId);

    void deleteByMemberId(UUID memberId);
}
