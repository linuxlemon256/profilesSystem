package com.example.profilessystem.entity;

import java.time.LocalDateTime;

public class Notification {
    private long id;
    private String username;
    private String senderName;
    private String content;
    private int readStatus;
    private LocalDateTime createdAt;
    public long getId(){return id;} public void setId(long id){this.id=id;}
    public String getUsername(){return username;} public void setUsername(String username){this.username=username;}
    public String getSenderName(){return senderName;} public void setSenderName(String senderName){this.senderName=senderName;}
    public String getContent(){return content;} public void setContent(String content){this.content=content;}
    public int getReadStatus(){return readStatus;} public void setReadStatus(int readStatus){this.readStatus=readStatus;}
    public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime createdAt){this.createdAt=createdAt;}
}
