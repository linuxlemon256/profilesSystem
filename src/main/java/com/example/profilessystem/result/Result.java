package com.example.profilessystem.result;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * 统一响应结果。
 *
 * @param <T> 业务数据类型
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> {

    public static final int CODE_SUCCESS = 200;
    public static final int CODE_UNAUTHORIZED = 401;
    public static final int CODE_BAD_REQUEST = 400;
    public static final int CODE_FORBIDDEN = 403;
    public static final int CODE_ERROR = 500;

    private int code;
    private String message;
    private T data;
    private Instant timestamp;

    public Result() {
    }

    public Result(int code, String message, T data, Instant timestamp) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = timestamp;
    }

    /** 成功，无返回数据。 */
    public static Result<Void> ok() {
        return new Result<>(CODE_SUCCESS, "success", null, now());
    }

    /** 成功，带返回数据。 */
    public static <T> Result<T> ok(T data) {
        return new Result<>(CODE_SUCCESS, "success", data, now());
    }

    /** 失败，默认 500。 */
    public static <T> Result<T> fail(String message) {
        return fail(CODE_ERROR, message);
    }

    /** 失败，自定义状态码。 */
    public static <T> Result<T> fail(int code, String message) {
        return new Result<>(code, message, null, now());
    }

    /** 当前时间，截断到毫秒，避免纳秒级多余精度。 */
    private static Instant now() {
        return Instant.now().truncatedTo(ChronoUnit.MILLIS);
    }

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
