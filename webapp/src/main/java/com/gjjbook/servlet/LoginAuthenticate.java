package com.gjjbook.servlet;

import com.gjjbook.AccountService;
import com.gjjbook.ServiceException;
import com.gjjbook.domain.Account;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/login_authenticate")
public class LoginAuthenticate extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher rq = getServletContext().getRequestDispatcher("/login");
        String email = (String) req.getAttribute("email");
        String password = (String) req.getAttribute("password");
        if (email == null || password == null) {
            email = req.getParameter("email");
            password = req.getParameter("password");
        }
        if (email == null || password == null) {
            req.setAttribute("errMsg", "Please Enter UserName and Password");
            rq.forward(req, resp);
        } else {
            HttpSession session = req.getSession();
            try {
                AccountService service = (AccountService) session.getAttribute("accountService");
                if (service == null) {
                    service = new AccountService();
                    session.setAttribute("accountService", service);
                }
                if (service.isPasswordMatch(email, password)) {
                    Account loggedUser = service.getByEmail(email);
                    session.setAttribute("loggedUser", loggedUser);
                    if (req.getParameter("rememberMe") != null) {
                        resp.addCookie(new Cookie("email", email));
                        resp.addCookie(new Cookie("password", password));
                    }
                    String path = (String) req.getAttribute("path");
                    if (path == null) {
                        path = "/account?id=" + loggedUser.getId();
                    }
                    resp.sendRedirect(path);
                } else {
                    req.setAttribute("errMsg", "Email/password does not match");
                    rq.forward(req, resp);
                }
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }
    }
}
