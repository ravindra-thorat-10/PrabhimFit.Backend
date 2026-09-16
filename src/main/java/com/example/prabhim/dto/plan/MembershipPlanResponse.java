package com.example.prabhim.dto.plan;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.example.prabhim.entity.MembershipPlan;

public class MembershipPlanResponse {

    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer durationMonths;
    private String durationLabel;
    private List<String> features;
    private String status;
    private long activeSubscriptions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public MembershipPlanResponse() {
    }

    public static MembershipPlanResponse fromEntity(MembershipPlan plan, long activeSubscriptions) {
        if (plan == null) {
            return null;
        }
        MembershipPlanResponse resp = new MembershipPlanResponse();
        resp.setId(plan.getId());
        resp.setName(plan.getName());
        resp.setDescription(plan.getDescription());
        resp.setPrice(plan.getPrice());
        resp.setDurationMonths(plan.getDurationMonths());
        resp.setDurationLabel(formatDuration(plan.getDurationMonths()));
        resp.setFeatures(plan.getFeaturesList());
        resp.setStatus(plan.getStatus());
        resp.setActiveSubscriptions(activeSubscriptions);
        resp.setCreatedAt(plan.getCreatedAt());
        resp.setUpdatedAt(plan.getUpdatedAt());
        return resp;
    }

    private static String formatDuration(Integer months) {
        if (months == null || months <= 1) {
            return "1 Month";
        }
        return months + " Months";
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getDurationMonths() {
        return durationMonths;
    }

    public void setDurationMonths(Integer durationMonths) {
        this.durationMonths = durationMonths;
    }

    public String getDurationLabel() {
        return durationLabel;
    }

    public void setDurationLabel(String durationLabel) {
        this.durationLabel = durationLabel;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getActiveSubscriptions() {
        return activeSubscriptions;
    }

    public void setActiveSubscriptions(long activeSubscriptions) {
        this.activeSubscriptions = activeSubscriptions;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
