package com.example.profilessystem.auth;

import com.example.profilessystem.mapper.ManagerMapper;
import com.example.profilessystem.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

/**
 * 登录校验：
 * 管理接口与 managerPanel.html 需要管理员会话；
 * learnServer.html 与其余 /api/user/** 需要用户会话。
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final ManagerMapper managerMapper;

    public AuthInterceptor(ManagerMapper managerMapper) {
        this.managerMapper = managerMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {
        String path = request.getRequestURI();

        if (isManagerOnly(path)) {
            // 引导：系统里还没有任何管理员时，允许创建第一个管理员
            if (managerMapper.count() == 0
                    && "POST".equalsIgnoreCase(request.getMethod())
                    && "/api/manager/insert/".equals(path)) {
                return true;
            }
            if (sessionValue(request, SessionKeys.MANAGER) != null) {
                return true;
            }
            return reject(request, response, "/managerLogin.html");
        }

        if (requiresUser(path) && sessionValue(request, SessionKeys.USER) == null) {
            return reject(request, response, "/login.html");
        }
        return true;
    }

    private String sessionValue(HttpServletRequest request, String key) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        Object value = session.getAttribute(key);
        return value == null ? null : value.toString();
    }

    private boolean isManagerOnly(String path) {
        if (path.startsWith("/api/manager/") || path.equals("/managerPanel.html")) {
            return true;
        }
        return path.equals("/api/user/get/all")
                || path.equals("/api/user/get/uncommit")
                || path.equals("/api/user/get/commit")
                || path.equals("/api/user/get/rejected")
                || path.equals("/api/user/insert")
                || path.startsWith("/api/user/approve/")
                || path.startsWith("/api/user/reject/");
    }

    private boolean requiresUser(String path) {
        // 学习路线页面公开；云端存储（进度同步）仍需登录
        return path.startsWith("/api/user/")
                || path.startsWith("/api/progress");
    }

    private boolean reject(HttpServletRequest request, HttpServletResponse response, String redirect)
            throws IOException {
        if (request.getRequestURI().endsWith(".html")) {
            response.sendRedirect(redirect);
            return false;
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(
                "{\"code\":" + Result.CODE_UNAUTHORIZED + ",\"message\":\"请先登录\"}");
        return false;
    }
}
