package com.example.prabhim.specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.example.prabhim.entity.Lead;

import jakarta.persistence.criteria.Predicate;

public class LeadSpecification {

    public static Specification<Lead> filter(
            UUID userId,
            String search,
            String status,
            String source,
            String priority,
            String assignedStaff,
            String gender,
            LocalDate startDate,
            LocalDate endDate) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Mandatory Multi-Tenant User Isolation
            predicates.add(cb.equal(root.get("user").get("id"), userId));

            // 2. Search query across all major text fields
            if (search != null && !search.trim().isEmpty()) {
                String pattern = "%" + search.trim().toLowerCase() + "%";
                Predicate nameMatch = cb.like(cb.lower(root.get("name")), pattern);
                Predicate phoneMatch = cb.like(cb.lower(cb.coalesce(root.get("phone"), "")), pattern);
                Predicate emailMatch = cb.like(cb.lower(cb.coalesce(root.get("email"), "")), pattern);
                Predicate goalMatch = cb.like(cb.lower(cb.coalesce(root.get("fitnessGoal"), "")), pattern);
                Predicate planMatch = cb.like(cb.lower(cb.coalesce(root.get("targetPlan"), "")), pattern);
                Predicate sourceMatch = cb.like(cb.lower(cb.coalesce(root.get("source"), "")), pattern);
                Predicate actionMatch = cb.like(cb.lower(cb.coalesce(root.get("nextAction"), "")), pattern);
                Predicate staffMatch = cb.like(cb.lower(cb.coalesce(root.get("assignedStaff"), "")), pattern);
                Predicate addressMatch = cb.like(cb.lower(cb.coalesce(root.get("address"), "")), pattern);

                predicates.add(cb.or(nameMatch, phoneMatch, emailMatch, goalMatch, planMatch, sourceMatch, actionMatch, staffMatch, addressMatch));
            }

            // 3. Status filter with frontend alias support
            if (status != null && !status.trim().isEmpty() && !status.equalsIgnoreCase("ALL")) {
                String s = status.trim().toUpperCase();
                if ("NEW".equals(s) || "NEW_INQUIRY".equals(s)) {
                    predicates.add(cb.or(
                            cb.equal(cb.upper(root.get("status")), "NEW"),
                            cb.equal(cb.upper(root.get("status")), "NEW_INQUIRY")
                    ));
                } else if ("FOLLOW_UP".equals(s) || "FOLLOWUP".equals(s)) {
                    predicates.add(cb.or(
                            cb.equal(cb.upper(root.get("status")), "FOLLOWUP_SCHEDULED"),
                            cb.equal(cb.upper(root.get("status")), "CONTACTED")
                    ));
                } else if ("TRIAL".equals(s)) {
                    predicates.add(cb.or(
                            cb.equal(cb.upper(root.get("status")), "TRIAL_VISITED"),
                            cb.equal(root.get("trialCompleted"), true),
                            cb.like(cb.lower(cb.coalesce(root.get("nextAction"), "")), "%trial%")
                    ));
                } else {
                    predicates.add(cb.equal(cb.upper(root.get("status")), s));
                }
            }

            // 4. Source filter
            if (source != null && !source.trim().isEmpty() && !source.equalsIgnoreCase("ALL")) {
                predicates.add(cb.like(cb.lower(root.get("source")), "%" + source.trim().toLowerCase() + "%"));
            }

            // 5. Priority filter
            if (priority != null && !priority.trim().isEmpty() && !priority.equalsIgnoreCase("ALL")) {
                predicates.add(cb.equal(cb.upper(root.get("priority")), priority.trim().toUpperCase()));
            }

            // 6. Assigned Staff filter
            if (assignedStaff != null && !assignedStaff.trim().isEmpty() && !assignedStaff.equalsIgnoreCase("ALL")) {
                predicates.add(cb.like(cb.lower(root.get("assignedStaff")), "%" + assignedStaff.trim().toLowerCase() + "%"));
            }

            // 7. Gender filter
            if (gender != null && !gender.trim().isEmpty() && !gender.equalsIgnoreCase("ALL")) {
                predicates.add(cb.equal(cb.lower(root.get("gender")), gender.trim().toLowerCase()));
            }

            // 8. Follow-up Date range filter
            if (startDate != null && endDate != null) {
                predicates.add(cb.between(root.get("followUpDate"), startDate, endDate));
            } else if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("followUpDate"), startDate));
            } else if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("followUpDate"), endDate));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
