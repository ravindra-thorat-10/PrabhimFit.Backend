package com.example.prabhim.dto.lead;

public class RemindersDto {

    private Boolean callReminder = true;
    private String callTime = "11:00 AM";
    private Boolean messageReminder = true;
    private Boolean paymentReminder = false;
    private String paymentDueDate;

    public RemindersDto() {
    }

    public Boolean getCallReminder() {
        return callReminder;
    }

    public void setCallReminder(Boolean callReminder) {
        this.callReminder = callReminder;
    }

    public String getCallTime() {
        return callTime;
    }

    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }

    public Boolean getMessageReminder() {
        return messageReminder;
    }

    public void setMessageReminder(Boolean messageReminder) {
        this.messageReminder = messageReminder;
    }

    public Boolean getPaymentReminder() {
        return paymentReminder;
    }

    public void setPaymentReminder(Boolean paymentReminder) {
        this.paymentReminder = paymentReminder;
    }

    public String getPaymentDueDate() {
        return paymentDueDate;
    }

    public void setPaymentDueDate(String paymentDueDate) {
        this.paymentDueDate = paymentDueDate;
    }
}
