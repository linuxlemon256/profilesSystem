package com.example.profilessystem.entity;

public class UserAdminUpdateBody {
    private String email;
    private String reason;
    private Integer level;
    private String code;
    private String password;
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
