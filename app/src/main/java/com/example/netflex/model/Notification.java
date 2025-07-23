package com.example.netflex.model;

public class Notification {
    private String title;
    private String content;
    private String createdAt;

    // Constructor
    public Notification(String title, String content, String createdAt) {
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }

    // Getter methods
    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
