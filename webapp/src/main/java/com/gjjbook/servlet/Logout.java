package com.gjjbook.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/logout")
public class Logout extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().invalidate();
        Cookie emailCookie = new Cookie("email", "");
        Cookie passwordCookie = new Cookie("password", "");
        emailCookie.setMaxAge(0);
        passwordCookie.setMaxAge(0);
        resp.addCookie(emailCookie);
        resp.addCookie(passwordCookie);
        resp.sendRedirect("/login");
    }
}
