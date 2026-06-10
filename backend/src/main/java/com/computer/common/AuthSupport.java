package com.computer.common;

import javax.servlet.http.HttpServletRequest;

public class AuthSupport {
    public static final int ADMIN_GROUP = 1;
    public static final int USER_GROUP = 2;

    private AuthSupport() {
    }

    public static Integer getUserId(HttpServletRequest request) {
        return JwtUtil.getUserId(request.getHeader("token"));
    }

    public static Integer getUserGroup(HttpServletRequest request) {
        return JwtUtil.getUserGroup(request.getHeader("token"));
    }

    public static Result requireAdmin(HttpServletRequest request) {
        Integer userGroup = getUserGroup(request);
        if (userGroup == null || userGroup != ADMIN_GROUP) {
            return Result.error(403, "无权访问");
        }
        return null;
    }

    public static boolean isAdmin(HttpServletRequest request) {
        Integer userGroup = getUserGroup(request);
        return userGroup != null && userGroup == ADMIN_GROUP;
    }
}
