package com.example.prabhim.dto.payment;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

import com.example.prabhim.entity.Payment;
import com.example.prabhim.entity.enums.PaymentStatus;

public class PaymentResponse {

    private UUID id;
    private String invoiceNumber;
    private UUID memberId;
    private String memberName;
    private String memberCode;
    private String memberAvatar;
    private UUID membershipPlanId;
    private String planName;
    private BigDecimal amount;
    private String amountDisplay;
    private String paymentMethod;
    private LocalDateTime paymentDate;
    private String paymentDateDisplay;
    private PaymentStatus status;
    private String description;
    private String transactionReference;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PaymentResponse() {
    }

    public static PaymentResponse fromEntity(Payment entity) {
        if (entity == null) {
            return null;
        }
        PaymentResponse resp = new PaymentResponse();
        resp.setId(entity.getId());
        resp.setInvoiceNumber(entity.getInvoiceNumber());

        // Member resolution
        if (entity.getMember() != null) {
            resp.setMemberId(entity.getMember().getId());
            resp.setMemberCode(entity.getMember().getMemberCode());
            String fullName = (entity.getMember().getFirstName() != null ? entity.getMember().getFirstName() : "")
                    + " " + (entity.getMember().getLastName() != null ? entity.getMember().getLastName() : "");
            resp.setMemberName(fullName.trim().isEmpty() ? "N/A" : fullName.trim());
            resp.setMemberAvatar(entity.getMember().getProfileImage());
        } else {
            resp.setMemberName(entity.getMemberName() != null && !entity.getMemberName().trim().isEmpty()
                    ? entity.getMemberName().trim() : "N/A");
            resp.setMemberCode(entity.getMemberCode());
            resp.setMemberAvatar(entity.getMemberAvatar());
        }

        // Plan resolution
        if (entity.getMembershipPlan() != null) {
            resp.setMembershipPlanId(entity.getMembershipPlan().getId());
            resp.setPlanName(entity.getMembershipPlan().getName());
        } else {
            resp.setPlanName(entity.getPlanName());
        }

        // Amount & formatting
        resp.setAmount(entity.getAmount());
        if (entity.getAmount() != null) {
            NumberFormat indiaFormat = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
            resp.setAmountDisplay(indiaFormat.format(entity.getAmount()).replace("INR", "₹").trim());
        }

        resp.setPaymentMethod(entity.getPaymentMethod());
        resp.setPaymentDate(entity.getPaymentDate());
        if (entity.getPaymentDate() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);
            resp.setPaymentDateDisplay(entity.getPaymentDate().format(formatter));
        }

        resp.setStatus(entity.getStatus() != null ? entity.getStatus() : PaymentStatus.PAID);
        resp.setDescription(entity.getDescription());
        resp.setTransactionReference(entity.getTransactionReference());
        resp.setCreatedAt(entity.getCreatedAt());
        resp.setUpdatedAt(entity.getUpdatedAt());

        return resp;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public UUID getMemberId() {
        return memberId;
    }

    public void setMemberId(UUID memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public String getMemberAvatar() {
        return memberAvatar;
    }

    public void setMemberAvatar(String memberAvatar) {
        this.memberAvatar = memberAvatar;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getAmountDisplay() {
        return amountDisplay;
    }

    public void setAmountDisplay(String amountDisplay) {
        this.amountDisplay = amountDisplay;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentDateDisplay() {
        return paymentDateDisplay;
    }

    public void setPaymentDateDisplay(String paymentDateDisplay) {
        this.paymentDateDisplay = paymentDateDisplay;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
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
