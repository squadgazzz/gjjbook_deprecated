package com.gjjbook.servlet;

import com.gjjbook.domain.Account;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/")
public class IndexFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession session = ((HttpServletRequest) request).getSession();
        Account account = (Account) session.getAttribute("loggedUser");
        String whereTo;
        if (account != null) {
            whereTo = "/account?id=" + account.getId();
        } else {
            whereTo = "/login";
        }

        ((HttpServletResponse) response).sendRedirect(whereTo);
    }

    @Override
    public void destroy() {

    }
}
