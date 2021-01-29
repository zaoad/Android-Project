package com.teamd.quixx.domain;

import java.util.List;

public class History {
    private List<HistoryInfo> data;
    private String message;
    private String status;

    public List<HistoryInfo> getData() {
        return data;
    }

    public void setData(List<HistoryInfo> data) {
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