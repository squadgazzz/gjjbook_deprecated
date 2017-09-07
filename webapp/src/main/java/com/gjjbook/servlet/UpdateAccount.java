package com.gjjbook.servlet;

import com.gjjbook.AccountService;
import com.gjjbook.ServiceException;
import com.gjjbook.domain.Account;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/updateaccount")
public class UpdateAccount extends RegisterUpdateAccount {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Account account = (Account) session.getAttribute("loggedUser");
        if (!account.getId().equals(Integer.valueOf(req.getParameter("id")))) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You can't edit other accounts");
        } else {
            AccountService service = (AccountService) session.getAttribute("accountService");
            try {
                parseAccountData(req, account);
                service.update(account);
                String password = req.getParameter("password");
                if (password != null && password.length() > 0) {
                    service.setPassword(account, req.getParameter("password"));
                }
                updateCookies(req, account, service);
            } catch (ServiceException e) {
                throw new ServletException(e);
            }
            resp.sendRedirect("/account?id=" + account.getId());
        }
    }

    private void updateCookies(HttpServletRequest req, Account account, AccountService service) throws ServiceException {
        Cookie[] cookies = req.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie c : cookies) {
                if (c.getName().equals("email")) {
                    String accEmail = account.getEmail();
                    if (!c.getValue().equals(accEmail)) {
                        c.setValue(accEmail);
                    }
                } else if (c.getName().equals("password")) {
                    String accPassword = service.getPassword(account);
                    if (!c.getValue().equals(accPassword)) {
                        c.setValue(accPassword);
                    }
                }
            }
        }
    }
}
