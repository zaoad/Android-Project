package com.teamd.quixx.domain;

import java.util.List;

public class Delivery {
    private List<DeliveryInfo> data;
    private String message;
    private String status;

    public List<DeliveryInfo> getData() {
        return data;
    }

    public void setData(List<DeliveryInfo> data) {
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
