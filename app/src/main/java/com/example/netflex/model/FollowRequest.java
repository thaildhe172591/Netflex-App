package com.example.netflex.model;

import com.google.gson.annotations.SerializedName;

public class FollowRequest {

    @SerializedName("targetId")
    private String targetId;

    @SerializedName("targetType")
    private String targetType;

    // Constructors
    public FollowRequest(String targetId, String targetType) {
        this.targetId = targetId;
        this.targetType = targetType;
    }

    // Getters and Setters
    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }
}
