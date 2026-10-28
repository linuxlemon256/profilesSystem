package com.example.profilessystem.entity;

/** 管理员登录请求体。 */
public class ManagerLoginBody {

    private String managername;
    private String password;

    public ManagerLoginBody() {
    }

    public ManagerLoginBody(String managername, String password) {
        this.managername = managername;
        this.password = password;
    }

    public String getManagername() {
        return managername;
    }

    public void setManagername(String managername) {
        this.managername = managername;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
