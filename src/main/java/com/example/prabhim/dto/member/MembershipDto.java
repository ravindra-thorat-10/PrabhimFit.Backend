package com.example.prabhim.dto.member;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import com.example.prabhim.entity.Membership;
import com.example.prabhim.entity.enums.MembershipStatus;

public class MembershipDto {

    private UUID id;
    private String planName;
    private String planType;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal price;
    private MembershipStatus status;
    private Boolean autoRenew;
    private Long daysRemaining;

    public MembershipDto() {
    }

    public static MembershipDto fromEntity(Membership membership) {
        if (membership == null) {
            return null;
        }
        MembershipDto dto = new MembershipDto();
        dto.setId(membership.getId());
        dto.setPlanName(membership.getPlanName());
        dto.setPlanType(membership.getPlanType());
        dto.setStartDate(membership.getStartDate());
        dto.setEndDate(membership.getEndDate());
        dto.setPrice(membership.getPrice());
        dto.setStatus(membership.getStatus());
        dto.setAutoRenew(membership.getAutoRenew());

        if (membership.getEndDate() != null) {
            long days = ChronoUnit.DAYS.between(LocalDate.now(), membership.getEndDate());
            dto.setDaysRemaining(days >= 0 ? days : 0L);
        }
        return dto;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public MembershipStatus getStatus() {
        return status;
    }

    public void setStatus(MembershipStatus status) {
        this.status = status;
    }

    public Boolean getAutoRenew() {
        return autoRenew;
    }

    public void setAutoRenew(Boolean autoRenew) {
        this.autoRenew = autoRenew;
    }

    public Long getDaysRemaining() {
        return daysRemaining;
    }

    public void setDaysRemaining(Long daysRemaining) {
        this.daysRemaining = daysRemaining;
    }
}
