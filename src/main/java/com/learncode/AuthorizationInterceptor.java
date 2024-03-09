package com.learncode;

import org.springframework.web.servlet.HandlerInterceptor;

import com.learncode.Entity.User;
import com.learncode.Entity.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AuthorizationInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession httpSession = request.getSession();
        User user = (User) httpSession.getAttribute("user");
        if (user != null) {
            boolean isAdmin = user.getRole().equals(UserRole.ADMIN) || user.getRole().equals(UserRole.EMPLOYEE);
            request.setAttribute("isAdmin", isAdmin);
        }
        return true; // Cho phép tiếp tục xử lý request
    }
}
