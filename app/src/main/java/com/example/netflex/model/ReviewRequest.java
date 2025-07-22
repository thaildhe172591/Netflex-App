package com.example.netflex.model;

import com.google.gson.annotations.SerializedName;

public class ReviewRequest {

    @SerializedName("targetId")
    private String targetId;

    @SerializedName("targetType")
    private String targetType; // e.g., "Movie", "Serie"

    @SerializedName("rating")
    private float rating; // Rating from 1 to 5, can be float for half-stars if supported

    // Constructors
    public ReviewRequest(String targetId, String targetType, float rating) {
        this.targetId = targetId;
        this.targetType = targetType;
        this.rating = rating;
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

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
