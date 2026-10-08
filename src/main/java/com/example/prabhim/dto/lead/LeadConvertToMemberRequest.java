package com.example.prabhim.dto.lead;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

public class LeadConvertToMemberRequest {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    private UUID trainerId;
    private Boolean recordPayment;
    private BigDecimal amountPaid;
    private String paymentMethod;
    private String notes;

    public LeadConvertToMemberRequest() {
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public UUID getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(UUID trainerId) {
        this.trainerId = trainerId;
    }

    public Boolean getRecordPayment() {
        return recordPayment;
    }

    public void setRecordPayment(Boolean recordPayment) {
        this.recordPayment = recordPayment;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
