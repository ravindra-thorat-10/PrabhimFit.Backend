package com.example.prabhim.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.prabhim.entity.Attendance;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, UUID>, JpaSpecificationExecutor<Attendance> {

    List<Attendance> findByMemberIdOrderByCheckInTimeDesc(UUID memberId);

    List<Attendance> findTop10ByMemberIdOrderByCheckInTimeDesc(UUID memberId);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.member.id = :memberId")
    long countTotalVisitsByMemberId(@Param("memberId") UUID memberId);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.member.id = :memberId AND a.checkInTime >= :startOfMonth")
    long countVisitsThisMonthByMemberId(@Param("memberId") UUID memberId,
            @Param("startOfMonth") LocalDateTime startOfMonth);

    @Query("SELECT a FROM Attendance a WHERE a.member.id = :memberId ORDER BY a.checkInTime DESC LIMIT 1")
    Optional<Attendance> findLatestByMemberId(@Param("memberId") UUID memberId);

    @Query("SELECT a FROM Attendance a WHERE " +
            "a.checkInTime BETWEEN :startOfDay AND :endOfDay AND " +
            "(:search IS NULL OR :search = '' OR " +
            "LOWER(CONCAT(a.member.firstName, ' ', a.member.lastName)) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(a.member.memberCode) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "a.member.phone LIKE CONCAT('%', :search, '%')) AND " +
            "(:status IS NULL OR :status = '' OR UPPER(:status) = 'ALL' OR UPPER(a.status) = UPPER(:status)) " +
            "ORDER BY a.checkInTime DESC")
    Page<Attendance> findByFilters(
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay,
            @Param("search") String search,
            @Param("status") String status,
            Pageable pageable
    );

    @Query("SELECT a FROM Attendance a WHERE " +
            "a.checkInTime BETWEEN :startOfDay AND :endOfDay " +
            "ORDER BY a.checkInTime DESC")
    List<Attendance> findAllForExport(
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.checkInTime BETWEEN :startOfDay AND :endOfDay AND UPPER(a.status) = 'PRESENT'")
    long countOnTimeCheckInsBetween(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.checkInTime BETWEEN :startOfDay AND :endOfDay AND UPPER(a.status) = 'LATE'")
    long countLateArrivalsBetween(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.checkInTime BETWEEN :startOfDay AND :endOfDay")
    long countTotalCheckInsBetween(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    void deleteByMemberId(UUID memberId);
}
