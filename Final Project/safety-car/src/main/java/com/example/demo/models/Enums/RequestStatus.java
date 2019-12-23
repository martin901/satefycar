package com.example.demo.models.Enums;

public enum RequestStatus {
    PENDING("Pending"), APPROVED("Approved"), DECLINED("Declined"), CANCELED("Canceled"), EXPIRED("Expired");

    private String viewName;

    RequestStatus(String viewName) {
        this.viewName = viewName;
    }

    public String getViewName() {
        return viewName;
    }
}
