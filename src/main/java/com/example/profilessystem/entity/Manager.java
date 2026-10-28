package com.example.profilessystem.entity;

public class Manager {
    private String managername;
    private String password;
    private int level;

    public String getName() {
        return managername;
    }
    public void setName(String username) {
        this.managername = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    public Manager(String username, String password, int level) {
        this.managername = username;
        this.password = password;
        this.level = level;
    }
    public Manager() {}
}
