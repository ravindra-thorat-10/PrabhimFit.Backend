package com.example.prabhim.specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.example.prabhim.entity.Lead;
import com.example.prabhim.entity.enums.Gender;
import com.example.prabhim.entity.enums.LeadPriority;
import com.example.prabhim.entity.enums.LeadSource;
import com.example.prabhim.entity.enums.LeadStatus;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

public class LeadSpecification {

    public static Specification<Lead> filter(
            String search,
            Gender gender,
            LeadStatus status,
            LeadPriority priority,
            LeadSource source,
            LocalDate followUpStartDate,
            LocalDate followUpEndDate,
            LocalDate joiningStartDate,
            LocalDate joiningEndDate,
            Boolean actionRequiredToday,
            Boolean paymentDueOnly) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Search across name, code, phone, email, profession, area, goal, notes
            if (search != null && !search.trim().isEmpty()) {
                String searchPattern = "%" + search.trim().toLowerCase() + "%";
                Expression<String> fullNameExp = cb.lower(cb.coalesce(root.get("fullName"), ""));
                Expression<String> codeExp = cb.lower(cb.coalesce(root.get("leadCode"), ""));
                Expression<String> phoneExp = cb.lower(cb.coalesce(root.get("phone"), ""));
                Expression<String> emailExp = cb.lower(cb.coalesce(root.get("email"), ""));
                Expression<String> professionExp = cb.lower(cb.coalesce(root.get("profession"), ""));
                Expression<String> areaExp = cb.lower(cb.coalesce(root.get("residentialArea"), ""));
                Expression<String> goalExp = cb.lower(cb.coalesce(root.get("fitnessGoal"), ""));
                Expression<String> notesExp = cb.lower(cb.coalesce(root.get("discussionNotes"), ""));

                predicates.add(cb.or(
                        cb.like(fullNameExp, searchPattern),
                        cb.like(codeExp, searchPattern),
                        cb.like(phoneExp, searchPattern),
                        cb.like(emailExp, searchPattern),
                        cb.like(professionExp, searchPattern),
                        cb.like(areaExp, searchPattern),
                        cb.like(goalExp, searchPattern),
                        cb.like(notesExp, searchPattern)
                ));
            }

            // 2. Gender filtering
            if (gender != null) {
                predicates.add(cb.equal(root.get("gender"), gender));
            }

            // 3. Status filtering
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            // 4. Priority filtering
            if (priority != null) {
                predicates.add(cb.equal(root.get("priority"), priority));
            }

            // 5. Source filtering
            if (source != null) {
                predicates.add(cb.equal(root.get("inquirySource"), source));
            }

            // 6. Follow-up date range
            if (followUpStartDate != null && followUpEndDate != null) {
                predicates.add(cb.between(root.get("followUpDate"), followUpStartDate, followUpEndDate));
            } else if (followUpStartDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("followUpDate"), followUpStartDate));
            } else if (followUpEndDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("followUpDate"), followUpEndDate));
            }

            // 7. Joining date range
            if (joiningStartDate != null && joiningEndDate != null) {
                predicates.add(cb.between(root.get("expectedJoiningDate"), joiningStartDate, joiningEndDate));
            } else if (joiningStartDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("expectedJoiningDate"), joiningStartDate));
            } else if (joiningEndDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("expectedJoiningDate"), joiningEndDate));
            }

            // 8. Action Required Today (due on or before today, and not already converted/lost)
            if (Boolean.TRUE.equals(actionRequiredToday)) {
                LocalDate today = LocalDate.now();
                Predicate dueTodayOrBefore = cb.lessThanOrEqualTo(root.get("followUpDate"), today);
                Predicate isActionRequired = cb.equal(root.get("status"), LeadStatus.ACTION_REQUIRED);
                Predicate notClosed = cb.not(root.get("status").in(Arrays.asList(LeadStatus.CONVERTED, LeadStatus.WON, LeadStatus.LOST)));

                predicates.add(cb.and(cb.or(dueTodayOrBefore, isActionRequired), notClosed));
            }

            // 9. Payment Due Only (pending fee > 0)
            if (Boolean.TRUE.equals(paymentDueOnly)) {
                predicates.add(cb.greaterThan(root.get("pendingPlanFee"), BigDecimal.ZERO));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
