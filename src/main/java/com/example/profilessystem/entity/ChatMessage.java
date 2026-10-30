package com.example.profilessystem.entity;

import java.time.LocalDateTime;

public class ChatMessage {
    private long id;
    private long groupId;
    private String senderUsername;
    private String receiverUsername;
    private String content;
    private LocalDateTime createdAt;
    private int senderLevel;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getGroupId() { return groupId; }
    public void setGroupId(long groupId) { this.groupId = groupId; }
    public String getSenderUsername() { return senderUsername; }
    public void setSenderUsername(String senderUsername) { this.senderUsername = senderUsername; }
    public String getReceiverUsername() { return receiverUsername; }
    public void setReceiverUsername(String receiverUsername) { this.receiverUsername = receiverUsername; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public int getSenderLevel() { return senderLevel; }
    public void setSenderLevel(int senderLevel) { this.senderLevel = senderLevel; }
}
