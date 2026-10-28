package com.example.profilessystem.entity;

/** 保存学习进度的请求体，data 是前端序列化后的进度 JSON 字符串。 */
public class ProgressBody {

    private String data;

    public ProgressBody() {
    }

    public ProgressBody(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
