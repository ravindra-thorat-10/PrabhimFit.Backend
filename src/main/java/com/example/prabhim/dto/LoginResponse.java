package com.example.prabhim.dto;

public class LoginResponse {

    private String access;
    private String refresh;
    private UserResponse user;

    public LoginResponse() {
    }

    public LoginResponse(String access, String refresh, UserResponse user) {
        this.access = access;
        this.refresh = refresh;
        this.user = user;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getRefresh() {
        return refresh;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }
}
