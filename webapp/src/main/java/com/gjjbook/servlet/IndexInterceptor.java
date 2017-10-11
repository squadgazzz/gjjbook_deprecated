package com.gjjbook.servlet;

import com.gjjbook.domain.Account;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class IndexInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("loggedUser");
        String whereTo;
        if (account != null) {
            whereTo = "/account?id=" + account.getId();
        } else {
            whereTo = "/login";
        }

        response.sendRedirect(request.getContextPath() + whereTo);
        return false;
    }
}
