package com.example.profilessystem.auth;

/** 会话中保存登录身份的 key。 */
public final class SessionKeys {

    /** 已登录用户的用户名。 */
    public static final String USER = "loginUser";

    /** 已登录管理员的账号名。 */
    public static final String MANAGER = "loginManager";

    private SessionKeys() {
    }
}
