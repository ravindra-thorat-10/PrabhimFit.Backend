package com.example.prabhim.specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.example.prabhim.entity.Member;
import com.example.prabhim.entity.Membership;
import com.example.prabhim.entity.enums.Gender;
import com.example.prabhim.entity.enums.MemberStatus;
import com.example.prabhim.entity.enums.MembershipStatus;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

public class MemberSpecification {

    public static Specification<Member> filter(
            String search,
            MemberStatus status,
            UUID trainerId,
            String membershipPlan,
            MembershipStatus membershipStatus,
            LocalDate startDate,
            LocalDate endDate,
            Gender gender) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Search across memberCode, firstName, lastName, email, phone, and fullName
            if (search != null && !search.trim().isEmpty()) {
                String searchPattern = "%" + search.trim().toLowerCase() + "%";
                Expression<String> fullNameExp = cb.concat(
                        cb.concat(cb.lower(root.get("firstName")), " "),
                        cb.lower(cb.coalesce(root.get("lastName"), ""))
                );

                Predicate codeMatch = cb.like(cb.lower(root.get("memberCode")), searchPattern);
                Predicate firstNameMatch = cb.like(cb.lower(root.get("firstName")), searchPattern);
                Predicate lastNameMatch = cb.like(cb.lower(cb.coalesce(root.get("lastName"), "")), searchPattern);
                Predicate emailMatch = cb.like(cb.lower(root.get("email")), searchPattern);
                Predicate phoneMatch = cb.like(cb.coalesce(root.get("phone"), ""), searchPattern);
                Predicate fullNameMatch = cb.like(fullNameExp, searchPattern);

                predicates.add(cb.or(codeMatch, firstNameMatch, lastNameMatch, emailMatch, phoneMatch, fullNameMatch));
            }

            // 2. Status filtering
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            // 3. Trainer filtering
            if (trainerId != null) {
                predicates.add(cb.equal(root.get("trainer").get("id"), trainerId));
            }

            // 4. Gender filtering
            if (gender != null) {
                predicates.add(cb.equal(root.get("gender"), gender));
            }

            // 5. Date filtering (joinDate range)
            if (startDate != null && endDate != null) {
                predicates.add(cb.between(root.get("joinDate"), startDate, endDate));
            } else if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("joinDate"), startDate));
            } else if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("joinDate"), endDate));
            }

            // 6. Membership plan filtering via Subquery
            if (membershipPlan != null && !membershipPlan.trim().isEmpty()) {
                Subquery<UUID> subquery = query.subquery(UUID.class);
                Root<Membership> membershipRoot = subquery.from(Membership.class);
                String planPattern = "%" + membershipPlan.trim().toLowerCase() + "%";

                Predicate planMatch = cb.or(
                        cb.like(cb.lower(membershipRoot.get("planName")), planPattern),
                        cb.like(cb.lower(cb.coalesce(membershipRoot.get("planType"), "")), planPattern)
                );

                subquery.select(membershipRoot.get("member").get("id"))
                        .where(cb.and(
                                cb.equal(membershipRoot.get("member").get("id"), root.get("id")),
                                planMatch
                        ));

                predicates.add(cb.exists(subquery));
            }

            // 7. Membership status filtering via Subquery
            if (membershipStatus != null) {
                Subquery<UUID> subquery = query.subquery(UUID.class);
                Root<Membership> membershipRoot = subquery.from(Membership.class);

                subquery.select(membershipRoot.get("member").get("id"))
                        .where(cb.and(
                                cb.equal(membershipRoot.get("member").get("id"), root.get("id")),
                                cb.equal(membershipRoot.get("status"), membershipStatus)
                        ));

                predicates.add(cb.exists(subquery));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
