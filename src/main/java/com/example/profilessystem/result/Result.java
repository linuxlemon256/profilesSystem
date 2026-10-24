package com.example.profilessystem.result;

import java.time.LocalDateTime;

public class Result {
    private int code;
    private String message;
    private Object data;
    private LocalDateTime time;
    public Result(int code, String message, Object data,  LocalDateTime time) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.time = time;
    }
    public Result() {}
    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }
    public LocalDateTime getTime() {
        return time;
    }
    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
