package com.example.netflex.model;

public class SignUpResponse {
    private String title;
    private Integer status;
    private String detail;
    private String instance;
    private String traceId;

    // Default constructor
    public SignUpResponse() {}

    // Constructor for success case (assuming "1" maps to status 200)
    public SignUpResponse(Integer status) {
        this.status = status;
    }

    // Constructor for error case
    public SignUpResponse(String title, Integer status, String detail, String instance, String traceId) {
        this.title = title;
        this.status = status;
        this.detail = detail;
        this.instance = instance;
        this.traceId = traceId;
    }

    // Getters
    public String getTitle() { return title; }
    public Integer getStatus() { return status; }
    public String getDetail() { return detail; }
    public String getInstance() { return instance; }
    public String getTraceId() { return traceId; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setStatus(Integer status) { this.status = status; }
    public void setDetail(String detail) { this.detail = detail; }
    public void setInstance(String instance) { this.instance = instance; }
    public void setTraceId(String traceId) { this.traceId = traceId; }

    public boolean isSuccess() {
        return status != null && status == 200;
    }

    public boolean isError() {
        return status != null && status == 400;
    }
}