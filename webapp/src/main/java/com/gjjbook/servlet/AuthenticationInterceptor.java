package com.gjjbook.servlet;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        String params = request.getQueryString();
        if (params != null) {
            path += "?" + params;
        }
        Cookie[] cookies = request.getCookies();
        HttpSession currentSession = request.getSession();

        if (!path.contains("/login") && !path.contains("/IMG") && !path.contains("/CSS") &&
                !path.contains("/webjars") && !path.contains("/register") && !path.contains("/account_registration") &&
                (currentSession == null || currentSession.getAttribute("loggedUser") == null)) {
            String email = null;
            String password = null;

            if (cookies == null || cookies.length == 0) {
                response.sendRedirect(request.getContextPath() + "/login");
                return false;
            } else {
                for (Cookie c : cookies) {
                    String cookieName = c.getName();

                    if ("email".equals(cookieName)) {
                        email = c.getValue();
                    } else if ("password".equals(cookieName)) {
                        password = c.getValue();
                    }
                }
                if (email != null && password != null) {
                    HttpSession session = request.getSession();
                    request.setAttribute("email", email);
                    request.setAttribute("password", password);
                    request.setAttribute("path", path);
                    RequestDispatcher rd = session.getServletContext().getRequestDispatcher("/login_authenticate");
                    rd.forward(request, response);
                } else {
                    response.sendRedirect(request.getContextPath() + "/login");
                    return false;
                }
            }
        }

        return super.preHandle(request, response, handler);
    }
}
