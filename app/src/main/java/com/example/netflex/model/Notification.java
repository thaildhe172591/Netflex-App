package com.example.netflex.model;

public class Notification {
    private String title;
    private String content;
    private String createdAt;
    private boolean haveRead;

    public Notification(String title) {
        this.title = title;
    }

    public Notification(String title, boolean haveRead, String createdAt, String content) {
        this.title = title;
        this.haveRead = haveRead;
        this.createdAt = createdAt;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public boolean isHaveRead() {
        return haveRead;
    }
}
