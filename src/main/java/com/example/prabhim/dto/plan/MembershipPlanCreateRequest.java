package com.example.prabhim.dto.plan;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class MembershipPlanCreateRequest {

    private String name;
    private String planName;

    private String description;
    private String shortDescription;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", message = "Price cannot be negative")
    private BigDecimal price;

    @NotNull(message = "Duration in months is required")
    @Min(value = 1, message = "Duration must be at least 1 month")
    private Integer durationMonths = 1;

    private String features;
    private List<String> featuresList;

    private String status = "ACTIVE";

    public MembershipPlanCreateRequest() {
    }

    public String resolveName() {
        if (name != null && !name.trim().isEmpty()) {
            return name.trim();
        }
        if (planName != null && !planName.trim().isEmpty()) {
            return planName.trim();
        }
        return "New Plan";
    }

    public String resolveDescription() {
        if (description != null && !description.trim().isEmpty()) {
            return description.trim();
        }
        if (shortDescription != null && !shortDescription.trim().isEmpty()) {
            return shortDescription.trim();
        }
        return null;
    }

    public String resolveFeaturesString() {
        if (features != null && !features.trim().isEmpty()) {
            return features.trim();
        }
        if (featuresList != null && !featuresList.isEmpty()) {
            return String.join(", ", featuresList);
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
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

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public List<String> getFeaturesList() {
        return featuresList;
    }

    public void setFeaturesList(List<String> featuresList) {
        this.featuresList = featuresList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
