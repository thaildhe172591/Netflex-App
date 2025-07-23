package com.example.netflex.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Follow {
    @SerializedName("targetId")
    private String targetId;
    @SerializedName("targetType")
    private String targetType;
    @SerializedName("userId")
    private String userId;
    @SerializedName("createdAt")
    private Date createdAt;

    public Follow() {
    }

    public Follow(String targetId, String targetType, String userId, Date createdAt) {
        this.targetId = targetId;
        this.targetType = targetType;
        this.userId = userId;
        this.createdAt = createdAt;
    }

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
