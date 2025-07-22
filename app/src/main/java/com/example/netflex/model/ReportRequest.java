package com.example.netflex.model;

import com.google.gson.annotations.SerializedName;

public class ReportRequest {
    @SerializedName("reason")
    private String reason;
    @SerializedName("description")
    private String description;
    public  ReportRequest(){}
    public ReportRequest(String reason, String description) {
        this.reason = reason;
        this.description = description;
    }
}
