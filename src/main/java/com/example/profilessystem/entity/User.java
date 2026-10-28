package com.example.profilessystem.entity;

import java.time.LocalDateTime;

public class User {
    private String username;
    private String password;
    private String storage;
    private String code;
    private int isin = 0;
    private String email;
    private String reason;
    private LocalDateTime createdAt;
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
}
