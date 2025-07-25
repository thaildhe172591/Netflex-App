package com.example.netflex.model;

import com.google.gson.annotations.SerializedName;

public class ReviewRequest {

    @SerializedName("targetId")
    private String targetId;

    @SerializedName("targetType")
    private String targetType;

    @SerializedName("rating")
    private int rating;

    public ReviewRequest(String targetId, String targetType, int rating) {
        this.targetId = targetId;
        this.targetType = targetType;
        this.rating = rating;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
