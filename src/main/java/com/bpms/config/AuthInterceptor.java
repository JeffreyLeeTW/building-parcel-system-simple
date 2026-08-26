package com.bpms.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        HttpSession session = request.getSession(false);

        if (uri.startsWith("/resident")) {
            if (session == null || session.getAttribute(SessionKeys.RESIDENT_ID) == null) {
                response.sendRedirect(request.getContextPath() + "/login/resident");
                return false;
            }
        } else if (uri.startsWith("/admin") && !uri.equals("/admin/login")) {
            if (session == null || session.getAttribute(SessionKeys.PARCELMAN_ID) == null) {
                response.sendRedirect(request.getContextPath() + "/admin/login");
                return false;
            }
        }
        return true;
    }
}
