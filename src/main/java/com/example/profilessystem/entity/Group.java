package com.example.profilessystem.entity;

import java.util.ArrayList;
import java.time.LocalDateTime;

public class Group {
    private int id;
    private String name;
    private String description;
    private ArrayList<User> users;
    private String ownerUsername;
    private LocalDateTime createdAt;
    private int memberCount;
    public Group() {
    }
    public Group(int id, String name, String description, ArrayList<User> users) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.users = users;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public ArrayList<User> getUsers() {
        return users;
    }
    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
    public String getOwnerUsername() { return ownerUsername; }
    public void setOwnerUsername(String ownerUsername) { this.ownerUsername = ownerUsername; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public int getMemberCount() { return memberCount; }
    public void setMemberCount(int memberCount) { this.memberCount = memberCount; }
}
