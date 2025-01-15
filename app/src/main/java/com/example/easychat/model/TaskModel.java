package com.example.easychat.model;

import com.google.firebase.Timestamp;

public class TaskModel {
    private String taskId;
    private String title;
    private String description;
    private boolean isCompleted;
    private boolean isPinned;
    private Timestamp createdAt;
    private String userId; // Add this field

    public TaskModel() {
        // Default constructor required for Firebase
    }

    public TaskModel(String taskId, String title, String description, boolean isCompleted, boolean isPinned, Timestamp createdAt) {
        this.taskId = taskId;
        this.title = title;
        this.description = description;
        this.isCompleted = isCompleted;
        this.isPinned = isPinned;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public boolean isPinned() {
        return isPinned;
    }

    public void setPinned(boolean pinned) {
        isPinned = pinned;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}