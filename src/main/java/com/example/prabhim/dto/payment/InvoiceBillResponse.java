package com.example.prabhim.dto.payment;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

import com.example.prabhim.entity.Payment;
import com.example.prabhim.entity.enums.PaymentStatus;

public class InvoiceBillResponse {

    private UUID paymentId;
    private String invoiceNumber;
    private String organizationName = "PrabhimFit - Treasury & Billing";
    private String facility = "Downtown Flagship / All Zones";
    private LocalDateTime invoiceDate;
    private String invoiceDateDisplay;
    private String memberName;
    private String memberCode;
    private String memberEmail;
    private String memberPhone;
    private String planName;
    private BigDecimal baseAmount;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private String totalAmountDisplay;
    private String paymentMethod;
    private PaymentStatus paymentStatus;
    private String transactionReference;
    private String notes;
    private String issuedBy = "Operations / PrabhimFit Console";

    public InvoiceBillResponse() {
    }

    public static InvoiceBillResponse fromEntity(Payment payment) {
        if (payment == null) {
            return null;
        }
        InvoiceBillResponse bill = new InvoiceBillResponse();
        bill.setPaymentId(payment.getId());
        bill.setInvoiceNumber(payment.getInvoiceNumber());
        bill.setInvoiceDate(payment.getPaymentDate());
        if (payment.getPaymentDate() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, hh:mm a", Locale.ENGLISH);
            bill.setInvoiceDateDisplay(payment.getPaymentDate().format(formatter));
        }

        if (payment.getMember() != null) {
            String fullName = (payment.getMember().getFirstName() != null ? payment.getMember().getFirstName() : "")
                    + " " + (payment.getMember().getLastName() != null ? payment.getMember().getLastName() : "");
            bill.setMemberName(fullName.trim().isEmpty() ? "N/A" : fullName.trim());
            bill.setMemberCode(payment.getMember().getMemberCode());
            bill.setMemberEmail(payment.getMember().getEmail());
            bill.setMemberPhone(payment.getMember().getPhone());
        } else {
            bill.setMemberName(payment.getMemberName() != null ? payment.getMemberName() : "N/A");
            bill.setMemberCode(payment.getMemberCode());
        }

        if (payment.getMembershipPlan() != null) {
            bill.setPlanName(payment.getMembershipPlan().getName());
        } else {
            bill.setPlanName(payment.getPlanName() != null ? payment.getPlanName() : "General Subscription");
        }

        BigDecimal total = payment.getAmount() != null ? payment.getAmount() : BigDecimal.ZERO;
        bill.setTotalAmount(total);
        bill.setBaseAmount(total);
        bill.setTaxAmount(BigDecimal.ZERO);

        NumberFormat indiaFormat = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        bill.setTotalAmountDisplay(indiaFormat.format(total).replace("INR", "₹").trim());

        bill.setPaymentMethod(payment.getPaymentMethod());
        bill.setPaymentStatus(payment.getStatus() != null ? payment.getStatus() : PaymentStatus.PAID);
        bill.setTransactionReference(payment.getTransactionReference());
        bill.setNotes(payment.getDescription() != null ? payment.getDescription() : payment.getNotes());

        return bill;
    }

    public UUID getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(UUID paymentId) {
        this.paymentId = paymentId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public LocalDateTime getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDateTime invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceDateDisplay() {
        return invoiceDateDisplay;
    }

    public void setInvoiceDateDisplay(String invoiceDateDisplay) {
        this.invoiceDateDisplay = invoiceDateDisplay;
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

    public String getMemberEmail() {
        return memberEmail;
    }

    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }

    public String getMemberPhone() {
        return memberPhone;
    }

    public void setMemberPhone(String memberPhone) {
        this.memberPhone = memberPhone;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public BigDecimal getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(BigDecimal baseAmount) {
        this.baseAmount = baseAmount;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTotalAmountDisplay() {
        return totalAmountDisplay;
    }

    public void setTotalAmountDisplay(String totalAmountDisplay) {
        this.totalAmountDisplay = totalAmountDisplay;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }
}
