package com.gjjbook.servlet;

import com.gjjbook.AccountService;
import com.gjjbook.ServiceException;
import com.gjjbook.domain.Account;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
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
            parseAccountData(req, account);
            AccountService service = (AccountService) session.getAttribute("accountService");
            try {
                service.update(account);
                String password = req.getParameter("password");
                if (password != null) {
                    service.setPassword(account, req.getParameter("password"));
                }
            } catch (ServiceException e) {
                throw new ServletException(e);
            }
//            getServletContext().getRequestDispatcher("/account?id=" + account.getId()).forward(req, resp);
            resp.sendRedirect("/account?id=" + account.getId());
        }
    }
}
