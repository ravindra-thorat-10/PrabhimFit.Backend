package com.example.prabhim.dto.member;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;

public class MemberRenewalRequest {

    private String planName;
    private String planType;

    @Min(value = 1, message = "Plan duration must be at least 1 month")
    private Integer durationMonths = 12;

    @DecimalMin(value = "0.0", message = "Price cannot be negative")
    private BigDecimal price;

    private String paymentMethod = "CARD";
    private Boolean autoRenew = false;

    public MemberRenewalRequest() {
    }

    public MemberRenewalRequest(String planName, String planType, Integer durationMonths, BigDecimal price, String paymentMethod, Boolean autoRenew) {
        this.planName = planName;
        this.planType = planType;
        this.durationMonths = durationMonths != null ? durationMonths : 12;
        this.price = price;
        this.paymentMethod = paymentMethod != null ? paymentMethod : "CARD";
        this.autoRenew = autoRenew != null ? autoRenew : false;
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

    public Integer getDurationMonths() {
        return durationMonths;
    }

    public void setDurationMonths(Integer durationMonths) {
        this.durationMonths = durationMonths;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Boolean getAutoRenew() {
        return autoRenew;
    }

    public void setAutoRenew(Boolean autoRenew) {
        this.autoRenew = autoRenew;
    }
}
