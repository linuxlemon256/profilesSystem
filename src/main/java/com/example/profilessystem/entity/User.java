package com.example.profilessystem.entity;

import java.time.LocalDateTime;

public class User {
    private String username;
    private String password;
    private String storage;
    private String code;
    private int isin = 0;
    private int level = 0;
    private String email;
    private String reason;
    private LocalDateTime createdAt;
    private String recoveryType;
    private String recoveryReason;
    private int recoveryStatus = 0;
    private LocalDateTime recoveryCreatedAt;
    public User() {
    }
    public User(String username, String password, String storage, String code, int isin,  String email, String reason) {
        this.username = username;
        this.password = password;
        this.storage = storage;
        this.code = code;
        this.isin = isin;
        this.email = email;
        this.reason = reason;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getStorage() {
        return storage;
    }
    public void setStorage(String storage) {
        this.storage = storage;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public int getIsin() {
        return isin;
    }
    public void setIsin(int isin) {
        this.isin = isin;
    }
    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public String getRecoveryType() { return recoveryType; }
    public void setRecoveryType(String recoveryType) { this.recoveryType = recoveryType; }
    public String getRecoveryReason() { return recoveryReason; }
    public void setRecoveryReason(String recoveryReason) { this.recoveryReason = recoveryReason; }
    public int getRecoveryStatus() { return recoveryStatus; }
    public void setRecoveryStatus(int recoveryStatus) { this.recoveryStatus = recoveryStatus; }
    public LocalDateTime getRecoveryCreatedAt() { return recoveryCreatedAt; }
    public void setRecoveryCreatedAt(LocalDateTime recoveryCreatedAt) { this.recoveryCreatedAt = recoveryCreatedAt; }
}
