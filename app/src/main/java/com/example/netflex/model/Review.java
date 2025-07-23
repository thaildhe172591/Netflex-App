package com.example.netflex.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Review {
    @SerializedName("targetId")
    private String targetId;

    @SerializedName("targetType")
    private String targetType;

    @SerializedName("userId")
    private String userId;

    @SerializedName("rating")
    private int rating;

    @SerializedName("comment")
    private String comment;

    @SerializedName("likeCount")
    private int likeCount;

    @SerializedName("createdAt")
    private Date createdAt;
    public Review() {
    }

    public Review(String targetId, String targetType, String userId, int rating, String comment, int likeCount, Date createdAt) {
        this.targetId = targetId;
        this.targetType = targetType;
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
        this.likeCount = likeCount;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
