package com.example.prabhim.dto.member;

public class QuickCheckInRequest {

    private String facility;

    public QuickCheckInRequest() {
    }

    public QuickCheckInRequest(String facility) {
        this.facility = facility;
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }
}
