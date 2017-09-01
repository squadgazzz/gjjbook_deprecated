package com.gjjbook.servlet;

import com.gjjbook.AccountService;
import com.gjjbook.ServiceException;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        String path = req.getRequestURI();
        String params = req.getQueryString();
        if (params != null) {
            path += "?" + params;
        }
        Cookie[] cookies = req.getCookies();

        if (!path.contains("/login") && !path.contains("/IMG") && !path.contains("/CSS") &&
                !path.contains("/webjars") && !path.contains("/register") && !path.contains("/account_registration") &&
                (req.getSession() == null || req.getSession().getAttribute("loggedUser") == null)) {
            String email = null;
            String password = null;

            if (cookies == null || cookies.length == 0) {
                res.sendRedirect("/login");
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
                    HttpSession session = req.getSession();
                    req.setAttribute("email", email);
                    req.setAttribute("password", password);
                    req.setAttribute("path", path);
                    if (session.getAttribute("accountService") == null) {
                        try {
                            session.setAttribute("accountService", new AccountService());
                        } catch (ServiceException e) {
                            throw new ServletException(e);
                        }
                    }
                    RequestDispatcher rd = session.getServletContext().getRequestDispatcher("/login_authenticate");
                    rd.forward(req, res);
                } else {
                    res.sendRedirect("/login");
                }
            }
        } else {
            filterChain.doFilter(req, res);
        }
    }

    @Override
    public void destroy() {

    }
}
