package com.example.profilessystem.entity;

public class GroupMember {
    private String username;
    private String role;
    private int level;
    private int memberLevel;
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public int getMemberLevel() { return memberLevel; }
    public void setMemberLevel(int memberLevel) { this.memberLevel = memberLevel; }
}
