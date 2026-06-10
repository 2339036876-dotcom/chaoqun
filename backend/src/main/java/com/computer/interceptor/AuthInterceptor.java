package com.computer.interceptor;

import com.computer.common.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    private static final List<String> EXACT_PUBLIC_URIS = Arrays.asList(
            "/api/user/login",
            "/api/user/admin/login",
            "/api/user/register",
            "/api/goods/list",
            "/api/goods/type/list",
            "/api/slides/list",
            "/api/article/list",
            "/api/forum/list",
            "/api/notice/list"
    );

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        String uri = request.getRequestURI();
        System.out.println("Request URI: " + uri);
        System.out.println("Is public URI: " + isPublicUri(uri));
        if (isPublicUri(uri)) {
            return true;
        }
        String token = request.getHeader("token");
        System.out.println("Token: " + token);
        if (token == null || token.isEmpty()) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"msg\":\"未登录\"}");
            return false;
        }
        try {
            Claims claims = JwtUtil.parseToken(token);
            System.out.println("Claims: " + claims);
            // 只对管理员专用接口要求管理员权限
            if (isAdminUri(uri)) {
                Integer userGroup = claims.get("userGroup", Integer.class);
                System.out.println("User group: " + userGroup);
                if (userGroup == null || userGroup != 1) {
                    response.setStatus(403);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"code\":403,\"msg\":\"无权访问\"}");
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"msg\":\"token无效或已过期\"}");
            return false;
        }
    }

    private boolean isAdminUri(String uri) {
        return uri.startsWith("/api/user/list")
                || uri.startsWith("/api/user/delete")
                || uri.startsWith("/api/user/state")
                || uri.startsWith("/api/order/list")
                || uri.startsWith("/api/order/update")
                || uri.startsWith("/api/order/delivery")
                || uri.startsWith("/api/order/delete")
                || uri.startsWith("/api/comment/reply")
                || uri.startsWith("/api/comment/delete");
    }

    private boolean isPublicUri(String uri) {
        if (EXACT_PUBLIC_URIS.contains(uri)) {
            return true;
        }
        return uri.startsWith("/uploads/")
                || uri.startsWith("/api/goods/detail/")
                || uri.startsWith("/api/article/detail/")
                || uri.startsWith("/api/forum/detail/")
                || uri.startsWith("/api/notice/detail/")
                || uri.startsWith("/api/comment/list/")
                || uri.startsWith("/api/score/list/");
    }
}
