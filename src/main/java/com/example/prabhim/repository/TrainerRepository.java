package com.example.prabhim.repository;

import java.math.BigDecimal;
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

import com.example.prabhim.entity.Trainer;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, UUID>, JpaSpecificationExecutor<Trainer> {

    Optional<Trainer> findByEmail(String email);

    Optional<Trainer> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    Optional<Trainer> findByTrainerCode(String trainerCode);

    Optional<Trainer> findTopByOrderByTrainerCodeDesc();

    List<Trainer> findByStatusOrderByFullNameAsc(String status);

    @Query("SELECT t FROM Trainer t WHERE " +
            "(:search IS NULL OR :search = '' OR " +
            "LOWER(t.fullName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(t.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(t.trainerCode) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(t.specialization) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "t.phone LIKE CONCAT('%', :search, '%')) AND " +
            "(:status IS NULL OR :status = '' OR UPPER(t.status) = UPPER(:status)) AND " +
            "(:specialization IS NULL OR :specialization = '' OR LOWER(t.specialization) LIKE LOWER(CONCAT('%', :specialization, '%')))")
    Page<Trainer> searchTrainers(
            @Param("search") String search,
            @Param("status") String status,
            @Param("specialization") String specialization,
            Pageable pageable
    );

    long countByStatusIgnoreCase(String status);

    @Query("SELECT COALESCE(SUM(t.monthlySalary), 0) FROM Trainer t WHERE UPPER(t.status) = 'ACTIVE'")
    BigDecimal sumActiveMonthlySalary();

    @Query("SELECT COALESCE(SUM(t.monthlySalary), 0) FROM Trainer t")
    BigDecimal sumTotalMonthlyPayroll();
}
