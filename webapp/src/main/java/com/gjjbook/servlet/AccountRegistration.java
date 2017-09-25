package com.gjjbook.servlet;

import com.gjjbook.domain.Account;
import com.gjjbook.service.AccountService;
import com.gjjbook.service.ServiceException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/account_registration")
@MultipartConfig(maxFileSize = 1_617_721)
public class AccountRegistration extends RegisterUpdateAccount {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Account account = new Account();
        parseAccountData(req, account);
        HttpSession session = req.getSession();
        Object serviceAttribute = session.getAttribute("accountService");
        AccountService service;
        try {
            if (serviceAttribute == null) {
                service = accountService;
                session.setAttribute("accountService", service);
            } else {
                service = (AccountService) serviceAttribute;
            }
            service.create(account);
        } catch (ServiceException e) {
            throw new ServletException(e);
        }

        RequestDispatcher rd = req.getServletContext().getRequestDispatcher("/login");
        rd.forward(req, resp);
    }
}
