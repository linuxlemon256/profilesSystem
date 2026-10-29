package com.example.profilessystem.entity;

public class PasswordRecoveryBody {
    private String username;
    private String code;
    private String password;
    private String confirmPassword;
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
}
