package com.example.prabhim.dto;

public class LogoutRequest {

    private String refresh;

    public LogoutRequest() {
    }

    public LogoutRequest(String refresh) {
        this.refresh = refresh;
    }

    public String getRefresh() {
        return refresh;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }
}
