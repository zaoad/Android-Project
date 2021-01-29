package com.teamd.quixx.domain;

public class DeliveryMan {
    private DeliveryManInfo data;
    private String message;
    private String status;

    public DeliveryManInfo getData() {
        return data;
    }

    public void setData(DeliveryManInfo data) {
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
