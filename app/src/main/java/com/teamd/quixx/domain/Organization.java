package com.teamd.quixx.domain;

public class Organization {
    private OrganizationInfo data;
    private String message;
    private String status;

    public OrganizationInfo getData() {
        return data;
    }

    public void setData(OrganizationInfo data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
