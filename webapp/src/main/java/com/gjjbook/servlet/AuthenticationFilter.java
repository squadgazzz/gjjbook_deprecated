package com.gjjbook.servlet;

import com.gjjbook.service.AccountService;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {
    private ServletContext servletContext;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        servletContext = filterConfig.getServletContext();
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
        HttpSession currentSession = req.getSession();

        if (!path.contains("/login") && !path.contains("/IMG") && !path.contains("/CSS") &&
                !path.contains("/webjars") && !path.contains("/register") && !path.contains("/account_registration") &&
                (currentSession == null || currentSession.getAttribute("loggedUser") == null)) {
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
                    checkAccountService(session);
                    req.setAttribute("email", email);
                    req.setAttribute("password", password);
                    req.setAttribute("path", path);
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

    private void checkAccountService(HttpSession session) throws ServletException {
        if (session.getAttribute("accountService") == null) {
            ApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            session.setAttribute("accountService", appContext.getBean(AccountService.class));
        }
    }

    @Override
    public void destroy() {

    }
}
