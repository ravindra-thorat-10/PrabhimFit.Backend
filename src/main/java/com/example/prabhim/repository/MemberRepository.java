package com.example.prabhim.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.prabhim.entity.Member;
import com.example.prabhim.entity.enums.MemberStatus;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID>, JpaSpecificationExecutor<Member> {

    Optional<Member> findByMemberCode(String memberCode);

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByMemberCode(String memberCode);

    @Query("SELECT m.memberCode FROM Member m WHERE m.memberCode LIKE 'PF-%' OR m.memberCode LIKE '#PF-%' OR m.memberCode LIKE 'FC-%' OR m.memberCode LIKE 'GM-%' OR m.memberCode LIKE '#GM-%' ORDER BY m.createdAt DESC LIMIT 1")
    Optional<String> findLatestMemberCode();

    long count();

    long countByStatus(MemberStatus status);

    @Query("SELECT COUNT(m) FROM Member m WHERE m.trainer.id = :trainerId")
    long countByTrainerId(@org.springframework.data.repository.query.Param("trainerId") UUID trainerId);
}
