package com.example.profilessystem.entity;

import java.util.ArrayList;

public class Group {
    private int id;
    private String name;
    private String description;
    private ArrayList<User> users;
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
}
