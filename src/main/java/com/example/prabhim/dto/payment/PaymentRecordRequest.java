package com.example.prabhim.dto.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.example.prabhim.entity.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class PaymentRecordRequest {

    private UUID memberId;

    private String memberCode;

    @JsonAlias({"athleteName", "traineeName", "customerName"})
    private String memberName;

    private UUID membershipPlanId;

    @JsonAlias({"planName", "membershipPlanName", "associatedPlan"})
    private String planName;

    private String invoiceNumber;

    @NotNull(message = "Payment amount is required")
    @DecimalMin(value = "0.01", message = "Payment amount must be greater than zero")
    @JsonAlias({"feeAmount", "totalAmount"})
    private BigDecimal amount;

    @JsonAlias({"method", "mode", "paymentMode"})
    private String paymentMethod;

    @JsonAlias({"paymentStatus"})
    private PaymentStatus status = PaymentStatus.PAID;

    @JsonFormat(pattern = "yyyy-MM-dd[ HH:mm:ss]")
    @JsonAlias({"date", "transactionDate"})
    private LocalDateTime paymentDate;

    @JsonAlias({"notes", "transactionNotes", "reference", "utrNumber", "voucherNotes"})
    private String description;

    @JsonAlias({"referenceNumber", "utr", "txnId"})
    private String transactionReference;

    public PaymentRecordRequest() {
    }

    public UUID getMemberId() {
        return memberId;
    }

    public void setMemberId(UUID memberId) {
        this.memberId = memberId;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public UUID getMembershipPlanId() {
        return membershipPlanId;
    }

    public void setMembershipPlanId(UUID membershipPlanId) {
        this.membershipPlanId = membershipPlanId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }
}
